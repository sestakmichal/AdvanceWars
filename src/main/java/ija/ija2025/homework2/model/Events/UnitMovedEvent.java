package ija.ija2025.homework2.model.Events;

import ija.ija2025.homework2.model.Map.Position;
import ija.ija2025.homework2.model.Units.Unit;

public record UnitMovedEvent(Unit unit, Position from, Position to) implements Event {}
///ActionObserver should react too and display options