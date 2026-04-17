package ija.ija2025.homework2.game.Services;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.TerrainType;
import ija.ija2025.homework2.common.Unit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PathFindingService {

    public static List<Position> getReachablePositions(TerrainType[][] map, Unit unit, Position startPos) {
        if (map == null || unit == null || startPos == null) {return new ArrayList<>();}
        int height = map.length;
        int width = map[0].length;

        int[][] distanceMap = new int[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                distanceMap[r][c] = Integer.MAX_VALUE;
            }
        }
        distanceMap[startPos.getRow()][startPos.getCol()] = 0;

        PriorityQueue<int[]> prQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        prQueue.add(new int[]{0, startPos.getRow(), startPos.getCol()});

        List<Position> reachable = new ArrayList<>();
        int maxUnitRange = unit.getMoveRange();

        while (!prQueue.isEmpty()) {
            int[] current = prQueue.poll(); // same like pq.pop() get lowest value node

            int cost = current[0];
            int row = current[1];
            int col = current[2];

            if (cost > maxUnitRange) continue;
            reachable.add(new Position(row, col));

            for (int[] dir : new int[][]{
                    {1,  0}, // right
                    {-1, 0}, // left
                    {0,  1}, // down
                    {0, -1}  // up
            }) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow < 0 || newCol < 0 || newRow >= height || newCol >= width) continue;

                int newCost = cost + unit.getMovementPrice(map[newRow][newCol]);

                if (newCost <= maxUnitRange && newCost < distanceMap[newRow][newCol]) {
                    distanceMap[newRow][newCol] = newCost;
                    prQueue.add(new int[]{newCost, newRow, newCol});
                }
            }
        }

        return reachable;
    }
}