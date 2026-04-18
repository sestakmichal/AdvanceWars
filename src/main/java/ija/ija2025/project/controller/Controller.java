package ija.ija2025.project.controller;
import ija.ija2025.project.model.Commands.MoveCommand;
import ija.ija2025.project.model.Game.*;
import ija.ija2025.project.model.Map.Position;

public class Controller {
    Game game;
    CommandManager commandManager =  new CommandManager();
    TurnPhase currentPhase = TurnPhase.ACTION;
    TurnState currentState = TurnState.IDLE;
    TurnAction currentAction = null;

    Position selectedPosition = null;

    public Controller(Game game) {
       this.game = game;
    }

    /// When Unit clicked, first display menu with move/attack/capture/wait options
    ///
    public void handleTileClick(Position position) {
        if (currentPhase != TurnPhase.ACTION) {return;}

        // select unit
        if (selectedPosition == null) {
            if (game.canUnitAct(position)) {
                selectedPosition = position;
                currentState = TurnState.UNIT_SELECTED;

                game.selectPosition(position);
            }
            return;
        }
        else {
            if (currentAction == TurnAction.MOVE && game.canUnitReach(selectedPosition, position)) {
                commandManager.execute(new MoveCommand(game, selectedPosition, position));

                selectedPosition = position;
                game.selectPosition(position);
                currentAction = null;
                return;
            }
        }

        deselect();
    }

    /// needs to be connected to the unit's action buttons
    public void handleActionClicked(GameAction action) { // Position position
        if (game.canUnitMove(selectedPosition)) {
            currentAction = TurnAction.MOVE;
            game.showMovablePositions(selectedPosition);
        }

    } ///maybe divide into specific actions, disable action ability here


    public void handleTurnChange() {
        game.nextTurn();
    }


    private void deselect() {
       selectedPosition = null;
       currentState = TurnState.IDLE;
       game.hidePositions();//maybe make separate method for deselecting
    }

    public void initializeMockUnits(Game game) {
        game.spawnUnit("Tank", "Player1", 0, 0);
        game.spawnUnit("Infantry", "Player1", 0, 1);
        game.spawnUnit("Artillery", "Player1", 0, 2);

        game.spawnUnit("Tank", "Player2", 1, 9);
        game.spawnUnit("Infantry", "Player2", 1, 10);
        game.spawnUnit("Artillery", "Player2", 1, 11);
    }
}
