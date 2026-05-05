package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Units.Unit;

public record BuildingCapturedEvent(Unit unit) implements Event {}