package ija.ija2025.homework2.game.Events;

import ija.ija2025.homework2.common.Position;

import java.util.List;

public record HighlightTilesEvent(List<Position> tileCoordinates, boolean highlight) implements Event {
}
