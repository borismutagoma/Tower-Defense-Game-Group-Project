/**
 * Bloon Tower Defense — art layer.
 *
 * Everything visual on the map is drawn here with the Canvas 2D API:
 * grass, cobblestone path, scenery, towers, balloons and effects.
 * Drawing happens in "cell units" (1 unit = 1 board cell) so the art
 * scales with the board, whatever its size.
 *
 * This file contains no game logic: it only paints what the Java
 * backend (or the offline simulation) reports.
 */
(function () {
    'use strict';

    const TAU = Math.PI * 2;
    const INK = '#2b1a0c';          // cartoon outline colour

    // ---------------------------------------------------------------- utils
    function mulberry32(seed) {
        let a = seed >>> 0;
        return function () {
            a = (a + 0x6D2B79F5) >>> 0;
            let t = a;
            t = Math.imul(t ^ (t >>> 15), t | 1);
            t ^= t + Math.imul(t ^ (t >>> 7), t | 61);
            return ((t ^ (t >>> 14)) >>> 0) / 4294967296;
        };
    }

    function hashString(s) {
        let h = 2166136261;
        for (let i = 0; i < s.length; i++) {
            h ^= s.charCodeAt(i);
            h = Math.imul(h, 16777619);
        }
        return h >>> 0;
    }

    function circle(ctx, x, y, r, fill, stroke, lw) {
        ctx.beginPath();
        ctx.arc(x, y, r, 0, TAU);
        if (fill) { ctx.fillStyle = fill; ctx.fill(); }
        if (stroke) { ctx.lineWidth = lw || 0.035; ctx.strokeStyle = stroke; ctx.stroke(); }
    }

    function ellipse(ctx, x, y, rx, ry, rot, fill, stroke, lw) {
        ctx.beginPath();
        ctx.ellipse(x, y, rx, ry, rot || 0, 0, TAU);
        if (fill) { ctx.fillStyle = fill; ctx.fill(); }
        if (stroke) { ctx.lineWidth = lw || 0.035; ctx.strokeStyle = stroke; ctx.stroke(); }
    }

    function roundRect(ctx, x, y, w, h, r) {
        ctx.beginPath();
        ctx.moveTo(x + r, y);
        ctx.arcTo(x + w, y, x + w, y + h, r);
        ctx.arcTo(x + w, y + h, x, y + h, r);
        ctx.arcTo(x, y + h, x, y, r);
        ctx.arcTo(x, y, x + w, y, r);
        ctx.closePath();
    }

    function hexToRgb(hex) {
        const n = parseInt(hex.slice(1), 16);
        return [(n >> 16) & 255, (n >> 8) & 255, n & 255];
    }

    // ------------------------------------------------------------ palettes
    const BALLOON_STYLES = {
        CumulusBalloon: { base: '#e8392e', light: '#ff9a8a', dark: '#9c1810', size: 0.27, label: 'Cumulus' },
        ZephyrBalloon:  { base: '#2f8be3', light: '#9ad2ff', dark: '#134f91', size: 0.24, label: 'Zephyr' },
        StratusBalloon: { base: '#3fb146', light: '#b4f0a0', dark: '#1d6a22', size: 0.31, label: 'Stratus' },
    };
    const DEFAULT_BALLOON = { base: '#f5a623', light: '#ffe0a0', dark: '#9a5f00', size: 0.27, label: 'Balloon' };

    function balloonStyle(type) { return BALLOON_STYLES[type] || DEFAULT_BALLOON; }

    // ======================================================== BACKGROUND
    /**
     * Pre-renders the static map (grass, path, scenery) into an offscreen canvas.
     * @param {object} opts {rows, cols, path:[[r,c]...], grid, px (pixels per cell)}
     */
    function buildBackground(opts) {
        const { rows, cols, path, grid, px } = opts;
        const W = Math.round(cols * px), H = Math.round(rows * px);
        const canvas = document.createElement('canvas');
        canvas.width = W; canvas.height = H;
        const ctx = canvas.getContext('2d');
        const rng = mulberry32(hashString(JSON.stringify(path) + rows + 'x' + cols));

        const isPath = (r, c) => r >= 0 && c >= 0 && r < rows && c < cols && grid[r][c] === 1;
        // distance (in cells) from a point to the nearest path cell square
        const distToPath = (x, y) => {
            let best = 99;
            const cr = Math.floor(y), cc = Math.floor(x);
            for (let r = cr - 2; r <= cr + 2; r++) {
                for (let c = cc - 2; c <= cc + 2; c++) {
                    if (!isPath(r, c)) continue;
                    const dx = Math.max(c - x, 0, x - (c + 1));
                    const dy = Math.max(r - y, 0, y - (r + 1));
                    best = Math.min(best, Math.hypot(dx, dy));
                }
            }
            return best;
        };

        ctx.setTransform(px, 0, 0, px, 0, 0);

        // ---- grass base
        const g0 = ctx.createLinearGradient(0, 0, cols, rows);
        g0.addColorStop(0, '#6cc136');
        g0.addColorStop(1, '#55aa2b');
        ctx.fillStyle = g0;
        ctx.fillRect(0, 0, cols, rows);

        // soft light / dark blotches
        for (let i = 0; i < rows * cols * 0.18; i++) {
            const x = rng() * cols, y = rng() * rows, r = 0.8 + rng() * 2.4;
            const light = rng() < 0.55;
            const gr = ctx.createRadialGradient(x, y, 0, x, y, r);
            gr.addColorStop(0, light ? 'rgba(150,215,80,0.22)' : 'rgba(60,130,30,0.20)');
            gr.addColorStop(1, 'rgba(0,0,0,0)');
            ctx.fillStyle = gr;
            ctx.fillRect(x - r, y - r, r * 2, r * 2);
        }

        // grass blades
        ctx.lineCap = 'round';
        for (let i = 0; i < rows * cols * 9; i++) {
            const x = rng() * cols, y = rng() * rows;
            const len = 0.04 + rng() * 0.07;
            ctx.strokeStyle = rng() < 0.5 ? 'rgba(40,110,20,0.28)' : 'rgba(190,240,120,0.22)';
            ctx.lineWidth = 0.012 + rng() * 0.01;
            ctx.beginPath();
            ctx.moveTo(x, y);
            ctx.lineTo(x + (rng() - 0.5) * 0.04, y - len);
            ctx.stroke();
        }

        // ---- path geometry (through cell centres, extended off the map edges)
        const pts = path.map(([r, c]) => [c + 0.5, r + 0.5]);
        if (pts.length) {
            const [fr, fc] = path[0];
            const [lr, lc] = path[path.length - 1];
            if (fc === 0) pts.unshift([-1, fr + 0.5]);
            else if (fr === 0) pts.unshift([fc + 0.5, -1]);
            if (lc === cols - 1) pts.push([cols + 1, lr + 0.5]);
            else if (lr === rows - 1) pts.push([lc + 0.5, rows + 1]);
        }
        const strokePath = (c2, width, style) => {
            c2.beginPath();
            pts.forEach(([x, y], i) => (i ? c2.lineTo(x, y) : c2.moveTo(x, y)));
            c2.lineJoin = 'round';
            c2.lineCap = 'round';
            c2.lineWidth = width;
            c2.strokeStyle = style;
            c2.stroke();
        };

        // dark grass halo around the path
        ctx.save();
        ctx.filter = `blur(${Math.max(1, px * 0.08)}px)`;
        strokePath(ctx, 1.08, 'rgba(30,80,10,0.35)');
        ctx.restore();
        // drop shadow
        ctx.save();
        ctx.translate(0.04, 0.07);
        strokePath(ctx, 0.92, 'rgba(0,0,0,0.25)');
        ctx.restore();
        strokePath(ctx, 0.9, '#6e5226');   // outline
        strokePath(ctx, 0.84, '#d9b26c');  // sandy rim
        strokePath(ctx, 0.78, '#c99b55');  // mortar bed

        // ---- cobblestones (Voronoi cells, computed per pixel inside the path)
        const mask = document.createElement('canvas');
        mask.width = W; mask.height = H;
        const mctx = mask.getContext('2d');
        mctx.setTransform(px, 0, 0, px, 0, 0);
        strokePath(mctx, 0.76, '#000');
        const stones = paintCobbles(mctx.getImageData(0, 0, W, H), W, H, px, rng);
        mctx.setTransform(1, 0, 0, 1, 0, 0);
        mctx.putImageData(stones, 0, 0);
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        ctx.drawImage(mask, 0, 0);
        ctx.setTransform(px, 0, 0, px, 0, 0);

        // ---- scenery
        // rocks
        for (let i = 0; i < rows * cols * 0.06; i++) {
            const x = 0.2 + rng() * (cols - 0.4), y = 0.2 + rng() * (rows - 0.4);
            if (distToPath(x, y) < 0.12) continue;
            drawRock(ctx, x, y, 0.06 + rng() * 0.07, rng);
        }
        // flowers
        const flowerColors = ['#4fd8ee', '#ff7eb9', '#ffe45c', '#ffffff', '#ffa94d', '#c084fc'];
        for (let i = 0; i < rows * cols * 0.28; i++) {
            const x = 0.15 + rng() * (cols - 0.3), y = 0.15 + rng() * (rows - 0.3);
            if (distToPath(x, y) < 0.1) continue;
            drawFlower(ctx, x, y, 0.065 + rng() * 0.035, flowerColors[Math.floor(rng() * flowerColors.length)], rng() * TAU);
        }
        // bushes poking in from the map edges
        const edgeSpots = [];
        for (let i = 0; i < 26 && edgeSpots.length < 7; i++) {
            const side = Math.floor(rng() * 4);
            let x, y;
            if (side === 0) { x = -0.25; y = 1 + rng() * (rows - 2); }
            else if (side === 1) { x = cols + 0.25; y = 1 + rng() * (rows - 2); }
            else if (side === 2) { x = 1 + rng() * (cols - 2); y = -0.3; }
            else { x = 1 + rng() * (cols - 2); y = rows + 0.3; }
            const cx = Math.min(cols - 0.5, Math.max(0.5, x)), cy = Math.min(rows - 0.5, Math.max(0.5, y));
            if (distToPath(cx, cy) < 1.3) continue;
            if (edgeSpots.some(([ex, ey]) => Math.hypot(ex - x, ey - y) < 3)) continue;
            edgeSpots.push([x, y]);
        }
        edgeSpots.forEach(([x, y]) => drawBush(ctx, x, y, 0.85 + rng() * 0.45, rng));

        // vignette
        ctx.setTransform(1, 0, 0, 1, 0, 0);
        const vg = ctx.createRadialGradient(W / 2, H / 2, Math.min(W, H) * 0.45, W / 2, H / 2, Math.max(W, H) * 0.75);
        vg.addColorStop(0, 'rgba(0,0,0,0)');
        vg.addColorStop(1, 'rgba(0,30,0,0.28)');
        ctx.fillStyle = vg;
        ctx.fillRect(0, 0, W, H);

        return canvas;
    }

    function paintCobbles(maskData, W, H, px, rng) {
        const g = Math.max(4, px * 0.23);                    // stone size in pixels
        const gw = Math.ceil(W / g) + 3, gh = Math.ceil(H / g) + 3;
        const sx = new Float32Array(gw * gh), sy = new Float32Array(gw * gh);
        const sc = new Float32Array(gw * gh * 3);
        const palette = ['#f6e3b4', '#f1d49b', '#f8e9c6', '#efc98d', '#f3dba8', '#ead0a0'].map(hexToRgb);
        for (let j = 0; j < gh; j++) {
            for (let i = 0; i < gw; i++) {
                const k = j * gw + i;
                sx[k] = (i - 1 + 0.15 + rng() * 0.7) * g;
                sy[k] = (j - 1 + 0.15 + rng() * 0.7) * g;
                const p = palette[Math.floor(rng() * palette.length)];
                const v = 0.94 + rng() * 0.1;
                sc[k * 3] = p[0] * v; sc[k * 3 + 1] = p[1] * v; sc[k * 3 + 2] = p[2] * v;
            }
        }
        const src = maskData.data;
        const out = new ImageData(W, H);
        const dst = out.data;
        const mortar = g * 0.075, bevel = g * 0.16;
        for (let y = 0; y < H; y++) {
            const cy = Math.floor(y / g) + 1;
            for (let x = 0; x < W; x++) {
                const p = (y * W + x) * 4;
                const a = src[p + 3];
                if (a < 4) continue;
                const cx = Math.floor(x / g) + 1;
                let d1 = 1e9, d2 = 1e9, best = 0;
                for (let oy = -1; oy <= 1; oy++) {
                    for (let ox = -1; ox <= 1; ox++) {
                        const k = (cy + oy) * gw + (cx + ox);
                        const dx = sx[k] - x, dy = sy[k] - y;
                        const d = dx * dx + dy * dy;
                        if (d < d1) { d2 = d1; d1 = d; best = k; }
                        else if (d < d2) { d2 = d; }
                    }
                }
                const edge = (Math.sqrt(d2) - Math.sqrt(d1)) / 2;
                let r, gg, b;
                if (edge < mortar) {
                    r = 196; gg = 150; b = 88;
                } else {
                    const t = Math.min(1, (edge - mortar) / bevel);
                    // light from the top-left: brighten towards the stone centre
                    const dx = x - sx[best], dy = y - sy[best];
                    const tilt = 1 + Math.max(-0.08, Math.min(0.08, -(dx + dy) / (g * 6)));
                    const shade = (0.8 + 0.2 * t) * tilt;
                    r = sc[best * 3] * shade; gg = sc[best * 3 + 1] * shade; b = sc[best * 3 + 2] * shade;
                }
                dst[p] = r; dst[p + 1] = gg; dst[p + 2] = b; dst[p + 3] = a;
            }
        }
        return out;
    }

    function drawRock(ctx, x, y, r, rng) {
        const n = 6 + Math.floor(rng() * 3);
        const pts = [];
        for (let i = 0; i < n; i++) {
            const a = (i / n) * TAU + rng() * 0.4;
            const rr = r * (0.75 + rng() * 0.35);
            pts.push([x + Math.cos(a) * rr, y + Math.sin(a) * rr * 0.8]);
        }
        ctx.beginPath();
        pts.forEach(([px, py], i) => (i ? ctx.lineTo(px, py + 0.025) : ctx.moveTo(px, py + 0.025)));
        ctx.closePath();
        ctx.fillStyle = 'rgba(0,0,0,0.22)';
        ctx.fill();
        ctx.beginPath();
        pts.forEach(([px, py], i) => (i ? ctx.lineTo(px, py) : ctx.moveTo(px, py)));
        ctx.closePath();
        const gr = ctx.createLinearGradient(x - r, y - r, x + r, y + r);
        gr.addColorStop(0, '#d7dade');
        gr.addColorStop(1, '#8f959b');
        ctx.fillStyle = gr;
        ctx.fill();
        ctx.lineWidth = 0.014;
        ctx.strokeStyle = '#5d6369';
        ctx.stroke();
    }

    function drawFlower(ctx, x, y, r, color, rot) {
        for (let i = 0; i < 5; i++) {
            const a = rot + (i / 5) * TAU;
            circle(ctx, x + Math.cos(a) * r * 0.7, y + Math.sin(a) * r * 0.7, r * 0.55, color);
        }
        circle(ctx, x, y, r * 0.38, color === '#ffe45c' ? '#ff9f1c' : '#ffd23f');
    }

    function drawBush(ctx, x, y, size, rng) {
        const blobs = [];
        const n = 5 + Math.floor(rng() * 3);
        for (let i = 0; i < n; i++) {
            const a = rng() * TAU, d = rng() * size * 0.55;
            blobs.push([x + Math.cos(a) * d, y + Math.sin(a) * d, size * (0.38 + rng() * 0.3)]);
        }
        blobs.forEach(([bx, by, br]) => circle(ctx, bx + 0.08, by + 0.12, br, 'rgba(0,40,0,0.35)'));
        blobs.forEach(([bx, by, br]) => circle(ctx, bx, by, br, '#2d7a1f', '#1b4d12', 0.03));
        blobs.forEach(([bx, by, br]) => {
            const gr = ctx.createRadialGradient(bx - br * 0.35, by - br * 0.4, 0, bx, by, br);
            gr.addColorStop(0, 'rgba(140,220,90,0.75)');
            gr.addColorStop(0.6, 'rgba(70,160,45,0.25)');
            gr.addColorStop(1, 'rgba(0,0,0,0)');
            circle(ctx, bx, by, br * 0.96, gr);
        });
    }

    // ============================================================ TOWERS
    // Each sprite is drawn centred on (0,0), facing +x, roughly 0.8 cells wide.

    function drawLimbStroke(ctx, x1, y1, x2, y2, w, fill) {
        ctx.lineCap = 'round';
        ctx.beginPath(); ctx.moveTo(x1, y1); ctx.lineTo(x2, y2);
        ctx.lineWidth = w + 0.05; ctx.strokeStyle = INK; ctx.stroke();
        ctx.lineWidth = w; ctx.strokeStyle = fill; ctx.stroke();
    }

    function drawMonkeyBody(ctx, fur, face, opts = {}) {
        const s = opts.scale || 1;
        ctx.save();
        ctx.scale(s, s);
        // tail
        ctx.lineCap = 'round';
        ctx.beginPath();
        ctx.moveTo(-0.24, 0.06);
        ctx.bezierCurveTo(-0.45, 0.12, -0.46, 0.34, -0.3, 0.33);
        ctx.lineWidth = 0.09; ctx.strokeStyle = INK; ctx.stroke();
        ctx.lineWidth = 0.05; ctx.strokeStyle = fur; ctx.stroke();
        // arms (behind the body)
        const handL = opts.handL || [0.2, -0.24];
        const handR = opts.handR || [0.26, 0.16];
        drawLimbStroke(ctx, -0.02, -0.18, handL[0], handL[1], 0.085, fur);
        drawLimbStroke(ctx, -0.02, 0.18, handR[0], handR[1], 0.085, fur);
        // body
        ellipse(ctx, -0.07, 0, 0.24, 0.26, 0, fur, INK, 0.04);
        ellipse(ctx, -0.04, 0, 0.13, 0.15, 0, 'rgba(255,255,255,0.12)');
        // hands
        circle(ctx, handL[0], handL[1], 0.06, face, INK, 0.03);
        circle(ctx, handR[0], handR[1], 0.06, face, INK, 0.03);
        // ears
        circle(ctx, 0.02, -0.17, 0.07, face, INK, 0.03);
        circle(ctx, 0.02, 0.17, 0.07, face, INK, 0.03);
        // head
        circle(ctx, 0.05, 0, 0.17, fur, INK, 0.04);
        ellipse(ctx, 0.13, 0, 0.085, 0.12, 0, face);
        // eyes + brow
        circle(ctx, 0.16, -0.045, 0.024, '#fff');
        circle(ctx, 0.16, 0.045, 0.024, '#fff');
        circle(ctx, 0.17, -0.045, 0.013, INK);
        circle(ctx, 0.17, 0.045, 0.013, INK);
        // shine on the head
        ellipse(ctx, 0.0, -0.06, 0.06, 0.035, -0.6, 'rgba(255,255,255,0.25)');
        ctx.restore();
    }

    function drawDart(ctx, x, y, len, fletch) {
        ctx.save();
        ctx.translate(x, y);
        ctx.lineCap = 'round';
        ctx.beginPath(); ctx.moveTo(-len * 0.5, 0); ctx.lineTo(len * 0.5, 0);
        ctx.lineWidth = 0.035; ctx.strokeStyle = '#3a3a3a'; ctx.stroke();
        ctx.beginPath(); ctx.moveTo(len * 0.5 + 0.06, 0); ctx.lineTo(len * 0.5 - 0.02, -0.025); ctx.lineTo(len * 0.5 - 0.02, 0.025); ctx.closePath();
        ctx.fillStyle = '#cfd6dd'; ctx.fill();
        ctx.beginPath(); ctx.moveTo(-len * 0.5, 0); ctx.lineTo(-len * 0.5 - 0.07, -0.05); ctx.lineTo(-len * 0.5 + 0.03, 0); ctx.lineTo(-len * 0.5 - 0.07, 0.05); ctx.closePath();
        ctx.fillStyle = fletch; ctx.fill();
        ctx.restore();
    }

    const TOWER_ART = {
        monkey(ctx, t) {
            drawMonkeyBody(ctx, '#8b5a2b', '#efc794', { handR: [0.25 + t.recoil * 0.06, 0.15] });
            drawDart(ctx, 0.33 + t.recoil * 0.06, 0.15, 0.2, '#e8392e');
            // red headband
            ctx.beginPath(); ctx.arc(0.05, 0, 0.17, Math.PI * 0.62, Math.PI * 1.38);
            ctx.lineWidth = 0.05; ctx.strokeStyle = '#e23b2e'; ctx.stroke();
        },
        gorilla(ctx, t) {
            const k = 1.18;
            ctx.save(); ctx.scale(k, k);
            // big shoulders
            ellipse(ctx, -0.08, 0, 0.2, 0.3, 0, '#3c3c46', INK, 0.04);
            const push = t.recoil * 0.07;
            drawLimbStroke(ctx, -0.02, -0.22, 0.22 + push, -0.2, 0.11, '#3c3c46');
            drawLimbStroke(ctx, -0.02, 0.22, 0.22 + push, 0.2, 0.11, '#3c3c46');
            circle(ctx, 0.25 + push, -0.2, 0.085, '#6f7282', INK, 0.035);
            circle(ctx, 0.25 + push, 0.2, 0.085, '#6f7282', INK, 0.035);
            circle(ctx, 0.04, 0, 0.16, '#3c3c46', INK, 0.04);
            ellipse(ctx, 0.12, 0, 0.08, 0.11, 0, '#767a8c');
            ctx.beginPath(); ctx.moveTo(0.07, -0.08); ctx.lineTo(0.1, 0); ctx.lineTo(0.07, 0.08);
            ctx.lineWidth = 0.03; ctx.strokeStyle = '#1f1f26'; ctx.stroke();
            circle(ctx, 0.15, -0.04, 0.02, '#ffcf3f'); circle(ctx, 0.15, 0.04, 0.02, '#ffcf3f');
            ellipse(ctx, -0.02, -0.07, 0.06, 0.03, -0.6, 'rgba(255,255,255,0.2)');
            ctx.restore();
        },
        sniper(ctx, t) {
            const back = -t.recoil * 0.06;
            ctx.save(); ctx.translate(back, 0);
            // rifle under the arms
            ctx.fillStyle = '#5b3a1e';
            roundRect(ctx, -0.02, 0.05, 0.2, 0.07, 0.02); ctx.fill();
            ctx.fillStyle = '#2b2b30';
            roundRect(ctx, 0.12, 0.065, 0.48, 0.04, 0.015); ctx.fill();
            ctx.fillStyle = '#1b1b20';
            roundRect(ctx, 0.12, 0.035, 0.12, 0.03, 0.01); ctx.fill();
            drawMonkeyBody(ctx, '#7a4e24', '#e9bf8a', { handL: [0.18, 0.02], handR: [0.32, 0.09] });
            // camo helmet
            circle(ctx, 0.04, 0, 0.175, '#5b7a2e', INK, 0.04);
            circle(ctx, 0.0, -0.06, 0.045, '#3f5a1d');
            circle(ctx, 0.08, 0.07, 0.04, '#3f5a1d');
            circle(ctx, -0.05, 0.06, 0.035, '#7f9a46');
            ctx.restore();
        },
        needle(ctx, t) {
            ctx.save();
            ctx.rotate(t.spin || 0);
            const n = 8;
            for (let i = 0; i < n; i++) {
                const a = (i / n) * TAU;
                ctx.save(); ctx.rotate(a);
                ctx.beginPath(); ctx.moveTo(0.22, -0.045); ctx.lineTo(0.43 + t.recoil * 0.04, 0); ctx.lineTo(0.22, 0.045); ctx.closePath();
                ctx.fillStyle = '#e7edf3'; ctx.fill();
                ctx.lineWidth = 0.022; ctx.strokeStyle = INK; ctx.stroke();
                ctx.restore();
            }
            const g = ctx.createRadialGradient(-0.08, -0.08, 0.02, 0, 0, 0.3);
            g.addColorStop(0, '#d5dce4'); g.addColorStop(1, '#6d7884');
            circle(ctx, 0, 0, 0.27, g, INK, 0.04);
            for (let i = 0; i < 8; i++) {
                const a = (i / 8) * TAU + 0.39;
                circle(ctx, Math.cos(a) * 0.2, Math.sin(a) * 0.2, 0.022, '#4c555e');
            }
            const g2 = ctx.createRadialGradient(-0.04, -0.05, 0.01, 0, 0, 0.15);
            g2.addColorStop(0, '#7ff0e2'); g2.addColorStop(1, '#0f8f86');
            circle(ctx, 0, 0, 0.13, g2, INK, 0.035);
            ellipse(ctx, -0.04, -0.05, 0.045, 0.025, -0.7, 'rgba(255,255,255,0.7)');
            ctx.restore();
        },
        bomb(ctx, t) {
            // wooden turntable
            const g = ctx.createRadialGradient(-0.1, -0.1, 0.02, 0, 0, 0.36);
            g.addColorStop(0, '#b77a3e'); g.addColorStop(1, '#7a4a1e');
            circle(ctx, 0, 0, 0.34, g, INK, 0.04);
            ctx.save();
            ctx.beginPath(); ctx.arc(0, 0, 0.32, 0, TAU); ctx.clip();
            ctx.strokeStyle = 'rgba(60,30,10,0.45)'; ctx.lineWidth = 0.02;
            for (let i = -3; i <= 3; i++) { ctx.beginPath(); ctx.moveTo(-0.4, i * 0.11); ctx.lineTo(0.4, i * 0.11); ctx.stroke(); }
            ctx.restore();
            for (let i = 0; i < 6; i++) {
                const a = (i / 6) * TAU;
                circle(ctx, Math.cos(a) * 0.27, Math.sin(a) * 0.27, 0.022, '#3a3a40');
            }
            // barrel
            const back = -t.recoil * 0.08;
            ctx.save(); ctx.translate(back, 0);
            circle(ctx, -0.12, 0, 0.09, '#2c2c33', INK, 0.035);
            const bg = ctx.createLinearGradient(0, -0.12, 0, 0.12);
            bg.addColorStop(0, '#6a6a75'); bg.addColorStop(0.45, '#33333b'); bg.addColorStop(1, '#1d1d22');
            ctx.fillStyle = bg;
            roundRect(ctx, -0.14, -0.12, 0.56, 0.24, 0.06); ctx.fill();
            ctx.lineWidth = 0.04; ctx.strokeStyle = INK; ctx.stroke();
            ctx.fillStyle = '#26262c';
            roundRect(ctx, 0.36, -0.145, 0.1, 0.29, 0.04); ctx.fill(); ctx.stroke();
            ctx.fillStyle = '#e8b33a';
            roundRect(ctx, 0.08, -0.125, 0.05, 0.25, 0.02); ctx.fill();
            ctx.restore();
        },
        slow(ctx, t) {
            stoneBase(ctx);
            const pulse = 1 + Math.sin(t.time * 3) * 0.04;
            const g = ctx.createRadialGradient(-0.06, -0.07, 0.02, 0, 0, 0.24);
            g.addColorStop(0, '#e2a8ff'); g.addColorStop(0.6, '#9b3fd1'); g.addColorStop(1, '#5d1d86');
            circle(ctx, 0, 0, 0.21 * pulse, g, INK, 0.04);
            // swirl
            ctx.beginPath();
            for (let i = 0; i <= 40; i++) {
                const a = i / 40 * TAU * 1.6 + t.time * 1.5;
                const r = 0.02 + i / 40 * 0.15;
                const x = Math.cos(a) * r, y = Math.sin(a) * r;
                i ? ctx.lineTo(x, y) : ctx.moveTo(x, y);
            }
            ctx.lineWidth = 0.025; ctx.strokeStyle = 'rgba(255,255,255,0.8)'; ctx.lineCap = 'round'; ctx.stroke();
            // goo drips
            [[0.17, 0.12], [-0.15, 0.15], [0.02, 0.2]].forEach(([x, y]) => circle(ctx, x, y, 0.04, '#8e3cc4', INK, 0.02));
        },
        freeze(ctx, t) {
            stoneBase(ctx, '#7f98ab', '#4e6576');
            const g = ctx.createRadialGradient(-0.06, -0.07, 0.02, 0, 0, 0.25);
            g.addColorStop(0, '#ffffff'); g.addColorStop(0.5, '#a9e4ff'); g.addColorStop(1, '#3a9bd8');
            circle(ctx, 0, 0, 0.21, g, '#1d5a82', 0.04);
            // snowflake
            ctx.save();
            ctx.rotate(t.time * 0.4);
            ctx.strokeStyle = '#ffffff'; ctx.lineWidth = 0.03; ctx.lineCap = 'round';
            for (let i = 0; i < 6; i++) {
                ctx.save(); ctx.rotate(i / 6 * TAU);
                ctx.beginPath(); ctx.moveTo(0, 0); ctx.lineTo(0.15, 0);
                ctx.moveTo(0.09, 0); ctx.lineTo(0.13, -0.04);
                ctx.moveTo(0.09, 0); ctx.lineTo(0.13, 0.04);
                ctx.stroke(); ctx.restore();
            }
            ctx.restore();
            ellipse(ctx, -0.07, -0.08, 0.05, 0.03, -0.7, 'rgba(255,255,255,0.85)');
        },
    };

    function stoneBase(ctx, light = '#a8977f', dark = '#6d5d48') {
        const g = ctx.createRadialGradient(-0.1, -0.1, 0.02, 0, 0, 0.36);
        g.addColorStop(0, light); g.addColorStop(1, dark);
        ctx.beginPath();
        for (let i = 0; i < 8; i++) {
            const a = (i / 8) * TAU + Math.PI / 8;
            const x = Math.cos(a) * 0.33, y = Math.sin(a) * 0.33;
            i ? ctx.lineTo(x, y) : ctx.moveTo(x, y);
        }
        ctx.closePath();
        ctx.fillStyle = g; ctx.fill();
        ctx.lineWidth = 0.04; ctx.strokeStyle = INK; ctx.stroke();
    }

    /**
     * Draw a tower at (x, y) in cell units.
     * @param t visual state: {angle, recoil (0..1), spin, time, ghost, invalid}
     */
    function drawTower(ctx, kind, x, y, t = {}) {
        const art = TOWER_ART[kind] || TOWER_ART.monkey;
        const st = Object.assign({ angle: 0, recoil: 0, spin: 0, time: 0 }, t);
        ctx.save();
        ctx.translate(x, y);
        if (st.ghost) ctx.globalAlpha = 0.65;
        // soft shadow
        ellipse(ctx, 0.05, 0.09, 0.4, 0.33, 0, 'rgba(0,0,0,0.28)');
        ctx.save();
        if (kind !== 'needle' && kind !== 'slow' && kind !== 'freeze') ctx.rotate(st.angle);
        const pop = (st.scale || 1.14) * (1 + st.recoil * 0.06);
        ctx.scale(pop, pop);
        art(ctx, st);
        ctx.restore();
        ctx.restore();
    }

    // =========================================================== BALLOONS
    function drawBalloon(ctx, b, x, y, time) {
        const st = balloonStyle(b.type);
        const r = st.size;
        const bob = Math.sin(time * 4 + (b.id || 0) * 1.7) * 0.015;
        y += bob;
        ctx.save();
        // shadow on the ground
        ellipse(ctx, x + 0.06, y + r + 0.12 - bob, r * 0.7, r * 0.28, 0, 'rgba(0,0,0,0.22)');
        // string
        ctx.beginPath();
        ctx.moveTo(x, y + r * 1.12);
        ctx.quadraticCurveTo(x + 0.05, y + r * 1.35, x - 0.01, y + r * 1.6);
        ctx.lineWidth = 0.015; ctx.strokeStyle = 'rgba(40,25,10,0.7)'; ctx.stroke();
        // knot
        ctx.beginPath();
        ctx.moveTo(x, y + r * 0.96);
        ctx.lineTo(x - r * 0.17, y + r * 1.17);
        ctx.lineTo(x + r * 0.17, y + r * 1.17);
        ctx.closePath();
        ctx.fillStyle = st.dark; ctx.fill();
        // body
        const g = ctx.createRadialGradient(x - r * 0.35, y - r * 0.4, r * 0.1, x, y, r * 1.1);
        g.addColorStop(0, st.light);
        g.addColorStop(0.45, st.base);
        g.addColorStop(1, st.dark);
        ellipse(ctx, x, y, r * 0.86, r, 0, g, 'rgba(30,10,5,0.75)', 0.03);
        // highlight
        ellipse(ctx, x - r * 0.33, y - r * 0.42, r * 0.17, r * 0.29, 0.5, 'rgba(255,255,255,0.75)');
        circle(ctx, x - r * 0.12, y - r * 0.72, r * 0.07, 'rgba(255,255,255,0.6)');

        // status effects
        if (b.effect === 'FREEZE') {
            ctx.save();
            ctx.globalAlpha = 0.8;
            const ig = ctx.createLinearGradient(x - r, y - r, x + r, y + r);
            ig.addColorStop(0, 'rgba(230,250,255,0.85)');
            ig.addColorStop(1, 'rgba(120,200,240,0.6)');
            ctx.fillStyle = ig;
            roundRect(ctx, x - r * 1.15, y - r * 1.2, r * 2.3, r * 2.5, r * 0.35);
            ctx.fill();
            ctx.lineWidth = 0.025; ctx.strokeStyle = '#ffffff'; ctx.stroke();
            ctx.beginPath();
            ctx.moveTo(x - r * 0.8, y - r * 0.6); ctx.lineTo(x - r * 0.3, y - r * 0.95);
            ctx.moveTo(x + r * 0.4, y + r * 0.8); ctx.lineTo(x + r * 0.85, y + r * 0.4);
            ctx.lineWidth = 0.02; ctx.stroke();
            ctx.restore();
        } else if (b.effect === 'SLOWED') {
            ellipse(ctx, x, y - r * 0.55, r * 0.75, r * 0.38, 0, 'rgba(155,63,209,0.88)', 'rgba(70,15,100,0.8)', 0.02);
            circle(ctx, x - r * 0.45, y - r * 0.2, r * 0.13, 'rgba(155,63,209,0.88)');
            circle(ctx, x + r * 0.35, y - r * 0.12, r * 0.1, 'rgba(155,63,209,0.88)');
            ellipse(ctx, x - r * 0.2, y - r * 0.7, r * 0.18, r * 0.07, 0, 'rgba(255,255,255,0.5)');
        }

        // health bar (only when damaged)
        if (b.maxLife > 0 && b.life < b.maxLife) {
            const pct = Math.max(0, b.life / b.maxLife);
            const w = r * 1.8, h = 0.06;
            ctx.fillStyle = 'rgba(0,0,0,0.6)';
            roundRect(ctx, x - w / 2, y - r - 0.16, w, h, 0.03); ctx.fill();
            ctx.fillStyle = pct > 0.5 ? '#5ee05a' : pct > 0.25 ? '#ffc933' : '#ff4d3d';
            roundRect(ctx, x - w / 2 + 0.01, y - r - 0.15, (w - 0.02) * pct, h - 0.02, 0.02); ctx.fill();
        }
        ctx.restore();
    }

    // ============================================================ EFFECTS
    function drawProjectile(ctx, p, k) {
        const x = p.x0 + (p.x1 - p.x0) * k;
        const y = p.y0 + (p.y1 - p.y0) * k;
        const ang = Math.atan2(p.y1 - p.y0, p.x1 - p.x0);
        ctx.save();
        if (p.kind === 'bomb') {
            const lift = Math.sin(k * Math.PI) * 0.35;
            circle(ctx, x + 0.04, y + 0.06, 0.08, 'rgba(0,0,0,0.25)');
            circle(ctx, x, y - lift, 0.1, '#25252b', INK, 0.025);
            circle(ctx, x - 0.03, y - lift - 0.03, 0.03, 'rgba(255,255,255,0.45)');
            circle(ctx, x + 0.06, y - lift - 0.09, 0.03, '#ffcf3f');
        } else if (p.kind === 'sniper') {
            ctx.globalAlpha = 1 - k;
            ctx.beginPath(); ctx.moveTo(p.x0, p.y0); ctx.lineTo(p.x1, p.y1);
            ctx.lineWidth = 0.05; ctx.strokeStyle = 'rgba(255,230,120,0.9)'; ctx.lineCap = 'round'; ctx.stroke();
            ctx.lineWidth = 0.02; ctx.strokeStyle = '#fff'; ctx.stroke();
        } else if (p.kind === 'needle') {
            ctx.translate(x, y); ctx.rotate(ang);
            ctx.beginPath(); ctx.moveTo(-0.1, 0); ctx.lineTo(0.1, 0);
            ctx.lineWidth = 0.025; ctx.strokeStyle = '#e9eef3'; ctx.stroke();
            ctx.lineWidth = 0.01; ctx.strokeStyle = '#59636d'; ctx.stroke();
        } else {
            ctx.translate(x, y); ctx.rotate(ang);
            drawDart(ctx, 0, 0, p.kind === 'gorilla' ? 0.26 : 0.18, p.kind === 'gorilla' ? '#ff9f1c' : '#e8392e');
        }
        ctx.restore();
    }

    function drawPop(ctx, e, k) {
        const st = balloonStyle(e.type);
        ctx.save();
        ctx.translate(e.x, e.y);
        const r = 0.18 + k * 0.32;
        ctx.globalAlpha = 1 - k;
        ctx.beginPath();
        for (let i = 0; i < 20; i++) {
            const a = (i / 20) * TAU + e.rot;
            const rr = i % 2 ? r * 0.55 : r;
            i ? ctx.lineTo(Math.cos(a) * rr, Math.sin(a) * rr) : ctx.moveTo(Math.cos(a) * rr, Math.sin(a) * rr);
        }
        ctx.closePath();
        ctx.fillStyle = '#fffbe6'; ctx.fill();
        ctx.lineWidth = 0.03; ctx.strokeStyle = '#ffb21f'; ctx.stroke();
        // rubber bits
        for (let i = 0; i < 6; i++) {
            const a = (i / 6) * TAU + e.rot * 2;
            const d = 0.1 + k * 0.45;
            ctx.save();
            ctx.translate(Math.cos(a) * d, Math.sin(a) * d + k * k * 0.2);
            ctx.rotate(a + k * 4);
            ctx.fillStyle = st.base;
            ctx.fillRect(-0.04, -0.02, 0.08, 0.04);
            ctx.restore();
        }
        ctx.restore();
    }

    function drawExplosion(ctx, e, k) {
        ctx.save();
        ctx.translate(e.x, e.y);
        const R = e.radius * (0.35 + k * 0.75);
        ctx.globalAlpha = Math.max(0, 1 - k * 1.1);
        const g = ctx.createRadialGradient(0, 0, 0, 0, 0, R);
        g.addColorStop(0, '#fff6c2');
        g.addColorStop(0.35, '#ffcc33');
        g.addColorStop(0.7, '#ff7a1a');
        g.addColorStop(1, 'rgba(200,60,10,0)');
        circle(ctx, 0, 0, R, g);
        for (let i = 0; i < 7; i++) {
            const a = (i / 7) * TAU + e.rot;
            circle(ctx, Math.cos(a) * R * 0.75, Math.sin(a) * R * 0.75, R * 0.28 * (1 - k * 0.5), 'rgba(90,80,75,0.55)');
        }
        ctx.restore();
    }

    function drawPulse(ctx, e, k) {
        ctx.save();
        const R = 0.3 + (e.radius - 0.3) * k;
        ctx.globalAlpha = (1 - k) * 0.8;
        circle(ctx, e.x, e.y, R, e.fill, e.stroke, 0.06);
        ctx.restore();
    }

    function drawRange(ctx, x, y, radius, ok) {
        ctx.save();
        circle(ctx, x, y, radius, ok ? 'rgba(20,20,20,0.22)' : 'rgba(200,30,20,0.25)');
        ctx.setLineDash([0.12, 0.08]);
        circle(ctx, x, y, radius, null, ok ? 'rgba(255,255,255,0.85)' : 'rgba(255,120,110,0.9)', 0.035);
        ctx.restore();
    }

    /** Draws a small icon of a tower / balloon into a standalone canvas (for the HUD). */
    function iconCanvas(drawFn, cssSize) {
        const dpr = Math.min(window.devicePixelRatio || 1, 3);
        const c = document.createElement('canvas');
        c.width = c.height = Math.round(cssSize * dpr);
        c.style.width = c.style.height = cssSize + 'px';
        const ctx = c.getContext('2d');
        ctx.setTransform(cssSize * dpr, 0, 0, cssSize * dpr, 0, 0);
        drawFn(ctx);
        return c;
    }

    window.TDArt = {
        TAU,
        buildBackground,
        drawTower,
        drawBalloon,
        drawProjectile,
        drawPop,
        drawExplosion,
        drawPulse,
        drawRange,
        balloonStyle,
        iconCanvas,
        roundRect,
    };
})();
