package ija.ija2025.homework2.game;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.tool.GameObserver;

import java.util.List;

public class Game {
    //private map;
    private GameObserver gameObserver;

    public Unit createUnit(String type, String owner, int x, int y) {
        return new Unit(type, owner, x, y);
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
