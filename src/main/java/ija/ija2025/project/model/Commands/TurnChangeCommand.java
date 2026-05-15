/**
 * Authors: Michal Šesták
 * Description: Class describing command for ending the game's turn.
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Units.Unit;

import java.util.HashMap;
import java.util.Map;

public class TurnChangeCommand implements Command {
    private String newPlayer;
    private String previousPlayer;
    private int newPlayerPreviousMoney;
    private Map<Unit, Integer> newPlayerPreviousUnitHP = new HashMap<>();

    @JsonIgnore
    private Game game;

    public TurnChangeCommand(Game game) {
        this.game = game;
        this.previousPlayer = game.getCurrentPlayer();
        newPlayer = game.getCurrentPlayer().equals("Player1") ? "Player2" : "Player1";
    }

    @Override
    public void execute() {
        newPlayerPreviousMoney = game.getPlayerMoney(newPlayer);
        for (Unit unit : game.getUnits()) {
            if (unit.isOwnedBy(newPlayer)) {
                newPlayerPreviousUnitHP.put(unit, unit.getHealth());
            }
        }
        game.nextTurn(newPlayer);
    }

    @Override
    public void undo() {
        game.reverseTurn(previousPlayer, newPlayerPreviousMoney, newPlayerPreviousUnitHP);
    }

    @Override
    public String getType() { return "turnEnd"; }

    @Override
    public void setGame(Game game) { this.game = game; }
}