package ija.ija2025.project.controller;
import ija.ija2025.project.model.Commands.*;
import ija.ija2025.project.model.Game.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;

public class Controller {
    private Game game;
    private Runnable onReturnToMenu;
    private CommandManager commandManager = new CommandManager();
    private TurnState currentState = TurnState.IDLE;
    private Position selectedPos = null;

    public Controller(Game game) {
        this.game = game;
    }

    /// When Unit clicked, first display menu with move/attack/capture/wait options
    public void handleTileClick(Position position) {

        if (currentState == TurnState.IDLE) {
            if (game.canUnitAct(position)) {
                selectedPos = position;
                finalizeAction();
                return;
            }
            if (game.isOwnedFactory(position) && game.getUnitAt(position) == null) {
                game.openFactoryMenu(position);
                return;
            }
            deselect();
        }

        if (currentState == TurnState.MOVING) {
            if (game.canUnitMove(selectedPos) && game.canUnitMoveTo(selectedPos, position)) {
                commandManager.execute(new MoveCommand(game, selectedPos, position));

                selectedPos = position;
                finalizeAction();
                return;
            }
            deselect();
        }

        if (currentState == TurnState.ATTACKING) {
            if (game.canUnitAct(selectedPos) && game.canUnitAttackAt(selectedPos, position)) {
                Unit attacker = game.getUnitAt(selectedPos);
                Unit target = game.getUnitAt(position);
                commandManager.execute(new AttackCommand(game, attacker, target));

                finalizeAction();
                return;
            }
            deselect();
        }

        if (currentState == TurnState.CAPTURING) {
            if (game.canUnitAct(selectedPos) && game.canUnitCaptureAt(selectedPos)) {
                Unit capturer = game.getUnitAt(selectedPos);
                commandManager.execute(new CaptureCommand(game, capturer));

                finalizeAction();
                return;
            }
            deselect();
        }

    }

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
                handleTileClick(null);
            }
            case WAIT -> {
                currentState = TurnState.IDLE;
                game.hideMenu();
                commandManager.execute(new WaitCommand(game, game.getUnitAt(selectedPos)));
                finalizeAction();
            }
        }

    }

    public void handleBuyRequest(String type, Position pos, int cost) {
        commandManager.execute(new BuyUnitCommand(game, type, pos, cost));
    }

    private void deselect() {
        currentState = TurnState.IDLE;
        selectedPos = null;
        game.hidePositions();
    }

    private void finalizeAction() {
        currentState = TurnState.IDLE;
        showPositions(selectedPos);
        checkTurn();
    }

    private void showPositions(Position pos) {
        game.hidePositions();
        if (game.canUnitMove(pos)) {
            game.showMovablePositions(pos);
        }
        if (game.canUnitAct(pos)) {
            game.showAttackablePositions(pos);
            game.showUnitPosition(pos);
        }
    }

    private void checkTurn() {
        if (!game.unitsAreAvailableForCurrentPlayer()) {
            commandManager.execute(new TurnEndCommand(game, game.getCurrentPlayer()));
        }
    }

    public boolean canAffordUnit(int unitValue) {
        int playerMoney = game.getPlayerMoney(game.getCurrentPlayer());
        return playerMoney >= unitValue;
    }

    public void handleSaveGame(String filePath) {
        try {
            GameFactory.saveGameToJson(game, filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save: " + e.getMessage(), e);
        }
    }

    public void setOnReturnToMenu(Runnable callback) {
        this.onReturnToMenu = callback;
    }

    public void handleReturnToMenu() {
        if (onReturnToMenu != null) {
            onReturnToMenu.run();
        }
    }
}