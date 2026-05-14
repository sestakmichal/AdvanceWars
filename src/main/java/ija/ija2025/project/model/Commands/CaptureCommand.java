/**
 * Authors: Matej Čapek
 * Description: Class describing the command for capturing a building.
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ija.ija2025.project.model.Commands.Results.CaptureResult;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Map.Position;

public class CaptureCommand implements Command {
    private Position unitPosition;
    private String previousOwner;
    private int previousHP;
    private boolean wasCaptured;

    @JsonIgnore
    private Game game;

    public CaptureCommand() {}

    public CaptureCommand(Game game, Unit unit) {
        this.game = game;
        this.unitPosition = unit.getPosition();
    }

    @Override
    public void execute() {
        Unit unit = game.getUnitAt(unitPosition);
        if (unit != null) {
            CaptureResult result = game.captureBuilding(unit);
            previousOwner = result.previousOwner();
            previousHP = result.previousHP();
            wasCaptured = result.wasCaptured();
        }
    }

    @Override
    public void undo() {
        Unit unit = game.getUnitAt(unitPosition);
        if (unit != null) {
            game.reverseCaptureBuilding(unit, previousOwner, previousHP, wasCaptured);
        }
    }

    public Position getUnitPosition() { return unitPosition; }
    public String getPreviousOwner() { return previousOwner; }
    public int getPreviousHP() { return previousHP; }

    @JsonProperty("wasCaptured")
    public boolean wasCaptured() { return wasCaptured; }

    @Override
    public void setGame(Game game) { this.game = game; }

    @Override
    public String getType() {
        return "capture";
    }
}
