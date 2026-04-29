package ija.ija2025.project.model.Game;

import ija.ija2025.project.model.Buildings.Building;
import ija.ija2025.project.model.Map.Position;
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

            // GAME STATE
            if (root.has("currentPlayer")) {
                game.setCurrentPlayer(root.get("currentPlayer").asText());
            }

            if (root.has("playerMoney")) {
                root.get("playerMoney").fields().forEachRemaining(e ->
                    game.setPlayerMoney(e.getKey(), e.getValue().asInt())
                );
            }

            // MAP (terrain only)
            JsonNode mapNode = root.get("map");

            int rows = mapNode.size();
            int cols = mapNode.get(0).size();

            TerrainType[][] terrainMap = new TerrainType[rows][cols];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    String tile = mapNode.get(r).get(c).asText();
                    terrainMap[r][c] = TerrainType.fromString(tile);
                }
            }

            game.updateMap(terrainMap);

            // BUILDINGS
            if (root.has("buildings")) {
                for (JsonNode b : root.get("buildings")) {

                    int row = b.get("row").asInt();
                    int col = b.get("col").asInt();

                    String type = b.get("type").asText();

                    // owner is Neutral if owner = null
                    String owner = "Neutral";
                    if (b.has("owner") && !b.get("owner").isNull()) {
                        String rawOwner = b.get("owner").asText();
                        if (!rawOwner.isBlank() && !rawOwner.equalsIgnoreCase("neutral")) {
                            owner = rawOwner;
                        }
                    }

                    Building building = game.spawnBuilding(type, owner, row, col);

                    if (b.has("hp")) {
                        building.setHealth(b.get("hp").asInt());
                    }
                }
            }

            // UNITS
            if (root.has("units")) {
                for (JsonNode u : root.get("units")) {

                    String type = u.get("type").asText();
                    String owner = u.get("owner").asText();
                    int row = u.get("row").asInt();
                    int col = u.get("col").asInt();

                    Unit unit = game.spawnUnit(type, owner, row, col);

                    if (u.has("hp")) {
                        unit.setHealth(u.get("hp").asInt());
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load game JSON", e);
        }

        return game;
    }
}
