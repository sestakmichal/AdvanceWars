package ija.ija2025.project.model.Events;

import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;

public record MoneyEarnedEvent(String player, int amount) implements Event {}