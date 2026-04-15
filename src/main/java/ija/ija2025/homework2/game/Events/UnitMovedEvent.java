package ija.ija2025.homework2.game.Events;

import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.common.Unit;

public class UnitMovedEvent implements Event {
    private Unit unit;
    private Tile from;
    private Tile to;

    @Override
    public EventType getType() { return EventType.UNIT_MOVED; }

    public UnitMovedEvent(Unit unit, Tile from, Tile to) {
        this.unit = unit;
        this.from = from;
        this.to = to;
    }

}
