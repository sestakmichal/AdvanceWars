package ija.ija2025.homework2.game;

import ija.ija2025.homework2.common.*;
import ija.ija2025.homework2.common.units.*;
import ija.ija2025.homework2.game.Events.Event;
import ija.ija2025.homework2.tool.Observer;
import ija.ija2025.homework2.tool.Observable;

import java.util.*;

public class Game implements Observable {
    private Position mapSize;
    private TerrainType[][] map;

    private List<Unit> units = new ArrayList<>();
    private HashMap<Position, Unit> unitsMap = new HashMap<>();
    private List<Observer> gameObservers = new ArrayList<>();

    public void updateMap(TerrainType[][] newMap) {
        map = newMap;
        mapSize = new Position(map.length, map[0].length);
    }

    public TerrainType getTerrainAt(Position pos) {
        return map[pos.getRow()][pos.getCol()];
    }

    public Position getSize() { return mapSize; }

    public Unit createUnit(String type, String owner, int row, int col) {
        Unit unit = switch (type) {
            case "Tank" -> new Tank("Tank", owner, row, col);
            case "Artillery" -> new Artillery("Artillery", owner, row, col);
            case "Infantry" -> new Infantry("Infantry", owner, row, col);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        units.add(unit);
        unitsMap.put(new Position(row, col), unit);
        // notify unit spawn here
        return unit;
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
        unitsMap.remove(unit.getPosition());

        // notify unit removal here
    }

    public Unit getUnitAt(Position pos) {
        return unitsMap.get(pos);
    }

    // flood of all reachable tiles
    public List<Position> getReachableTiles(Position position) {
        if (map == null) return null;
        int height = mapSize.getRow();
        int width = mapSize.getCol();

        Unit unit = getUnitAt(position);
        if (unit == null) return null;

        // init distance array with max value
        int[][] distanceMap = new int[height][width];
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                distanceMap[r][c] = Integer.MAX_VALUE;
            }
        }
        // set starting position to 0
        distanceMap[position.getRow()][position.getCol()] = 0;

        // compare costs, sort from highest to lowest
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            Comparator.comparingInt(a -> a[0])
        );
        // add initial position
        pq.add(new int[]{0, position.getRow(), position.getCol()});

        List<Position> reachable = new ArrayList<>();
        int maxUnitRange = unit.getMoveRange();

        while (!pq.isEmpty()) {
            int[] current = pq.poll(); // same like pq.pop() get lowest value node

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

                // in bounds check
                if (newRow < 0 || newCol < 0 || newRow >= height || newCol >= width) continue;

                int newCost = cost + unit.getMovementPrice(map[newRow][newCol]);

                // is reachable?
                if (newCost <= maxUnitRange && newCost < distanceMap[newRow][newCol]) {
                    distanceMap[newRow][newCol] = newCost; // save cost
                    pq.add(new int[]{newCost, newRow, newCol}); // add node to queue
                }
            }
        }

        return reachable;
    }

    public boolean moveUnit(Position startPos, Position endPos) {
        List<Position> reachable = getReachableTiles(startPos);

        if (reachable.contains(endPos)) {
            Unit movingUnit = getUnitAt(startPos);
            if (movingUnit == null) return false;

            unitsMap.remove(startPos);
            movingUnit.setPosition(endPos);
            unitsMap.put(endPos, movingUnit);

            //notifyObservers();
            return true;
        }
        return false;
    }

    public void addObserver(Observer gameObserver) {
        this.gameObservers.add(gameObserver);
    }
    public void removeObserver(Observer gameObserver) { this.gameObservers.remove(gameObserver); }
    public void notifyObservers(Event event) {
        for (Observer observer: gameObservers) {
            observer.update(event);
        }
    }
}
