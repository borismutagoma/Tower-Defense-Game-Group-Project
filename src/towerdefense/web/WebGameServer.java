package towerdefense.web;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.*;

import towerdefense.balloons.Balloon;
import towerdefense.boards.*;
import towerdefense.cell.Cell;
import towerdefense.game.*;
import towerdefense.path.Path;
import towerdefense.player.Player;
import towerdefense.towers.Tower;
import towerdefense.towers.towerwitheffect.*;
import towerdefense.towers.towerwithprojectiles.*;
import towerdefense.balloons.generator.EvolvingBalloonGenerator;
import towerdefense.towers.tools.TowerManager;
import towerdefense.towers.tools.generator.TowerGenerator;
import towerdefense.strategy.FirstOutStrategy;
import towerdefense.util.Position;

/**
 * Lightweight HTTP server that exposes the Tower Defense game state as a REST API.
 * Uses com.sun.net.httpserver (built into the JDK, no external dependencies).
 *
 * Endpoints:
 *   GET  /api/state    - Returns full game state as JSON
 *   POST /api/tick     - Advance one tick
 *   POST /api/round    - Start next round (generate balloons)
 *   POST /api/tower    - Place a tower (body: {"type":"...", "row":N, "col":N})
 *   POST /api/sell     - Sell a tower   (body: {"row":N, "col":N})
 *   POST /api/reset    - Reset / new game
 *   GET  /*            - Serves static files from web/ directory
 */
public class WebGameServer {

    private Board board;
    private Player player;
    private TowerManager towerManager;
    private EvolvingBalloonGenerator balloonGenerator;
    private int currentRound;
    private int currentTick;
    private int gameLevel;
    private boolean roundActive;
    private Random random;

    public WebGameServer() {
        resetGame();
    }

    private void resetGame() {
        this.board = new ClassicalBoard(12, 18);
        this.player = new Player(GameConstants.PLAYER_STARTING_LIFE, GameConstants.PLAYER_STARTING_MONEY);
        TowerGenerator towerGenerator = new TowerGenerator(this.board);
        this.towerManager = new TowerManager(towerGenerator, this.player, this.board);
        this.balloonGenerator = new EvolvingBalloonGenerator();
        this.currentRound = 0;
        this.currentTick = 0;
        this.gameLevel = 1;
        this.roundActive = false;
        this.random = new Random();
    }

    private void startRound() {
        this.currentRound += 1;
        int balloonNumber = 15 + this.currentRound + this.random.nextInt(6);
        if (this.currentRound % 3 == 0) {
            this.gameLevel += 1;
        }
        this.balloonGenerator.generateBalloons(balloonNumber, this.board, this.gameLevel);
        this.roundActive = true;
    }

    private void tick() {
        if (!roundActive) return;

        for (Tower t : board.getAllActiveTowers()) {
            t.act(currentTick);
        }
        for (Balloon balloon : board.getActiveBalloons()) {
            balloon.update();
        }
        this.currentTick += 1;

        // Remove inactive balloons
        for (Balloon b : board.getActiveBalloons()) {
            if (b.isDead()) {
                this.player.gainMoney((int) (b.getDefaultLife() * 20));
            }
            if (b.isFinishedPath()) {
                this.player.dealDamage((int) b.getDefaultLife());
            }
        }
        board.getActiveBalloons().removeIf(balloon -> balloon.isDead() || balloon.isFinishedPath());

        if (board.getActiveBalloons().isEmpty()) {
            this.roundActive = false;
        }
    }

    private boolean placeTower(String type, int row, int col) {
        Position pos = new Position(row, col);
        Cell cell = board.getCell(pos);
        if (cell == null || cell.isOnPath()) return false;

        // Check no tower already there
        for (Tower t : board.getAllActiveTowers()) {
            if (t.getPosition().equals(pos)) return false;
        }

        Tower tower = null;

        switch (type.toLowerCase()) {
            case "monkey":
                if (player.getMoney() < GameConstants.MONKEY_TOWER_COST) return false;
                tower = new MonkeyDartTower(pos, null);
                player.spendMoney(GameConstants.MONKEY_TOWER_COST);
                break;
            case "needle":
                if (player.getMoney() < GameConstants.NEEDLE_TOWER_COST) return false;
                tower = new NeedleTower(pos, null);
                player.spendMoney(GameConstants.NEEDLE_TOWER_COST);
                break;
            case "sniper":
                if (player.getMoney() < GameConstants.SNIPER_TOWER_COST) return false;
                tower = new SniperMonkeyTower(pos, null);
                player.spendMoney(GameConstants.SNIPER_TOWER_COST);
                break;
            case "bomb":
                if (player.getMoney() < GameConstants.BOMB_TOWER_COST) return false;
                tower = new BombTower(pos, new FirstOutStrategy(null, board));
                player.spendMoney(GameConstants.BOMB_TOWER_COST);
                break;
            case "gorilla":
                if (player.getMoney() < GameConstants.GORILLA_TOWER_COST) return false;
                tower = new GorrillaTower(pos, null);
                player.spendMoney(GameConstants.GORILLA_TOWER_COST);
                break;
            case "slow":
                if (player.getMoney() < GameConstants.SLOW_TOWER_COST) return false;
                tower = new SlowTower(pos, null);
                player.spendMoney(GameConstants.SLOW_TOWER_COST);
                break;
            case "freeze":
                if (player.getMoney() < GameConstants.FREEZE_TOWER_COST) return false;
                tower = new FreezeTower(pos, null);
                player.spendMoney(GameConstants.FREEZE_TOWER_COST);
                break;
            default:
                return false;
        }

        tower.setStrategy(new FirstOutStrategy(tower, board));
        board.addTower(tower);
        return true;
    }

    private boolean sellTower(int row, int col) {
        Position pos = new Position(row, col);
        for (Tower t : board.getAllActiveTowers()) {
            if (t.getPosition().equals(pos)) {
                int refund = t.sell(board);
                player.gainMoney(refund);
                return true;
            }
        }
        return false;
    }

    /** Build full game state as JSON string */
    private String getStateJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        // Player info
        sb.append("\"player\":{");
        sb.append("\"life\":").append(player.getLife()).append(",");
        sb.append("\"money\":").append(player.getMoney());
        sb.append("},");

        // Game info
        sb.append("\"round\":").append(currentRound).append(",");
        sb.append("\"tick\":").append(currentTick).append(",");
        sb.append("\"gameLevel\":").append(gameLevel).append(",");
        sb.append("\"roundActive\":").append(roundActive).append(",");
        sb.append("\"gameOver\":").append(player.getLife() <= 0).append(",");

        // Board dimensions
        sb.append("\"board\":{");
        sb.append("\"rows\":").append(board.getRows()).append(",");
        sb.append("\"cols\":").append(board.getColumns());
        sb.append("},");

        // Grid (path cells)
        sb.append("\"grid\":[");
        for (int i = 0; i < board.getRows(); i++) {
            sb.append("[");
            for (int j = 0; j < board.getColumns(); j++) {
                sb.append(board.getGrid()[i][j].isOnPath() ? 1 : 0);
                if (j < board.getColumns() - 1) sb.append(",");
            }
            sb.append("]");
            if (i < board.getRows() - 1) sb.append(",");
        }
        sb.append("],");

        // Path cells (ordered)
        sb.append("\"path\":[");
        Path path = board.getPathOfIndex(0);
        if (path != null && path.getCells() != null) {
            List<Cell> cells = path.getCells();
            for (int i = 0; i < cells.size(); i++) {
                Cell c = cells.get(i);
                sb.append("[").append((int) c.getPosition().getX()).append(",").append((int) c.getPosition().getY()).append("]");
                if (i < cells.size() - 1) sb.append(",");
            }
        }
        sb.append("],");

        // Towers
        sb.append("\"towers\":[");
        List<Tower> towers = board.getAllActiveTowers();
        for (int i = 0; i < towers.size(); i++) {
            Tower t = towers.get(i);
            sb.append("{");
            sb.append("\"type\":\"").append(t.getClass().getSimpleName()).append("\",");
            sb.append("\"row\":").append((int) t.getPosition().getX()).append(",");
            sb.append("\"col\":").append((int) t.getPosition().getY()).append(",");
            sb.append("\"range\":").append(t.getRange()).append(",");
            sb.append("\"cadence\":").append(t.getCadence()).append(",");
            sb.append("\"cost\":").append(t.getCost()).append(",");
            sb.append("\"symbol\":\"").append(escapeJson(t.getSymbol())).append("\"");
            sb.append("}");
            if (i < towers.size() - 1) sb.append(",");
        }
        sb.append("],");

        // Balloons
        sb.append("\"balloons\":[");
        List<Balloon> balloons = board.getActiveBalloons();
        for (int i = 0; i < balloons.size(); i++) {
            Balloon b = balloons.get(i);
            sb.append("{");
            sb.append("\"type\":\"").append(b.getClass().getSimpleName()).append("\",");
            sb.append("\"row\":").append(b.getPosition().getX()).append(",");
            sb.append("\"col\":").append(b.getPosition().getY()).append(",");
            sb.append("\"life\":").append(b.getLife()).append(",");
            sb.append("\"maxLife\":").append(b.getDefaultLife()).append(",");
            sb.append("\"speed\":").append(b.getSpeed()).append(",");
            sb.append("\"effect\":\"").append(b.getEffect().name()).append("\",");
            sb.append("\"dead\":").append(b.isDead()).append(",");
            sb.append("\"finished\":").append(b.isFinishedPath());
            sb.append("}");
            if (i < balloons.size() - 1) sb.append(",");
        }
        sb.append("],");

        // Available tower types for shop
        sb.append("\"shop\":[");
        sb.append("{\"id\":\"monkey\",\"name\":\"Monkey Dart\",\"cost\":").append(GameConstants.MONKEY_TOWER_COST).append(",\"emoji\":\"🐒\",\"range\":").append(GameConstants.MONKEY_TOWER_RANGE).append(",\"cadence\":").append(GameConstants.MONKEY_TOWER_CADENCE).append(",\"damage\":").append(GameConstants.DART_DAMAGE).append("},");
        sb.append("{\"id\":\"needle\",\"name\":\"Needle\",\"cost\":").append(GameConstants.NEEDLE_TOWER_COST).append(",\"emoji\":\"🪡\",\"range\":").append(GameConstants.NEEDLE_TOWER_RANGE).append(",\"cadence\":").append(GameConstants.NEEDLE_TOWER_CADENCE).append(",\"damage\":").append(GameConstants.NEEDLE_DAMAGE).append("},");
        sb.append("{\"id\":\"gorilla\",\"name\":\"Gorilla\",\"cost\":").append(GameConstants.GORILLA_TOWER_COST).append(",\"emoji\":\"🦍\",\"range\":").append(GameConstants.GORILLA_TOWER_RANGE).append(",\"cadence\":").append(GameConstants.GORILLA_TOWER_CADENCE).append(",\"damage\":").append(GameConstants.SHARP_DART_DAMAGE).append("},");
        sb.append("{\"id\":\"sniper\",\"name\":\"Sniper Monkey\",\"cost\":").append(GameConstants.SNIPER_TOWER_COST).append(",\"emoji\":\"🎯\",\"range\":-1,\"cadence\":").append(GameConstants.SNIPER_TOWER_CADENCE).append(",\"damage\":").append(GameConstants.VERY_SHARP_DART_DAMAGE).append("},");
        sb.append("{\"id\":\"bomb\",\"name\":\"Bomb\",\"cost\":").append(GameConstants.BOMB_TOWER_COST).append(",\"emoji\":\"💣\",\"range\":").append(GameConstants.BOMB_TOWER_RANGE).append(",\"cadence\":").append(GameConstants.BOMB_TOWER_CADENCE).append(",\"damage\":").append(GameConstants.BOMB_DAMAGE).append("},");
        sb.append("{\"id\":\"slow\",\"name\":\"Slow\",\"cost\":").append(GameConstants.SLOW_TOWER_COST).append(",\"emoji\":\"🐌\",\"range\":").append(GameConstants.SLOW_TOWER_RANGE).append(",\"cadence\":").append(GameConstants.SLOW_TOWER_CADENCE).append(",\"damage\":0},");
        sb.append("{\"id\":\"freeze\",\"name\":\"Freeze\",\"cost\":").append(GameConstants.FREEZE_TOWER_COST).append(",\"emoji\":\"🧊\",\"range\":").append(GameConstants.FREEZE_TOWER_RANGE).append(",\"cadence\":").append(GameConstants.FREEZE_TOWER_CADENCE).append(",\"damage\":0}");
        sb.append("]");

        sb.append("}");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            return new String(is.readAllBytes());
        }
    }

    private static void sendJson(HttpExchange exchange, int code, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = json.getBytes("UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void sendCors(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        exchange.sendResponseHeaders(204, -1);
    }

    /** Simple JSON string value extractor (avoids external dependency) */
    private static String jsonStringVal(String json, String key) {
        String search = "\"" + key + "\":\"";
        int idx = json.indexOf(search);
        if (idx < 0) return "";
        int start = idx + search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    /** Simple JSON int value extractor */
    private static int jsonIntVal(String json, String key) {
        String search = "\"" + key + "\":";
        int idx = json.indexOf(search);
        if (idx < 0) return -1;
        int start = idx + search.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;
        return Integer.parseInt(json.substring(start, end));
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // API endpoints
        server.createContext("/api/state", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { sendCors(exchange); return; }
            sendJson(exchange, 200, getStateJson());
        });

        server.createContext("/api/tick", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { sendCors(exchange); return; }
            tick();
            sendJson(exchange, 200, getStateJson());
        });

        server.createContext("/api/round", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { sendCors(exchange); return; }
            if (!roundActive && player.getLife() > 0) {
                startRound();
            }
            sendJson(exchange, 200, getStateJson());
        });

        server.createContext("/api/tower", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { sendCors(exchange); return; }
            String body = readBody(exchange);
            String type = jsonStringVal(body, "type");
            int row = jsonIntVal(body, "row");
            int col = jsonIntVal(body, "col");
            boolean ok = placeTower(type, row, col);
            sendJson(exchange, ok ? 200 : 400, getStateJson());
        });

        server.createContext("/api/sell", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { sendCors(exchange); return; }
            String body = readBody(exchange);
            int row = jsonIntVal(body, "row");
            int col = jsonIntVal(body, "col");
            boolean ok = sellTower(row, col);
            sendJson(exchange, ok ? 200 : 400, getStateJson());
        });

        server.createContext("/api/reset", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { sendCors(exchange); return; }
            resetGame();
            sendJson(exchange, 200, getStateJson());
        });

        // Static file serving for web/
        server.createContext("/", exchange -> {
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) { sendCors(exchange); return; }
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";

            // Resolve relative to where the server was started (project root)
            java.nio.file.Path filePath = Paths.get("web" + path);
            if (Files.exists(filePath) && !Files.isDirectory(filePath)) {
                byte[] data = Files.readAllBytes(filePath);
                String contentType = "text/html";
                if (path.endsWith(".css")) contentType = "text/css";
                else if (path.endsWith(".js")) contentType = "application/javascript";
                else if (path.endsWith(".json")) contentType = "application/json";
                else if (path.endsWith(".png")) contentType = "image/png";
                else if (path.endsWith(".svg")) contentType = "image/svg+xml";

                exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=utf-8");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, data.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(data);
                }
            } else {
                String msg = "404 Not Found";
                exchange.sendResponseHeaders(404, msg.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(msg.getBytes());
                }
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("==============================================");
        System.out.println(" Bloon Tower Defense - Web UI");
        System.out.println(" Server running at http://localhost:" + port);
        System.out.println("==============================================");
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException e) { /* default */ }
        }
        new WebGameServer().start(port);
    }
}
