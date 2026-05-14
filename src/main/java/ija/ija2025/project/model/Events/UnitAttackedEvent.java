package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Units.Unit;

public record UnitAttackedEvent(Unit attacker, Unit target, int damage) implements Event {}
///ActionObserver should react too and display options