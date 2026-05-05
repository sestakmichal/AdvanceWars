package ija.ija2025.project.controller;
import ija.ija2025.project.model.Commands.*;
import ija.ija2025.project.model.Game.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;

public class Controller {
    Game game;
    CommandManager commandManager = new CommandManager();
    TurnPhase currentPhase = TurnPhase.ACTION;
    TurnState currentState = TurnState.IDLE;

    Position selectedPos = null;

    public Controller(Game game) {
        this.game = game;
    }

    /// When Unit clicked, first display menu with move/attack/capture/wait options
    ///
    public void handleTileClick(Position position) {
        if (currentPhase != TurnPhase.ACTION) {
            return;
        }

        // select unit

        if (currentState == TurnState.IDLE) {
            if (game.canUnitAct(position)) {
                game.hidePositions();
                selectedPos = position;
                selectDoablePositions(selectedPos);
                return;
            }
            deselect();
        }

        if (currentState == TurnState.MOVING) {
            if (game.canUnitMove(selectedPos) && game.canUnitMoveTo(selectedPos, position)) {
                game.hidePositions();
                commandManager.execute(new MoveCommand(game, selectedPos, position));
                selectedPos = position;
                selectDoablePositions(selectedPos);
                currentState = TurnState.IDLE;
                return;
            }
            deselect();
        }

        if (currentState == TurnState.ATTACKING) {
            if (game.canUnitAct(selectedPos) && game.canUnitAttackAt(selectedPos, position)) {
                game.hidePositions();
                Unit attacker = game.getUnitAt(selectedPos);
                Unit target = game.getUnitAt(position);

                commandManager.execute(new AttackCommand(game, attacker, target));
                selectDoablePositions(selectedPos);
                currentState = TurnState.IDLE;
                return;
            }
            deselect();
        }

        if (currentState == TurnState.CAPTURING) {
            if (game.canUnitAct(selectedPos) && game.canUnitCaptureAt(selectedPos)) {
                game.hidePositions();

                Unit capturer = game.getUnitAt(selectedPos);
                commandManager.execute(new CaptureCommand(game, capturer));
                selectDoablePositions(selectedPos);
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
                game.hideMenu();
            }
            case WAIT -> {
                currentState = TurnState.IDLE;
                game.waitUnit(selectedPos);
            }
        }

    }

    /// maybe divide into specific actions, disable action ability here


    public void handleTurnChange() {
        game.nextTurn();
    }

    private void deselect() {
        currentState = TurnState.IDLE;
        selectedPos = null;
        game.hidePositions();
    }

    private void selectDoablePositions(Position pos) {
        if (game.canUnitMove(pos)) {
            game.showMovablePositions(pos);
        }
        if (game.canUnitAct(pos)) {
            game.showAttackablePositions(pos);
            game.showUnitPosition(pos);
        }
    }
}
