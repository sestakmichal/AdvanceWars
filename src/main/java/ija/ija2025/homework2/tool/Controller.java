package ija.ija2025.homework2.tool;
import ija.ija2025.homework2.common.Commands.MoveCommand;
import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.game.Game;
import ija.ija2025.homework2.game.GamePhase;

import java.io.Console;

public class Controller {
    Game game;
    CommandManager commandManager =  new CommandManager();
    GamePhase currentPhase = GamePhase.ACTION;

    Unit selectedUnit = null;
    Position selectedPosition = null;

   public Controller(Game game) {
       this.game = game;
   }

    public void handleTileClick(Position position) {
        if (currentPhase != GamePhase.ACTION) {return;}

        if (selectedPosition == null) {
            if (game.canControlUnitAt(position)) {
                selectedPosition = position;
                game.showReachablePositions(position);
                game.selectPosition(position);
            }
            return;
        }

        if (game.canMoveUnit(selectedPosition, position)) {
            commandManager.execute(new MoveCommand(game, selectedPosition, position));
            game.selectPosition(position);
            selectedUnit = game.getUnitAt(position);
            return;
        }

        if (selectedUnit == null) {
            deselect();
        }
    }


    public void handleActionClicked(Position position) {


    } ///maybe divide into specific actions, disable action ability here


    public void handleTurnChange() {
        game.nextTurn();
    }


    private void deselect() {
       selectedPosition = null;
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
