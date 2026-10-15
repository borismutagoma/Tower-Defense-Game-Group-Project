/**
 * Offline fallback simulation.
 *
 * Used only when the Java WebGameServer cannot be reached (e.g. when the
 * page is hosted on GitHub Pages). It mimics the JSON returned by
 * /api/state so the same front-end can render it. The real rules live
 * in the Java backend.
 */
(function () {
    'use strict';

    const SHOP = [
        { id: 'monkey',  name: 'Monkey Dart',   cost: 200, emoji: '🐒', range: 100, cadence: 2, damage: 1 },
        { id: 'needle',  name: 'Needle',        cost: 300, emoji: '🪡', range: 80,  cadence: 1, damage: 1 },
        { id: 'gorilla', name: 'Gorilla',       cost: 300, emoji: '🦍', range: 200, cadence: 4, damage: 2 },
        { id: 'sniper',  name: 'Sniper Monkey', cost: 500, emoji: '🎯', range: -1,  cadence: 5, damage: 4 },
        { id: 'bomb',    name: 'Bomb',          cost: 600, emoji: '💣', range: 150, cadence: 5, damage: 2 },
        { id: 'slow',    name: 'Slow',          cost: 400, emoji: '🐌', range: 100, cadence: 3, damage: 0 },
        { id: 'freeze',  name: 'Freeze',        cost: 500, emoji: '🧊', range: 100, cadence: 6, damage: 0 },
    ];
    const CLASS_OF = {
        monkey: 'MonkeyDartTower', needle: 'NeedleTower', gorilla: 'GorrillaTower',
        sniper: 'SniperMonkeyTower', bomb: 'BombTower', slow: 'SlowTower', freeze: 'FreezeTower',
    };

    const localSim = {
        s: null,

        init() {
            const rows = 12, cols = 18;
            let grid, path;
            // random left-to-right path, retried until it is long enough
            for (let attempt = 0; attempt < 200; attempt++) {
                grid = Array.from({ length: rows }, () => Array(cols).fill(0));
                path = [];
                let r = Math.floor(Math.random() * rows), c = 0;
                grid[r][c] = 1; path.push([r, c]);
                let stuck = false;
                while (c < cols - 1) {
                    const n = [];
                    if (!grid[r][c + 1]) n.push([r, c + 1], [r, c + 1]);
                    if (c > 0 && r > 0 && !grid[r - 1][c]) n.push([r - 1, c]);
                    if (c > 0 && r < rows - 1 && !grid[r + 1][c]) n.push([r + 1, c]);
                    if (!n.length) { stuck = true; break; }
                    [r, c] = n[Math.floor(Math.random() * n.length)];
                    grid[r][c] = 1; path.push([r, c]);
                }
                if (!stuck && path.length >= cols + 8) break;
            }
            this.s = {
                player: { life: 20, money: 800 },
                round: 0, tick: 0, gameLevel: 1, roundActive: false, gameOver: false,
                board: { rows, cols }, grid, path,
                towers: [], balloons: [], shop: SHOP,
            };
        },

        snapshot() {
            const s = this.s;
            const out = Object.assign({}, s, {
                player: Object.assign({}, s.player),
                towers: s.towers.map(t => ({ type: t.type, row: t.row, col: t.col, range: t.range, cadence: t.cadence, cost: t.cost, symbol: t.symbol })),
                balloons: s.balloons.filter(b => b._delay <= 0).map(b => ({
                    type: b.type, row: b.row, col: b.col, life: b.life, maxLife: b.maxLife,
                    speed: b.speed, effect: b.effect, dead: b.dead, finished: b.finished,
                })),
            });
            return JSON.parse(JSON.stringify(out));
        },

        getState() { return this.snapshot(); },

        startRound() {
            const s = this.s;
            if (s.roundActive || s.gameOver) return this.snapshot();
            s.round++;
            if (s.round % 3 === 0) s.gameLevel++;
            const count = 15 + s.round + Math.floor(Math.random() * 6);
            const types = [
                { name: 'CumulusBalloon', life: 1.0, speed: 1.0 },
                { name: 'ZephyrBalloon',  life: 0.8, speed: 1.5 },
                { name: 'StratusBalloon', life: 2.0, speed: 0.5 },
            ];
            const maxI = Math.min(s.gameLevel - 1, types.length - 1);
            const minI = Math.min(Math.floor(s.gameLevel / 3), maxI);
            for (let i = 0; i < count; i++) {
                const t = types[minI + Math.floor(Math.random() * (maxI - minI + 1))];
                s.balloons.push({
                    type: t.name, row: s.path[0][0], col: s.path[0][1],
                    life: t.life, maxLife: t.life, speed: t.speed,
                    effect: 'NONE', dead: false, finished: false,
                    _idx: 0, _wait: 0, _delay: i * 2, _fx: 0,
                });
            }
            s.roundActive = true;
            return this.snapshot();
        },

        tick() {
            const s = this.s;
            if (!s.roundActive || s.gameOver) return this.snapshot();
            const dim = Math.max(s.board.rows, s.board.cols);
            const reach = range => (range === -1 ? Infinity : (range / 100) * dim * 0.15);

            for (const t of s.towers) {
                if (s.tick - t._last < t.cadence) continue;
                let best = null;
                for (const b of s.balloons) {
                    if (b.dead || b.finished || b._delay > 0) continue;
                    if (Math.hypot(t.row - b.row, t.col - b.col) > reach(t.range)) continue;
                    if (!best || b._idx + b._wait > best._idx + best._wait) best = b;
                }
                if (!best) continue;
                if (t.damage > 0) {
                    const hit = t.id === 'bomb'
                        ? s.balloons.filter(b => b._delay <= 0 && Math.hypot(b.row - best.row, b.col - best.col) <= 1.01)
                        : [best];
                    hit.forEach(b => { b.life = Math.max(0, b.life - t.damage); if (b.life <= 0) b.dead = true; });
                }
                if (t.id === 'freeze') { best.effect = 'FREEZE'; best._fx = 10; }
                if (t.id === 'slow') { best.effect = 'SLOWED'; best._fx = 10; }
                t._last = s.tick;
            }

            for (const b of s.balloons) {
                if (b.dead || b.finished) continue;
                if (b._delay > 0) { b._delay--; continue; }
                if (b._fx > 0) b._fx--; else b.effect = 'NONE';
                let v = b.speed;
                if (b.effect === 'FREEZE') v = 0;
                if (b.effect === 'SLOWED') v *= 0.5;
                b._wait += v;
                while (b._wait >= 1 && b._idx < s.path.length - 1) { b._idx++; b._wait -= 1; }
                if (b._idx >= s.path.length - 1) {
                    b.finished = true;
                    [b.row, b.col] = s.path[s.path.length - 1];
                } else {
                    const [r0, c0] = s.path[b._idx], [r1, c1] = s.path[b._idx + 1];
                    b.row = r0 + (r1 - r0) * b._wait;
                    b.col = c0 + (c1 - c0) * b._wait;
                }
            }

            s.balloons = s.balloons.filter(b => {
                if (b.dead) { s.player.money += Math.floor(b.maxLife * 20); return false; }
                if (b.finished) { s.player.life -= Math.ceil(b.maxLife); return false; }
                return true;
            });
            s.tick++;
            if (s.player.life <= 0) { s.player.life = 0; s.gameOver = true; s.roundActive = false; }
            if (!s.balloons.length) s.roundActive = false;
            return this.snapshot();
        },

        placeTower(type, row, col) {
            const s = this.s;
            const info = SHOP.find(t => t.id === type);
            if (!info || s.player.money < info.cost || s.grid[row][col] === 1) return this.snapshot();
            if (s.towers.some(t => t.row === row && t.col === col)) return this.snapshot();
            s.player.money -= info.cost;
            s.towers.push({
                type: CLASS_OF[type], id: type, row, col, range: info.range, cadence: info.cadence,
                damage: info.damage, cost: info.cost, symbol: info.emoji, _last: -100,
            });
            return this.snapshot();
        },

        sellTower(row, col) {
            const s = this.s;
            const i = s.towers.findIndex(t => t.row === row && t.col === col);
            if (i >= 0) { s.player.money += Math.floor(s.towers[i].cost / 2); s.towers.splice(i, 1); }
            return this.snapshot();
        },

        reset() { this.init(); return this.snapshot(); },
    };

    window.localSim = localSim;
})();
