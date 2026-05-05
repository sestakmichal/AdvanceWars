package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Units.Unit;

public class CaptureCommand implements Command {
    private Game game;
    private Unit unit;

    public CaptureCommand(Game game, Unit unit) {
        this.game = game;
        this.unit = unit;
    }

    @Override
    public void execute() {game.captureBuilding(unit);}

    @Override
    public void undo() {
        // heal target
        // respawn unit if it was destroyed
    }
}
