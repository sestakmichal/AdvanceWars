package ija.ija2025.project.model.Game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ija.ija2025.project.controller.CommandManager;
import ija.ija2025.project.model.Buildings.Building;
import ija.ija2025.project.model.Commands.Command;
import ija.ija2025.project.model.Map.TerrainType;
import ija.ija2025.project.model.Units.Unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.util.List;
import java.util.Random;

public class GameFactory {
    public static GameSave loadGameSave(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(file);

            Game game = new Game();
            loadGameState(game, root);
            loadMap(game, root.get("map"));
            loadBuildings(game, root.get("buildings"));
            loadUnits(game, root.get("units"));

            List<Command> history = null;
            if (root.has("history") && !root.get("history").isNull() && root.get("history").isArray()) {
                history = mapper.readValue(
                    root.get("history").traverse(),
                    new TypeReference<List<Command>>() {}
                );
            }

            return new GameSave(game, history);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load save file", e);
        }
    }

    public static Game createGameFromJson(String filePath) {
        Game game = new Game();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(new File(filePath));

            loadGameState(game, root);
            loadMap(game, root.get("map"));
            loadBuildings(game, root.get("buildings"));
            loadUnits(game, root.get("units"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to load game JSON", e);
        }

        return game;
    }

    private static void loadGameState(Game game, JsonNode root) {
        if (root.has("currentPlayer")) {
            game.setCurrentPlayer(root.get("currentPlayer").asText());
        }

        if (root.has("playerMoney")) {
            root.get("playerMoney").fields().forEachRemaining(e ->
                    game.setPlayerMoney(e.getKey(), e.getValue().asInt())
            );
        }
    }

    private static void loadMap(Game game, JsonNode mapNode) {
        int rows = mapNode.size();
        int cols = mapNode.get(0).size();

        TerrainType[][] terrainMap = new TerrainType[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                terrainMap[r][c] =
                        TerrainType.fromString(mapNode.get(r).get(c).asText());
            }
        }

        game.updateMap(terrainMap);
    }

    private static void loadBuildings(Game game, JsonNode buildings) {
        if (buildings == null) return;

        for (JsonNode b : buildings) {

            int row = b.get("row").asInt();
            int col = b.get("col").asInt();
            TerrainType type = TerrainType.fromString(b.get("type").asText());

            String owner = resolveOwner(b);

            Building building = game.spawnBuilding(type, owner, row, col);

            if (b.has("hp")) {
                building.setHealth(b.get("hp").asInt());
            }
        }
    }

    private static void loadUnits(Game game, JsonNode units) {
        if (units == null) return;

        for (JsonNode u : units) {

            Unit unit = game.spawnUnit(
                    u.get("type").asText(),
                    u.get("owner").asText(),
                    u.get("row").asInt(),
                    u.get("col").asInt()
            );

            if (u.has("hp")) {
                unit.setHealth(u.get("hp").asInt());
            }
        }
    }

    private static String resolveOwner(JsonNode b) {
        String owner = "Neutral";

        if (b.has("owner") && !b.get("owner").isNull()) {
            String raw = b.get("owner").asText();
            if (!raw.isBlank() && !raw.equalsIgnoreCase("neutral")) {
                owner = raw;
            }
        }
        return owner;
    }

    public static void saveGameToJson(Game game, String filePath, CommandManager commandManager) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            ObjectNode root = mapper.createObjectNode();

            saveGameState(game, root);
            saveMap(game, root);
            saveBuildings(game, root);
            saveUnits(game, root);
            root.set("history", mapper.valueToTree(commandManager.getCommandHistory()));

            mapper.writeValue(new File(filePath), root);

        } catch (Exception e) {
            throw new RuntimeException("Failed to save game JSON", e);
        }
    }

    private static void saveGameState(Game game, ObjectNode root) {
        root.put("currentPlayer", game.getCurrentPlayer());

        ObjectNode moneyNode = root.putObject("playerMoney");
        List<String> players = List.of("Player1", "Player2");
        for (String p : players) {
            moneyNode.put(p, game.getPlayerMoney(p));
        }
    }

    private static void saveMap(Game game, ObjectNode root) {
        ArrayNode mapNode = root.putArray("map");
        TerrainType[][] terrainMap = game.getMap();
        for (TerrainType[] row : terrainMap) {
            ArrayNode rowNode = mapNode.addArray();
            for (TerrainType cell : row) {
                rowNode.add(cell.toString());
            }
        }
    }

    private static void saveBuildings(Game game, ObjectNode root) {
        ArrayNode buildingsNode = root.putArray("buildings");
        for (Building b : game.getBuildingsMap().values()) {
            ObjectNode bNode = buildingsNode.addObject();
            bNode.put("type", b.getType().toString());
            bNode.put("row", b.getPosition().getRow());
            bNode.put("col", b.getPosition().getCol());
            bNode.put("owner", b.getOwner());
            bNode.put("hp", b.getHealth());
        }
    }

    private static void saveUnits(Game game, ObjectNode root) {
        ArrayNode unitsNode = root.putArray("units");
        for (Unit u : game.getUnits()) {
            ObjectNode uNode = unitsNode.addObject();
            uNode.put("type", u.getType());
            uNode.put("owner", u.getOwner());
            uNode.put("row", u.getPosition().getRow());
            uNode.put("col", u.getPosition().getCol());
            uNode.put("hp", u.getHealth());
        }
    }

    public static Game generateNewGame(int rows, int cols, int cities, int facotries) {
        Game game = new Game();
        TerrainType[][] map = new TerrainType[rows][cols];

        // fill with Plain
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) map[r][c] = TerrainType.Plain;
        }

        // generate Nature
        generateBlobs(map, TerrainType.Water, 2, 8);
        generateBlobs(map, TerrainType.Mountain, 3, 5);
        generateBlobs(map, TerrainType.Forest, 5, 6);

        // basic game state
        game.setCurrentPlayer("Player1");
        game.setPlayerMoney("Player1", 3000);
        game.setPlayerMoney("Player2", 3000);

        // HQs
        int hq1R = 1, hq1C = 1;
        int hq2R = rows - 2, hq2C = cols - 2;

        map[hq1R][hq1C] = TerrainType.HQ;
        game.spawnBuilding(TerrainType.HQ, "Player1", hq1R, hq1C);

        map[hq2R][hq2C] = TerrainType.HQ;
        game.spawnBuilding(TerrainType.HQ, "Player2", hq2R, hq2C);

        // Factory and city near HQs
        spawnNearbyWithBuilding(game, map, hq1R, hq1C, TerrainType.Factory, "Player1");
        spawnNearbyWithBuilding(game, map, hq1R, hq1C, TerrainType.City, "Player1");
        spawnNearbyWithBuilding(game, map, hq2R, hq2C, TerrainType.Factory, "Player2");
        spawnNearbyWithBuilding(game, map, hq2R, hq2C, TerrainType.City, "Player2");

        // Scatter extra Cities / Facotries
        scatterBuildings(game, map, TerrainType.City, "Neutral", cities);
        scatterBuildings(game, map, TerrainType.Factory, "Neutral", facotries);

        game.updateMap(map);
        return game;
    }

    private static void generateBlobs(TerrainType[][] map, TerrainType type, int numBlobs, int maxSize) {
        Random rand = new Random();
        for (int i = 0; i < numBlobs; i++) {
            // Pick a random starting point
            int r = rand.nextInt(map.length);
            int c = rand.nextInt(map[0].length);

            int size = rand.nextInt(maxSize) + 2;
            for (int s = 0; s < size; s++) {
                if (isInBounds(map, r, c) && map[r][c] == TerrainType.Plain) {
                    map[r][c] = type;
                }
                // Move to an adjacent tile for the next part of the blob
                r += rand.nextInt(3) - 1; // -1, 0, or 1
                c += rand.nextInt(3) - 1;
            }
        }
    }

    private static void spawnNearbyWithBuilding(Game game, TerrainType[][] map, int r, int c, TerrainType type, String owner) {
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            int dr = rand.nextInt(3) - 1; // Direct neighbors
            int dc = rand.nextInt(3) - 1;
            int nr = r + dr, nc = c + dc;

            if (isInBounds(map, nr, nc) && map[nr][nc] == TerrainType.Plain) {
                map[nr][nc] = type;
                game.spawnBuilding(type, owner, nr, nc);
                return;
            }
        }
    }

    private static void scatterBuildings(Game game, TerrainType[][] map, TerrainType type, String owner, int count) {
        Random rand = new Random();
        int placed = 0;
        while (placed < count) {
            int r = rand.nextInt(map.length);
            int c = rand.nextInt(map[0].length);
            if (map[r][c] == TerrainType.Plain) {
                map[r][c] = type;
                game.spawnBuilding(type, owner, r, c);
                placed++;
            }
        }
    }

    private static boolean isInBounds(TerrainType[][] map, int r, int c) {
        return r >= 0 && r < map.length && c >= 0 && c < map[0].length;
    }
}