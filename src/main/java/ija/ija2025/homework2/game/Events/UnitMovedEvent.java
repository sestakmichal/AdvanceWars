package ija.ija2025.homework2.game.Events;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;

public record UnitMovedEvent(Unit unit, Position from, Position to) implements Event {}