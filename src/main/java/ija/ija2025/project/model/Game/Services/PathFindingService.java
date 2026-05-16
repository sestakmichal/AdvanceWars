/**
 * Authors: Michal Šesták, Matej Čapek
 * Description: Class service for finding the reachable and attackable positions.
 */

package ija.ija2025.project.model.Game.Services;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Map.TerrainType;
import ija.ija2025.project.model.Units.Unit;
import java.util.*;

public class PathFindingService {

    public static List<Position> getReachablePositions(
        TerrainType[][] map,
        HashMap<Position, Unit> allUnits,
        Unit unit,
        Position startPos)
    {
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
            int[] current = prQueue.poll();

            int cost = current[0];
            int row = current[1];
            int col = current[2];

            if (cost > maxUnitRange) continue;

            Unit unitOnTile = allUnits.get(new Position(row, col));
            Position currentPos = new Position(row, col);

            if (unitOnTile == null || currentPos.equals(startPos)) reachable.add(currentPos);

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
                    Unit blockingUnit = allUnits.get(new Position(newRow, newCol));
                    boolean isEnemy = blockingUnit != null && !unit.getOwner().equals(blockingUnit.getOwner());

                    // can't move through enemy
                    if (!isEnemy) {
                        distanceMap[newRow][newCol] = newCost;
                        prQueue.add(new int[]{newCost, newRow, newCol});
                    }
                }
            }
        }

        return reachable;
    }

    public static List<Position> getAttackablePositions(TerrainType[][] map, Unit unit, Position startPos) {
        if (map == null || unit == null || startPos == null) {return new ArrayList<>();}
        int height = map.length;
        int width = map[0].length;

        int[][] distanceMap = new int[height][width];
        for (int r = 0; r < height; r++) {
            Arrays.fill(distanceMap[r], Integer.MAX_VALUE);
        }
        distanceMap[startPos.getRow()][startPos.getCol()] = 0;


        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{0, startPos.getRow(), startPos.getCol()});

        List<Position> attackable = new ArrayList<>();
        Set<Integer> unitRange = unit.getAttackRange();

        int maxRange = unitRange.stream().max(Integer::compareTo).orElse(0);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cost = current[0];
            int row = current[1];
            int col = current[2];

            if (unitRange.contains(cost)) {
                attackable.add(new Position(row, col));
            }

            if (cost >= maxRange ) continue;

            for (int[] dir : new int[][]{
                    {1,  0}, // right
                    {-1, 0}, // left
                    {0,  1}, // down
                    {0, -1}  // up
            }) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow < 0 || newCol < 0 || newRow >= height || newCol >= width) continue;

                int newCost = cost + 1;

                if (newCost <= distanceMap[newRow][newCol]) {
                    distanceMap[newRow][newCol] = newCost;
                    queue.add(new int[]{newCost, newRow, newCol});
                }
            }
        }

        return attackable;
    }

}