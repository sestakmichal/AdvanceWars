package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Game.Game;

public class TurnEndCommand implements Command {
    private Game game;
    String previousPlayer;

    public TurnEndCommand(Game game, String previousPlayer) {
        this.game = game;
        this.previousPlayer = previousPlayer; //unused now
    }

    @Override
    public void execute() {
        previousPlayer = game.getCurrentPlayer();
        game.nextTurn();
    }

    @Override
    public void undo() { game.setCurrentPlayer(previousPlayer);}
}