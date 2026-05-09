package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Units.Unit;

public class WaitCommand implements Command {
    private Game game;
    private Unit unit;

    public WaitCommand(Game game, Unit unit) {
        this.game = game;
        this.unit = unit;
    }

    @Override
    public void execute() {game.waitUnit(unit);}

    @Override
    public void undo() {
        // heal target
        // respawn unit if it was destroyed
    }
}
