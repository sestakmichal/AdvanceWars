/**
 * Authors: Matej Čapek
 * Description: Class describing command for ending the game's turn.
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ija.ija2025.project.model.Commands.Helpers.UnitHPEntry;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TurnChangeCommand implements Command {
    private String newPlayer;
    private String previousPlayer;
    private int newPlayerPreviousMoney;

    @JsonIgnore
    private Map<Position, Integer> newPlayerPreviousUnitHP = new HashMap<>();
    @JsonIgnore
    private Game game;

    public TurnChangeCommand() {}

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
                newPlayerPreviousUnitHP.put(unit.getPosition(), unit.getHealth());
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

    public String getNewPlayer() { return newPlayer; }
    public void setNewPlayer(String newPlayer) { this.newPlayer = newPlayer; }

    public String getPreviousPlayer() { return previousPlayer; }
    public void setPreviousPlayer(String previousPlayer) { this.previousPlayer = previousPlayer; }

    public int getNewPlayerPreviousMoney() { return newPlayerPreviousMoney; }
    public void setNewPlayerPreviousMoney(int newPlayerPreviousMoney) { this.newPlayerPreviousMoney = newPlayerPreviousMoney; }

    public List<UnitHPEntry> getUnitHPHistory() {
        List<UnitHPEntry> list = new ArrayList<>();
        for (Map.Entry<Position, Integer> entry : newPlayerPreviousUnitHP.entrySet()) {
            list.add(new UnitHPEntry(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public void setUnitHPHistory(List<UnitHPEntry> list) {
        this.newPlayerPreviousUnitHP = new HashMap<>();
        for (UnitHPEntry entry : list) {
            this.newPlayerPreviousUnitHP.put(entry.position, entry.hp);
        }
    }
}