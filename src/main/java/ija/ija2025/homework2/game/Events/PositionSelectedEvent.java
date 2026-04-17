package ija.ija2025.homework2.game.Events;
import ija.ija2025.homework2.common.Position;

public record PositionSelectedEvent(Position position) implements Event {}