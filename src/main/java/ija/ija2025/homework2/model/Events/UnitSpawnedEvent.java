package ija.ija2025.homework2.model.Events;

import ija.ija2025.homework2.model.Map.Position;
import ija.ija2025.homework2.model.Units.Unit;

public record UnitSpawnedEvent(Unit unit, Position position) implements Event {}