package ija.ija2025.homework2.game;

import ija.ija2025.homework2.common.*;
import ija.ija2025.homework2.common.units.Artillery;
import ija.ija2025.homework2.common.units.Infantry;
import ija.ija2025.homework2.common.units.Tank;
import ija.ija2025.homework2.tool.GameObserver;

import java.util.List;

public class Game {
    //private map;
    private GameObserver gameObserver;

    public Unit createUnit(String type, String owner, int x, int y) {
        return switch (type) {
            case "Tank" -> new Tank("Tank", owner, x, y);
            case "Artillery" -> new Artillery("Artillery", owner, x, y);
            case "Infantry" -> new Infantry("Infantry", owner, x, y);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public List<Position> getReachableTiles(Position position) {
        return null;
    }

    public  boolean moveUnit(Position startPos, Position endPos) {
        // return true if move is possible
        return false;
    }

    public void addObserver(GameObserver gameObserver) {
        this.gameObserver = gameObserver;
    }
}
