package ija.ija2025.homework2.game.Events;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;

public record UnitSpawnedEvent(Unit unit, Position position) implements Event {}