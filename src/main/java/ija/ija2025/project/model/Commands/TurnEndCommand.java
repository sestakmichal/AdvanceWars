/**
 * Authors: Michal Šesták
 * Description: Class describing command for ending the game's turn.
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ija.ija2025.project.model.Game.Game;

public class TurnEndCommand implements Command {
    String previousPlayer;

    @JsonIgnore
    private Game game;

    public TurnEndCommand() {}

    public TurnEndCommand(Game game, String previousPlayer) {
        this.game = game;
    }

    @Override
    public void execute() {
        if (game != null) {
            previousPlayer = game.getCurrentPlayer();
            game.nextTurn();
        }
    }

    @Override
    public void undo() {
        if (game != null && previousPlayer != null) {
            game.setCurrentPlayer(previousPlayer);
        }
    }

    public String getPreviousPlayer() {
        return previousPlayer;
    }

    // Setter pro Jackson (pokud nepoužíváte reflexi přímo na pole)
    public void setPreviousPlayer(String previousPlayer) {
        this.previousPlayer = previousPlayer;
    }

    @Override
    public void setGame(Game game) { this.game = game; }

    @Override
    public String getType() {
        return "turnEnd";
    }
}