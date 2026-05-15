/**
 * Authors: Matej Čapek
 * Description: Class describing command for unit waiting.
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Map.Position;

public class WaitCommand implements Command {
    private Position unitPosition;

    @JsonIgnore
    private Game game;

    public WaitCommand() {}

    public WaitCommand(Game game, Unit unit) {
        this.game = game;
        this.unitPosition = unit.getPosition();
    }

    @Override
    public void execute() {
        if (game != null) {
            Unit unit = game.getUnitAt(unitPosition);
                game.waitUnit(unit);
        }
    }

    @Override
    public void undo() {
        if (game != null) {
            Unit unit = game.getUnitAt(unitPosition);
            game.reverseWaitUnit(unit);
        }
    }

    public Position getUnitPosition() {
        return unitPosition;
    }

    @Override
    public void setGame(Game game) { this.game = game; }

    @Override
    public String getType() {
        return "wait";
    }
}
