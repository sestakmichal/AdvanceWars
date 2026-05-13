package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Units.Unit;

public class CaptureCommand implements Command {
    private Game game;
    private Unit unit;
    private String previousOwner;
    private int previousHP;
    private boolean wasCaptured;

    public CaptureCommand(Game game, Unit unit) {
        this.game = game;
        this.unit = unit;
    }

    @Override
    public void execute() {
        CaptureResult result = game.captureBuilding(unit);
        previousOwner = result.previousOwner();
        previousHP = result.previousHP();
        wasCaptured = result.wasCaptured();
    }

    @Override
    public void undo() { game.reverseCaptureBuilding(unit, previousOwner, previousHP, wasCaptured); }
}
