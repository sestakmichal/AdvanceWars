package ija.ija2025.homework2.game.Events;

import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;

public class UnitMovedEvent implements Event {
    Unit unit;
    Position from, to;

    public UnitMovedEvent(Unit unit, Position from, Position to) {
        this.unit = unit;
        this.from = from;
        this.to = to;
    }

}