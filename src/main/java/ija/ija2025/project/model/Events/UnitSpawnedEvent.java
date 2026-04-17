package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;

public record UnitSpawnedEvent(Unit unit, Position position) implements Event {}