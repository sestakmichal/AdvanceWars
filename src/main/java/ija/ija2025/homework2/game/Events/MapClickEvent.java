package ija.ija2025.homework2.game.Events;

import ija.ija2025.homework2.UI.Tile;

public class MapClickEvent implements Event {
    private final Tile tile;
    @Override
    public EventType getType() { return EventType.MAP_CLICKED; }

    public MapClickEvent(Tile tile) { this.tile = tile; }

    public Tile getTile() { return tile; }
}
