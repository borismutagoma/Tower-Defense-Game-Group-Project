/**
 * Bloon Tower Defense — web front-end controller.
 *
 * Talks to the Java WebGameServer REST API (unchanged):
 *   GET  /api/state   POST /api/tick   POST /api/round
 *   POST /api/tower {type,row,col}     POST /api/sell {row,col}   POST /api/reset
 *
 * The Java backend owns all the rules. This file only:
 *   - sends the player's actions to the API,
 *   - animates the state it receives (smooth movement between ticks,
 *     darts, pops, explosions) using the drawings in render.js.
 * If the API cannot be reached, it falls back to the small offline
 * simulation in sim.js so the page still works on a static host.
 */
(function () {
    'use strict';

    const A = window.TDArt;
    const $ = id => document.getElementById(id);

    // ================================================================ CONFIG
    const TICK_MS = { 1: 340, 2: 170, 3: 85 };     // delay between ticks per speed
    const ROUND_PAUSE_MS = 900;                    // pause between auto rounds

    const KIND_BY_CLASS = {
        MonkeyDartTower: 'monkey', NeedleTower: 'needle', GorrillaTower: 'gorilla', GorillaTower: 'gorilla',
        SniperMonkeyTower: 'sniper', BombTower: 'bomb', SlowTower: 'slow', FreezeTower: 'freeze',
    };
    const BLURBS = {
        monkey: 'Throws a dart at the balloon furthest along the track.',
        needle: 'Fires every single tick. Short range, never rests.',
        gorilla: 'Huge reach, throws heavy sharp darts.',
        sniper: 'Can hit a balloon anywhere on the map.',
        bomb: 'Explodes on impact and hurts nearby balloons too.',
        slow: 'Coats balloons in goo so they move at half speed.',
        freeze: 'Freezes balloons in place for a few ticks.',
    };
    const DAMAGE_KINDS = new Set(['monkey', 'needle', 'gorilla', 'sniper', 'bomb']);

    // ================================================================= STATE
    const G = {
        state: null,
        mode: null,            // 'java' | 'local'
        apiBase: null,
        playing: false,
        speed: 1,
        autoRounds: false,
        placing: null,         // shop id being placed
        selected: null,        // "row,col" of the selected tower
        hoverCell: null,       // [row, col]
        hoverShop: null,
        shiftKey: false,
        queue: Promise.resolve(),
        loopToken: 0,
    };

    // Visual model (pure presentation, rebuilt from the state the server sends)
    const V = {
        canvas: null, ctx: null, dpr: 1, cell: 40,
        bg: null, bgKey: '', pathKey: '', path: [],
        balloons: [], dying: [], effects: [], texts: [],
        towers: new Map(),
        nextId: 1, tickNo: 0, tickStart: 0, tickDur: 300,
        time: 0, last: performance.now(),
    };

    // ================================================================== API
    async function fetchJson(url, opts = {}, timeout = 4000) {
        const ctl = new AbortController();
        const timer = setTimeout(() => ctl.abort(), timeout);
        try {
            const res = await fetch(url, Object.assign({ signal: ctl.signal }, opts));
            const data = await res.json();
            return { ok: res.ok, data };
        } finally {
            clearTimeout(timer);
        }
    }

    async function api(endpoint, body) {
        if (G.mode === 'local') {
            const sim = window.localSim;
            switch (endpoint) {
                case '/state': return { ok: true, data: sim.getState() };
                case '/tick': return { ok: true, data: sim.tick() };
                case '/round': return { ok: true, data: sim.startRound() };
                case '/tower': return { ok: true, data: sim.placeTower(body.type, body.row, body.col) };
                case '/sell': return { ok: true, data: sim.sellTower(body.row, body.col) };
                case '/reset': return { ok: true, data: sim.reset() };
            }
        }
        const opts = endpoint === '/state' ? {} : {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body || {}),
        };
        return fetchJson(G.apiBase + endpoint, opts);
    }

    async function connect() {
        const bases = [];
        if (location.protocol.startsWith('http')) bases.push(location.origin + '/api');
        bases.push('http://localhost:8080/api');
        for (const base of [...new Set(bases)]) {
            try {
                const { data } = await fetchJson(base + '/state', {}, 2500);
                if (data && data.player && data.board) {
                    G.mode = 'java';
                    G.apiBase = base;
                    return data;
                }
            } catch (e) { /* try next */ }
        }
        G.mode = 'local';
        window.localSim.init();
        return window.localSim.getState();
    }

    /** Runs actions one after another so ticks and clicks never overlap. */
    function enqueue(task) {
        G.queue = G.queue.then(task).catch(err => {
            console.error(err);
            stopPlaying();
            toast('Lost connection to the game server', 'warn');
            setBadge('down');
        });
        return G.queue;
    }

    // ============================================================== ACTIONS
    async function doRound() {
        if (!G.state || G.state.roundActive || G.state.gameOver) return;
        const { data } = await api('/round');
        ingest(data, 300);
        const n = G.mode === 'local' ? window.localSim.s.balloons.length : data.balloons.length;
        showBanner(`Round ${data.round}`, `${n} balloon${n === 1 ? '' : 's'} incoming!`);
    }

    async function doTick(ms) {
        if (!G.state || !G.state.roundActive) return;
        const { data } = await api('/tick');
        ingest(data, ms);
        if (!data.roundActive && !data.gameOver) {
            toast(`Round ${data.round} cleared!`, 'good');
        }
    }

    async function doPlace(type, row, col) {
        const { data } = await api('/tower', { type, row, col });
        ingest(data, -1);
        const ok = data.towers.some(t => t.row === row && t.col === col);
        if (ok) {
            const info = shopInfo(type);
            addText(col + 0.5, row + 0.1, `-$${info ? info.cost : ''}`, '#ffd23f');
            addEffect({ kind: 'pulse', x: col + 0.5, y: row + 0.5, radius: 0.7, fill: 'rgba(255,255,255,0.35)', stroke: '#fff', dur: 350 });
        } else {
            toast('The server refused this spot', 'warn');
        }
    }

    async function doSell(row, col) {
        const t = G.state.towers.find(t => t.row === row && t.col === col);
        if (!t) return;
        const { data } = await api('/sell', { row, col });
        ingest(data, -1);
        G.selected = null;
        addText(col + 0.5, row + 0.2, `+$${Math.floor(t.cost / 2)}`, '#ffd23f');
        addEffect({ kind: 'boom', x: col + 0.5, y: row + 0.5, radius: 0.5, rot: Math.random(), dur: 380 });
        updateUI();
    }

    async function doReset() {
        stopPlaying();
        const { data } = await api('/reset');
        G.selected = null;
        G.placing = null;
        V.pathKey = '';
        ingest(data, 0);
        toast('New game — good luck!', 'good');
    }

    // ------------------------------------------------------------- play loop
    function tickInterval() { return TICK_MS[G.speed] || 300; }

    function startPlaying() {
        if (G.playing || !G.state || G.state.gameOver) return;
        G.playing = true;
        const token = ++G.loopToken;
        updateUI();
        (async () => {
            while (G.playing && token === G.loopToken) {
                const st = G.state;
                if (st.gameOver) break;
                if (!st.roundActive) {
                    if (!G.autoRounds) break;
                    await sleep(ROUND_PAUSE_MS);
                    if (!G.playing || token !== G.loopToken) break;
                    await enqueue(doRound);
                    continue;
                }
                const t0 = performance.now();
                const ms = tickInterval();
                await enqueue(() => doTick(ms));
                const wait = ms - (performance.now() - t0);
                if (wait > 0) await sleep(wait);
            }
            if (token === G.loopToken) { G.playing = false; updateUI(); }
        })();
    }

    function stopPlaying() {
        G.playing = false;
        G.loopToken++;
        updateUI();
    }

    async function onPlayButton() {
        if (!G.state || G.state.gameOver) return;
        if (!G.state.roundActive) {
            await enqueue(doRound);
            startPlaying();
        } else if (!G.playing) {
            startPlaying();
        } else {
            cycleSpeed();
        }
    }

    function cycleSpeed() {
        G.speed = G.speed % 3 + 1;
        toast(`Speed ×${G.speed}`);
        updateUI();
    }

    const sleep = ms => new Promise(r => setTimeout(r, ms));

    // ============================================================ HELPERS
    const clamp = (v, a, b) => Math.max(a, Math.min(b, v));
    const keyOf = (r, c) => r + ',' + c;
    const shopInfo = id => G.state && G.state.shop.find(s => s.id === id);
    const kindOf = t => KIND_BY_CLASS[t.type] || t.id || 'monkey';
    const towerAt = (r, c) => G.state && G.state.towers.find(t => t.row === r && t.col === c);

    function reachCells(range) {
        if (range === -1) return Infinity;
        const s = G.state.board;
        return (range / 100) * Math.max(s.rows, s.cols) * 0.15;
    }

    function placeError(id, r, c) {
        const s = G.state;
        if (r < 0 || c < 0 || r >= s.board.rows || c >= s.board.cols) return 'Out of the map';
        if (s.grid[r][c] === 1) return "You can't build on the track!";
        if (towerAt(r, c)) return 'There is already a tower here';
        const info = shopInfo(id);
        if (!info) return 'Unknown tower';
        if (s.player.money < info.cost) return 'Not enough money!';
        return null;
    }

    // ---- path maths: convert server (row, col) positions to "progress along the path"
    function pathPoint(s) {
        const P = V.path;
        if (!P.length) return [0, 0];
        if (P.length === 1) return [P[0][1] + 0.5, P[0][0] + 0.5];
        s = clamp(s, 0, P.length - 1);
        const k = Math.min(Math.floor(s), P.length - 2);
        const t = s - k;
        const [r0, c0] = P[k], [r1, c1] = P[k + 1];
        return [c0 + (c1 - c0) * t + 0.5, r0 + (r1 - r0) * t + 0.5];
    }

    function progressOf(row, col) {
        const P = V.path;
        if (P.length < 2) return 0;
        let best = 0, bd = Infinity;
        for (let k = 0; k < P.length - 1; k++) {
            const [r0, c0] = P[k], [r1, c1] = P[k + 1];
            const dr = r1 - r0, dc = c1 - c0;
            const L = dr * dr + dc * dc || 1;
            const t = clamp(((row - r0) * dr + (col - c0) * dc) / L, 0, 1);
            const d = Math.hypot(r0 + dr * t - row, c0 + dc * t - col);
            if (d < bd - 1e-9) { bd = d; best = k + t; }
        }
        return best;
    }

    function tickAlpha(now) {
        return V.tickDur <= 0 ? 1 : clamp((now - V.tickStart) / V.tickDur, 0, 1);
    }

    function balloonXY(b, s) {
        const [x, y] = pathPoint(s);
        return [x + b.jx, y + b.jy];
    }

    function jitterFor(id) {
        const h = Math.sin(id * 12.9898) * 43758.5453;
        const h2 = Math.sin(id * 78.233) * 12345.6789;
        return [((h - Math.floor(h)) - 0.5) * 0.48, ((h2 - Math.floor(h2)) - 0.5) * 0.44];
    }

    // ============================================================ INGEST
    /**
     * Receives a new state from the server and works out what changed
     * (balloons that moved, popped or escaped; towers built or sold) so the
     * renderer can animate it.
     */
    function ingest(ns, animMs) {
        const old = G.state;
        G.state = ns;
        const now = performance.now();
        V.tickNo++;

        const pathKey = JSON.stringify(ns.path) + ns.board.rows + 'x' + ns.board.cols;
        if (pathKey !== V.pathKey) {
            V.pathKey = pathKey;
            V.path = ns.path;
            V.balloons = []; V.dying = []; V.effects = []; V.texts = [];
            V.towers.clear();
            layout(true);
        }

        syncTowers(ns, now);

        // --- match balloons (the server list keeps its order; new ones are appended)
        const alpha = tickAlpha(now);
        const prev = V.balloons;
        const keepTiming = animMs < 0;      // not a tick: keep the running animation
        prev.forEach(b => { b.sNow = b.s0 + (b.s1 - b.s0) * alpha; });
        const next = [], removed = [], hits = [];
        let i = 0;
        for (const nb of ns.balloons) {
            const s = progressOf(nb.row, nb.col);
            let j = i;
            while (j < prev.length && !(prev[j].type === nb.type && s >= prev[j].s1 - 0.05 && s - prev[j].s1 <= 2.01)) j++;
            if (j < prev.length) {
                for (let k = i; k < j; k++) removed.push(prev[k]);
                const pb = prev[j];
                i = j + 1;
                if (nb.life < pb.life - 1e-9) hits.push({ b: pb, s: pb.s1 });
                if (nb.effect !== pb.effect && nb.effect !== 'NONE') effectPulse(nb.effect, pb);
                Object.assign(pb, { s0: keepTiming ? pb.s0 : pb.sNow, s1: s, life: nb.life, maxLife: nb.maxLife, effect: nb.effect, speed: nb.speed });
                next.push(pb);
            } else {
                const id = V.nextId++;
                const [jx, jy] = jitterFor(id);
                next.push({ id, type: nb.type, life: nb.life, maxLife: nb.maxLife, effect: nb.effect, speed: nb.speed, s0: s, s1: s, sNow: s, jx, jy });
            }
        }
        for (let k = i; k < prev.length; k++) removed.push(prev[k]);
        V.balloons = next;
        if (!keepTiming) {
            V.tickStart = now;
            V.tickDur = animMs;
        }

        // --- popped or escaped balloons
        const end = V.path.length - 1;
        const lifeLost = old ? Math.max(0, old.player.life - ns.player.life) : 0;
                for (const b of removed) {
            if (b.s1 + (b.speed || 0) >= end - 1e-6) {
                V.dying.push({ b, s0: b.sNow, s1: end + 0.6, t0: now, dur: Math.max(200, animMs), escape: true });
            } else {
                shootAt(b, b.s1, now, animMs, true);
            }
        }
        hits.forEach(h => shootAt(h.b, h.s, now, animMs, false));

        if (old && ns.player.life < old.player.life) {
            const [x, y] = pathPoint(end);
            addText(Math.min(x, ns.board.cols - 0.6), y - 0.3, `-${lifeLost} ♥`, '#ff5a4a');
            bumpHud('lives', 'hurt');
        }
        if (old && ns.player.money > old.player.money) bumpHud('money', 'bump');

        updateUI();
    }

    function syncTowers(ns, now) {
        const seen = new Set();
        for (const t of ns.towers) {
            const k = keyOf(t.row, t.col);
            seen.add(k);
            let v = V.towers.get(k);
            if (!v) {
                const x = t.col + 0.5, y = t.row + 0.5;
                // face the nearest bit of track when built
                let ang = 0, bd = Infinity;
                for (const [pr, pc] of V.path) {
                    const d = Math.hypot(pc + 0.5 - x, pr + 0.5 - y);
                    if (d < bd) { bd = d; ang = Math.atan2(pr + 0.5 - y, pc + 0.5 - x); }
                }
                v = { key: k, kind: kindOf(t), x, y, angle: ang, recoil: 0, spin: Math.random() * 6, born: now, firedTick: -1, pulsedTick: -1 };
                V.towers.set(k, v);
            }
            v.range = t.range;
            v.reach = reachCells(t.range);
            v.cost = t.cost;
            v.cadence = t.cadence;
        }
        for (const k of [...V.towers.keys()]) if (!seen.has(k)) V.towers.delete(k);
        if (G.selected && !seen.has(G.selected)) G.selected = null;
    }

    /** Visual only: pick the tower that most likely hit this balloon and fire a projectile. */
    function shootAt(b, s, now, animMs, popped) {
        const [x, y] = balloonXY(b, s);
        let best = null, bs = Infinity;
        for (const t of V.towers.values()) {
            if (!DAMAGE_KINDS.has(t.kind)) continue;
            const d = Math.hypot(t.x - x, t.y - y);
            if (d > t.reach + 0.8) continue;
            const score = d + (t.firedTick === V.tickNo && t.kind !== 'bomb' ? 4 : 0);
            if (score < bs) { bs = score; best = t; }
        }
        let arrive = now + 60;
        if (best) {
            const d = Math.hypot(best.x - x, best.y - y);
            if (best.kind === 'bomb' && best.firedTick === V.tickNo && best.lastShot) {
                arrive = best.lastShot.t0 + best.lastShot.dur;      // same blast
            } else {
                let dur = best.kind === 'sniper' ? 70 : Math.min(260, 50 + d * 45);
                if (animMs > 0) dur = Math.min(dur, Math.max(50, animMs * 0.8));
                const shot = { kind: 'proj', ptype: best.kind, x0: best.x, y0: best.y, x1: x, y1: y, t0: now, dur };
                if (best.kind === 'bomb') shot.onHit = () => addEffect({ kind: 'boom', x, y, radius: 1.1, rot: Math.random() * 6, dur: 420 });
                addEffect(shot);
                best.angle = Math.atan2(y - best.y, x - best.x);
                best.recoil = 1;
                best.firedTick = V.tickNo;
                best.lastShot = shot;
                arrive = now + dur;
            }
        }
        if (popped) {
            V.dying.push({ b, s0: s, s1: s, t0: now, dur: Math.max(0, arrive - now), popAt: arrive, x, y });
        } else {
            setTimeout(() => addEffect({ kind: 'pulse', x, y, radius: 0.35, fill: 'rgba(255,255,255,0.5)', stroke: '#fff', dur: 200 }), Math.max(0, arrive - now));
        }
    }

    function effectPulse(effect, b) {
        const kind = effect === 'FREEZE' ? 'freeze' : effect === 'SLOWED' ? 'slow' : null;
        if (!kind) return;
        const [x, y] = balloonXY(b, b.s1);
        let best = null, bd = Infinity;
        for (const t of V.towers.values()) {
            if (t.kind !== kind) continue;
            const d = Math.hypot(t.x - x, t.y - y);
            if (d <= t.reach + 0.8 && d < bd) { bd = d; best = t; }
        }
        if (!best || best.pulsedTick === V.tickNo) return;
        best.pulsedTick = V.tickNo;
        best.recoil = 1;
        addEffect({
            kind: 'pulse', x: best.x, y: best.y, radius: Math.min(best.reach, 6), dur: 450,
            fill: kind === 'freeze' ? 'rgba(170,230,255,0.25)' : 'rgba(170,80,220,0.22)',
            stroke: kind === 'freeze' ? '#dff6ff' : '#c27df0',
        });
    }

    function addEffect(e) {
        e.t0 = e.t0 || performance.now();
        V.effects.push(e);
    }

    function addText(x, y, text, color) {
        V.texts.push({ x, y, text, color, t0: performance.now(), dur: 900 });
    }

    // ============================================================ RENDER
    function layout(force) {
        const s = G.state;
        if (!s) return;
        const area = $('board-area');
        const cs = getComputedStyle(area);
        const aw = area.clientWidth - parseFloat(cs.paddingLeft) - parseFloat(cs.paddingRight) - 20;
        const ah = area.clientHeight - parseFloat(cs.paddingTop) - parseFloat(cs.paddingBottom) - 20;
        const cell = Math.max(14, Math.floor(Math.min(aw / s.board.cols, ah / s.board.rows)));
        const dpr = Math.min(window.devicePixelRatio || 1, 2.5);
        if (!force && cell === V.cell && dpr === V.dpr && V.bg) return;
        V.cell = cell;
        V.dpr = dpr;
        const w = cell * s.board.cols, h = cell * s.board.rows;
        V.canvas.style.width = w + 'px';
        V.canvas.style.height = h + 'px';
        V.canvas.width = Math.round(w * dpr);
        V.canvas.height = Math.round(h * dpr);
        V.bg = A.buildBackground({ rows: s.board.rows, cols: s.board.cols, path: s.path, grid: s.grid, px: cell * dpr });
    }

    function frame(now) {
        const dt = Math.min(0.05, (now - V.last) / 1000);
        V.last = now;
        V.time += dt;
        if (G.state && V.bg) draw(now, dt);
        requestAnimationFrame(frame);
    }

    function draw(now, dt) {
        const ctx = V.ctx, s = G.state;
        const px = V.cell * V.dpr;
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.clearRect(0, 0, V.canvas.width, V.canvas.height);
        ctx.drawImage(V.bg, 0, 0);
        ctx.setTransform(px, 0, 0, px, 0, 0);

        const alpha = tickAlpha(now);
        // rendered balloon positions
        const live = V.balloons.map(b => {
            const sNow = b.s0 + (b.s1 - b.s0) * alpha;
            const [x, y] = balloonXY(b, sNow);
            return { b, s: sNow, x, y };
        });

        // ---- placement preview / selection
        if (G.placing && G.hoverCell) {
            const [r, c] = G.hoverCell;
            const ok = !placeError(G.placing, r, c);
            const info = shopInfo(G.placing);
            ctx.save();
            ctx.fillStyle = ok ? 'rgba(255,255,255,0.22)' : 'rgba(220,40,30,0.35)';
            A.roundRect(ctx, c + 0.05, r + 0.05, 0.9, 0.9, 0.15);
            ctx.fill();
            ctx.restore();
            if (info && info.range !== -1) A.drawRange(ctx, c + 0.5, r + 0.5, reachCells(info.range), ok);
            A.drawTower(ctx, G.placing, c + 0.5, r + 0.5, { ghost: true, angle: -0.6, time: V.time });
        }
        const sel = G.selected && V.towers.get(G.selected);
        if (sel && isFinite(sel.reach)) A.drawRange(ctx, sel.x, sel.y, sel.reach, true);

        // ---- towers
        const towers = [...V.towers.values()].sort((a, b) => a.y - b.y);
        for (const t of towers) {
            // aim at the leading balloon in range
            if (DAMAGE_KINDS.has(t.kind) && t.kind !== 'needle') {
                let target = null;
                for (const L of live) {
                    if (Math.hypot(L.x - t.x, L.y - t.y) <= t.reach && (!target || L.s > target.s)) target = L;
                }
                if (target) {
                    const want = Math.atan2(target.y - t.y, target.x - t.x);
                    let d = want - t.angle;
                    d = Math.atan2(Math.sin(d), Math.cos(d));
                    t.angle += d * Math.min(1, dt * 10);
                }
            }
            if (t.kind === 'needle') t.spin += dt * (G.playing ? 1.6 : 0.4);
            t.recoil = Math.max(0, t.recoil - dt * 6);
            const born = clamp((now - t.born) / 260, 0, 1);
            const scale = born < 1 ? 0.4 + 0.75 * Math.sin(born * Math.PI * 0.75) : 1;
            if (G.selected === t.key) {
                ctx.save();
                ctx.beginPath();
                ctx.arc(t.x, t.y, 0.46 + Math.sin(V.time * 6) * 0.03, 0, A.TAU);
                ctx.lineWidth = 0.06; ctx.strokeStyle = '#ffd23f'; ctx.stroke();
                ctx.restore();
            }
            ctx.save();
            ctx.translate(t.x, t.y); ctx.scale(scale, scale); ctx.translate(-t.x, -t.y);
            A.drawTower(ctx, t.kind, t.x, t.y, { angle: t.angle, recoil: t.recoil, spin: t.spin, time: V.time });
            ctx.restore();
        }

        // ---- balloons (alive + those waiting for the dart to land)
        const drawList = live.map(L => ({ b: L.b, x: L.x, y: L.y }));
        V.dying = V.dying.filter(d => {
            const k = d.dur > 0 ? clamp((now - d.t0) / d.dur, 0, 1) : 1;
            if (d.escape) {
                if (k >= 1) return false;
                const [x, y] = balloonXY(d.b, d.s0 + (d.s1 - d.s0) * k);
                drawList.push({ b: d.b, x, y, fade: 1 - k * k });
                return true;
            }
            if (now >= d.popAt) {
                addEffect({ kind: 'pop', x: d.x, y: d.y, type: d.b.type, rot: Math.random() * 6, dur: 300 });
                addText(d.x, d.y - 0.25, `+${Math.floor((d.b.maxLife || 1) * 20)}`, '#ffd23f');
                return false;
            }
            drawList.push({ b: d.b, x: d.x, y: d.y });
            return true;
        });
        drawList.sort((a, b) => a.y - b.y);
        for (const d of drawList) {
            if (d.fade !== undefined) { ctx.save(); ctx.globalAlpha = d.fade; }
            A.drawBalloon(ctx, d.b, d.x, d.y, V.time);
            if (d.fade !== undefined) ctx.restore();
        }

        // ---- projectiles & effects
        V.effects = V.effects.filter(e => {
            const k = clamp((now - e.t0) / e.dur, 0, 1);
            if (e.kind === 'proj') A.drawProjectile(ctx, { kind: e.ptype, x0: e.x0, y0: e.y0, x1: e.x1, y1: e.y1 }, k);
            else if (e.kind === 'pop') A.drawPop(ctx, e, k);
            else if (e.kind === 'boom') A.drawExplosion(ctx, e, k);
            else if (e.kind === 'pulse') A.drawPulse(ctx, e, k);
            if (k >= 1) { if (e.onHit) e.onHit(); return false; }
            return true;
        });

        // ---- floating texts (drawn in CSS pixels for crisp fonts)
        ctx.setTransform(V.dpr, 0, 0, V.dpr, 0, 0);
        const fs = Math.max(12, Math.round(V.cell * 0.36));
        ctx.font = `${fs}px "Luckiest Guy", "Fredoka", sans-serif`;
        ctx.textAlign = 'center';
        ctx.textBaseline = 'middle';
        ctx.lineJoin = 'round';
        V.texts = V.texts.filter(t => {
            const k = (now - t.t0) / t.dur;
            if (k >= 1) return false;
            const x = t.x * V.cell, y = (t.y - k * 0.7) * V.cell;
            ctx.globalAlpha = k < 0.7 ? 1 : 1 - (k - 0.7) / 0.3;
            ctx.lineWidth = Math.max(3, fs * 0.22);
            ctx.strokeStyle = '#2b1a0c';
            ctx.strokeText(t.text, x, y);
            ctx.fillStyle = t.color;
            ctx.fillText(t.text, x, y);
            return true;
        });
        ctx.globalAlpha = 1;

        // crowd counters: the server can stack many balloons on the same spot
        const groups = new Map();
        for (const L of live) {
            const k = Math.round(L.s * 2);
            const g = groups.get(k) || { n: 0, x: 0, y: 1e9 };
            g.n++; g.x += L.x; g.y = Math.min(g.y, L.y);
            groups.set(k, g);
        }
        const fs2 = Math.max(11, Math.round(V.cell * 0.28));
        ctx.font = `${fs2}px "Luckiest Guy", "Fredoka", sans-serif`;
        for (const g of groups.values()) {
            if (g.n < 3) continue;
            const x = (g.x / g.n) * V.cell, y = (g.y - 0.55) * V.cell;
            ctx.lineWidth = Math.max(3, fs2 * 0.25);
            ctx.strokeStyle = '#2b1a0c';
            ctx.strokeText('×' + g.n, x, y);
            ctx.fillStyle = '#ffffff';
            ctx.fillText('×' + g.n, x, y);
        }

        if (s.gameOver === false && G.placing === null && G.hoverCell) {
            const [r, c] = G.hoverCell;
            V.canvas.classList.toggle('pointer', !!towerAt(r, c));
        }
    }

    // ================================================================= UI
    let shopBuilt = false;
    const hudCache = {};

    function buildShop() {
        const el = $('shop');
        el.innerHTML = '';
        G.state.shop.forEach((info, i) => {
            const b = document.createElement('button');
            b.className = 'shop-tile';
            b.dataset.id = info.id;
            b.title = `${info.name} — $${info.cost}`;
            b.appendChild(A.iconCanvas(ctx => A.drawTower(ctx, info.id, 0.5, 0.5, { angle: -0.7, time: 0, scale: 1 }), 62));
            const price = document.createElement('span');
            price.className = 'price cartoon gold';
            price.textContent = '$' + info.cost;
            const hk = document.createElement('span');
            hk.className = 'hotkey';
            hk.textContent = i + 1;
            b.append(price, hk);
            b.addEventListener('click', () => pickShop(info.id));
            b.addEventListener('mouseenter', () => { G.hoverShop = info.id; renderInfo(); });
            b.addEventListener('mouseleave', () => { G.hoverShop = null; renderInfo(); });
            el.appendChild(b);
        });
        shopBuilt = true;
    }

    function pickShop(id) {
        if (!G.state || G.state.gameOver) return;
        const info = shopInfo(id);
        if (!info) return;
        if (G.placing === id) { G.placing = null; updateUI(); return; }
        if (G.state.player.money < info.cost) {
            toast(`You need $${info.cost - G.state.player.money} more for ${info.name}`, 'warn');
            return;
        }
        G.placing = id;
        G.selected = null;
        updateUI();
    }

    const balloonIcons = {};
    function balloonIcon(type) {
        if (!balloonIcons[type]) {
            balloonIcons[type] = A.iconCanvas(ctx => A.drawBalloon(ctx, { type, life: 1, maxLife: 1, effect: 'NONE', id: 0 }, 0.5, 0.38, 0), 26).toDataURL();
        }
        return balloonIcons[type];
    }

    function setText(id, value) {
        const v = String(value);
        if (hudCache[id] === v) return;
        hudCache[id] = v;
        $(id).textContent = v;
    }

    function bumpHud(id, cls) {
        const el = $(id);
        el.classList.remove('bump', 'hurt');
        void el.offsetWidth;
        el.classList.add(cls);
    }

    function updateUI() {
        const s = G.state;
        if (!s) return;
        if (!shopBuilt) buildShop();

        setText('money', s.player.money);
        setText('lives', Math.max(0, s.player.life));
        setText('round', s.round);
        setText('level', s.gameLevel);
        setText('tick', s.tick);

        // shop tiles
        document.querySelectorAll('.shop-tile').forEach(tile => {
            const info = shopInfo(tile.dataset.id);
            tile.classList.toggle('locked', s.player.money < info.cost);
            tile.classList.toggle('selected', G.placing === info.id);
        });
        if (G.placing && s.player.money < (shopInfo(G.placing) || {}).cost) G.placing = null;
        V.canvas.classList.toggle('placing', !!G.placing);

        // play button
        const play = $('btn-play');
        const ff = s.roundActive && G.playing;
        play.disabled = s.gameOver;
        $('icon-play').style.display = ff ? 'none' : '';
        $('icon-ff').style.display = ff ? '' : 'none';
        play.classList.toggle('speed-2', ff && G.speed === 2);
        play.classList.toggle('speed-3', ff && G.speed === 3);
        let label;
        if (!s.roundActive) label = 'Start round';
        else if (!G.playing) label = 'Resume';
        else label = G.speed === 1 ? 'Fast forward' : `Speed ×${G.speed}`;
        setText('play-label', label);
        play.title = !s.roundActive ? 'Start the next round (R)' : G.playing ? 'Change speed (F)' : 'Resume (P)';

        $('btn-pause').disabled = !s.roundActive && !G.playing;
        $('btn-pause').textContent = G.playing || !s.roundActive ? 'Pause' : 'Resume';
        $('btn-pause').classList.toggle('active', !G.playing && s.roundActive);
        $('btn-step').disabled = !s.roundActive || G.playing;

        // balloon chips
        const counts = {};
        s.balloons.forEach(b => { counts[b.type] = (counts[b.type] || 0) + 1; });
        const chipsHtml = Object.keys(counts).length
            ? Object.entries(counts).map(([t, n]) => `<span class="chip" title="${A.balloonStyle(t).label}"><img src="${balloonIcon(t)}" width="26" height="26" alt="">×${n}</span>`).join('')
            : '<span class="chip empty">No balloons on the map</span>';
        if (hudCache.chips !== chipsHtml) { hudCache.chips = chipsHtml; $('balloon-chips').innerHTML = chipsHtml; }

        // game over
        $('game-over').classList.toggle('hidden', !s.gameOver);
        if (s.gameOver) {
            $('final-round').textContent = s.round;
            if (G.playing) stopPlaying();
        }
        renderInfo();
    }

    let infoCache = '';
    function renderInfo() {
        const s = G.state;
        if (!s) return;
        let html;
        const statRows = info => {
            const reach = info.range === -1 ? 'Whole map' : info.range;
            return `
                <div class="stat-row"><span>Damage</span><b>${info.damage || '—'}</b></div>
                <div class="stat-row"><span>Range</span><b>${reach}</b></div>
                <div class="stat-row"><span>Fires every</span><b>${info.cadence} tick${info.cadence > 1 ? 's' : ''}</b></div>`;
        };
        const sel = G.selected && towerAt(...G.selected.split(',').map(Number));
        if (sel) {
            const kind = kindOf(sel);
            const info = Object.assign({}, shopInfo(kind) || {}, { range: sel.range, cadence: sel.cadence });
            html = `<h3>${info.name || sel.type}</h3>
                    <div class="blurb">${BLURBS[kind] || ''}</div>
                    ${statRows(info)}
                    <button class="btn btn-sell" data-action="sell">Sell for $${Math.floor(sel.cost / 2)}</button>`;
        } else {
            const id = G.hoverShop || G.placing;
            const info = id && shopInfo(id);
            if (info) {
                const short = s.player.money < info.cost;
                html = `<h3>${info.name} <span style="float:right;color:${short ? '#b3261e' : '#2f7d16'}">$${info.cost}</span></h3>
                        <div class="blurb">${BLURBS[id] || ''}</div>
                        ${statRows(info)}`;
            } else {
                html = `<h3>Build your defence</h3>
                        <div class="tip">Pick a tower above, then click on the grass to place it.<br>
                        Click a placed tower to see its range or sell it.</div>`;
            }
        }
        if (html === infoCache) return;
        infoCache = html;
        $('info-card').innerHTML = html;
        const sellBtn = $('info-card').querySelector('[data-action="sell"]');
        if (sellBtn) sellBtn.addEventListener('click', sellSelected);
    }

    function sellSelected() {
        if (!G.selected) return;
        const [r, c] = G.selected.split(',').map(Number);
        enqueue(() => doSell(r, c));
    }

    function toast(msg, cls = '') {
        const el = document.createElement('div');
        el.className = 'toast ' + cls;
        el.textContent = msg;
        const box = $('toasts');
        box.appendChild(el);
        while (box.children.length > 3) box.firstChild.remove();
        setTimeout(() => el.remove(), 2400);
    }

    function showBanner(title, sub) {
        const el = $('round-banner');
        el.innerHTML = `${title}<small>${sub || ''}</small>`;
        el.classList.remove('show');
        void el.offsetWidth;
        el.classList.add('show');
    }

    function setBadge(mode) {
        const el = $('conn-badge');
        el.className = 'badge ' + mode;
        el.textContent = mode === 'java' ? 'Java server' : mode === 'local' ? 'Offline demo' : 'Disconnected';
        el.title = mode === 'java' ? 'Connected to ' + G.apiBase
            : mode === 'local' ? 'Java server not found — running the built-in demo simulation'
            : 'The Java server stopped answering';
    }

    // ============================================================= INPUT
    function cellFromEvent(e) {
        const rect = V.canvas.getBoundingClientRect();
        const c = Math.floor((e.clientX - rect.left) / V.cell);
        const r = Math.floor((e.clientY - rect.top) / V.cell);
        if (!G.state || r < 0 || c < 0 || r >= G.state.board.rows || c >= G.state.board.cols) return null;
        return [r, c];
    }

    function onBoardClick(e) {
        const cell = cellFromEvent(e);
        if (!cell || !G.state || G.state.gameOver) return;
        const [r, c] = cell;
        const existing = towerAt(r, c);
        if (G.placing) {
            if (existing) {
                G.placing = null;
                G.selected = keyOf(r, c);
                updateUI();
                return;
            }
            const err = placeError(G.placing, r, c);
            if (err) { toast(err, 'warn'); return; }
            const id = G.placing;
            if (!e.shiftKey) G.placing = null;
            enqueue(() => doPlace(id, r, c));
            updateUI();
            return;
        }
        G.selected = existing ? keyOf(r, c) : null;
        updateUI();
    }

    function bindEvents() {
        V.canvas.addEventListener('mousemove', e => { G.hoverCell = cellFromEvent(e); });
        V.canvas.addEventListener('mouseleave', () => { G.hoverCell = null; V.canvas.classList.remove('pointer'); });
        V.canvas.addEventListener('click', onBoardClick);
        V.canvas.addEventListener('contextmenu', e => { e.preventDefault(); G.placing = null; G.selected = null; updateUI(); });

        $('btn-play').addEventListener('click', onPlayButton);
        $('btn-pause').addEventListener('click', () => (G.playing ? stopPlaying() : startPlaying()));
        $('btn-step').addEventListener('click', () => { if (!G.playing) enqueue(() => doTick(220)); });
        $('btn-reset').addEventListener('click', () => enqueue(doReset));
        $('btn-play-again').addEventListener('click', () => enqueue(doReset));
        $('auto-rounds').addEventListener('change', e => { G.autoRounds = e.target.checked; });
        $('btn-help').addEventListener('click', () => $('help').classList.remove('hidden'));
        $('btn-help-close').addEventListener('click', () => $('help').classList.add('hidden'));
        $('help').addEventListener('click', e => { if (e.target.id === 'help') $('help').classList.add('hidden'); });

        document.addEventListener('keydown', e => {
            if (e.target.tagName === 'INPUT' && e.target.type !== 'checkbox') return;
            const k = e.key.toLowerCase();
            if (k === ' ') { e.preventDefault(); if (!G.playing) enqueue(() => doTick(220)); }
            else if (k === 'r') onPlayButton();
            else if (k === 'p') (G.playing ? stopPlaying() : startPlaying());
            else if (k === 'f') cycleSpeed();
            else if (k === 'a') { const cb = $('auto-rounds'); cb.checked = !cb.checked; G.autoRounds = cb.checked; toast(`Auto rounds ${cb.checked ? 'on' : 'off'}`); }
            else if (k === 'escape') { G.placing = null; G.selected = null; $('help').classList.add('hidden'); updateUI(); }
            else if (k === 'delete' || k === 'backspace' || k === 's') { if (G.selected) sellSelected(); }
            else if (/^[1-9]$/.test(k) && G.state) { const info = G.state.shop[+k - 1]; if (info) pickShop(info.id); }
        });

        let resizeTimer = null;
        const onResize = () => {
            clearTimeout(resizeTimer);
            resizeTimer = setTimeout(() => layout(false), 80);
        };
        if (window.ResizeObserver) new ResizeObserver(onResize).observe($('board-area'));
        window.addEventListener('resize', onResize);
    }

    // ============================================================== INIT
    async function init() {
        V.canvas = $('board');
        V.ctx = V.canvas.getContext('2d');
        bindEvents();
        const state = await connect();
        setBadge(G.mode);
        // wait for the cartoon font so canvas texts use it
        if (document.fonts && document.fonts.load) {
            try { await Promise.race([document.fonts.load('20px "Luckiest Guy"'), sleep(1500)]); } catch (e) { /* ignore */ }
        }
        $('connecting').classList.add('hidden');
        ingest(state, 0);
        if (G.mode === 'local') toast('Java server not found — playing the offline demo', 'warn');
        requestAnimationFrame(frame);
    }

    init();
})();
