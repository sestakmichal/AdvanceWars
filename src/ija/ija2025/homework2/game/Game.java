package ija.ija2025.homework2.game;

import ija.ija2025.homework2.common.*;
import ija.ija2025.homework2.common.units.Artillery;
import ija.ija2025.homework2.common.units.Infantry;
import ija.ija2025.homework2.common.units.Tank;
import ija.ija2025.homework2.tool.GameObserver;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private TerrainType[][] map;
    private List<Unit> units = new ArrayList<>();
    private GameObserver gameObserver;

    public void updateMap(TerrainType[][] newMap) { map = newMap; }

    public Unit createUnit(String type, String owner, int x, int y) {
        Unit unit = switch (type) {
            case "Tank" -> new Tank("Tank", owner, x, y);
            case "Artillery" -> new Artillery("Artillery", owner, x, y);
            case "Infantry" -> new Infantry("Infantry", owner, x, y);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
        units.add(unit);
        return unit;
    }

    public Unit getUnitAt(Position pos) {
        for (Unit unit : units) {
            if (pos.equals(unit.getPosition())) {
                return unit;
            }
        }
        return null;
    }

    // flood of all reachable tiles
    public List<Position> getReachableTiles(Position position) {
        Unit unit = getUnitAt(position);
        if (unit == null) return null;

        List<Position> reachable = new ArrayList<>();
        int range = unit.getMoveRange();

        // dijkstra TODO

        return reachable;
    }

    public boolean moveUnit(Position startPos, Position endPos) {
        List<Position> reachable = getReachableTiles(startPos);

        if (reachable.contains(endPos)) {
            getUnitAt(startPos).setPosition(endPos);
            return true;
        }
        return false;
    }

    public void addObserver(GameObserver gameObserver) {
        this.gameObserver = gameObserver;
    }
}
