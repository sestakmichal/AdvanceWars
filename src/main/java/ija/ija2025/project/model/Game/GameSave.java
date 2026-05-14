package ija.ija2025.project.model.Game;

import ija.ija2025.project.model.Commands.Command;

import java.util.List;

public record GameSave(Game game, List<Command> history) {}