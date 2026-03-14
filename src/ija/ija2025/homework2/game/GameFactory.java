package ija.ija2025.homework2.game;

import ija.ija2025.homework2.common.TerrainType;

import java.util.Arrays;

public class GameFactory {
    public static Game createGame(String[] map) {
        Game game = new Game();

        int rowSize = map.length;
        int colSize = map[0].replaceAll("\\s", "").length();
        TerrainType[][] convertedMap = new TerrainType[rowSize][colSize];

        for (int row = 0; row < rowSize; row++) {
            // remove spaces so only terrain characters remain
            String terrainRow = map[row].replaceAll("\\s","").toLowerCase();

            for (int col = 0; col < terrainRow.length(); col++) {
                char c = terrainRow.charAt(col);

                convertedMap[row][col] = TerrainType.fromChar(c);
            }
        }

        game.updateMap(convertedMap);

        // debug
        System.out.println(Arrays.deepToString(convertedMap));

        return game;
    }

}
