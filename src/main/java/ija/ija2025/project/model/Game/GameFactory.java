package ija.ija2025.project.model.Game;

import ija.ija2025.project.model.Buildings.Building;
import ija.ija2025.project.model.Map.TerrainType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ija.ija2025.project.model.Units.Unit;

import java.io.File;

public class GameFactory {
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

}