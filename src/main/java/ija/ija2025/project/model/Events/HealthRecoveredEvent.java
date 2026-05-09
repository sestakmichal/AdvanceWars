package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Units.Unit;

public record HealthRecoveredEvent(Unit unit, int health) implements Event {}