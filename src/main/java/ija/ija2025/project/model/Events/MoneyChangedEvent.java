package ija.ija2025.project.model.Events;

public record MoneyChangedEvent(String player, Integer money) implements Event {}