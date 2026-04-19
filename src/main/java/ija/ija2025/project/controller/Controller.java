package ija.ija2025.project.controller;
import ija.ija2025.project.model.Commands.MoveCommand;
import ija.ija2025.project.model.Game.*;
import ija.ija2025.project.model.Map.Position;

public class Controller {
    Game game;
    CommandManager commandManager =  new CommandManager();
    TurnPhase currentPhase = TurnPhase.ACTION;
    TurnState currentState = TurnState.IDLE;

    Position selectedPos = null;

    public Controller(Game game) {
       this.game = game;
    }

    /// When Unit clicked, first display menu with move/attack/capture/wait options
    ///
    public void handleTileClick(Position position) {
        if (currentPhase != TurnPhase.ACTION) {return;}

        // select unit

        if (currentState == TurnState.IDLE) {
            if (game.canUnitAct(position)) {
                game.hidePositions();
                selectedPos = position;
                if (game.canUnitMove(position)) {
                    game.showMovablePositions(position);
                }
                if (game.canUnitAct(position)) {
                    ///game.showAttackablePositions(position);
                }
                game.selectUnitPosition(position);
                return;
            }
            deselect();
        }

        if (currentState == TurnState.MOVING) {
            if (game.canUnitMove(selectedPos) && game.canUnitMoveTo(selectedPos, position)) {
                game.hidePositions();
                commandManager.execute(new MoveCommand(game, selectedPos, position));
                selectedPos = position;
                game.selectUnitPosition(position);
                currentState = TurnState.IDLE;
                return;
            }
            deselect();
        }

    }

    /// needs to be connected to the unit's action buttons
    public void handleActionClicked(GameAction action) { // Position position
        switch (action) {
            case MOVE -> {
                currentState = TurnState.MOVING;
                game.hideMenu();
            }
            case ATTACK -> {
                currentState = TurnState.ATTACKING;
                game.hideMenu();
            }
            case CAPTURE -> {
                currentState = TurnState.CAPTURING;

            }
            case WAIT -> {
                currentState = TurnState.IDLE;
                game.waitUnit(selectedPos);
            }
        }

    } ///maybe divide into specific actions, disable action ability here


    public void handleTurnChange() {
        game.nextTurn();
    }


    private void deselect() {
        currentState = TurnState.IDLE;
        selectedPos = null;
        game.hidePositions();
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
