package ija.ija2025.project.model.Events;
import ija.ija2025.project.model.Map.Position;

public record PositionSelectedEvent(Position position) implements Event {}