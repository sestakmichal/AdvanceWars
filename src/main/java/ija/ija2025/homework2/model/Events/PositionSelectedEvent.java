package ija.ija2025.homework2.model.Events;
import ija.ija2025.homework2.model.Map.Position;

public record PositionSelectedEvent(Position position) implements Event {}