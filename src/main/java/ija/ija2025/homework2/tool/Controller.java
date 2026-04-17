package ija.ija2025.homework2.tool;
import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.game.Game;
import ija.ija2025.homework2.game.GamePhase;

public class Controller {
    Game game;

    GamePhase currentPhase = GamePhase.ACTION;
    Unit currentUnit = null;
    Position selectedPosition = null;

   public Controller(Game game) {
       this.game = game;
   }


    public void handleTileClick(Position position) {
        if (currentPhase != GamePhase.ACTION) {return;}
        if (currentUnit != null) {return;}

        Unit clickedUnit = game.getUnitAt(position);

        if (selectedPosition == null) {
            if (game.canControlUnitAt(position)) {
                selectedPosition = position;
                game.showReachablePositions(position);
            }
        } else {
            if (game.moveUnit(selectedPosition, position)) {
                currentUnit = game.getUnitAt(position);
            }
            deselect();
        }
    }




    public void handleTurnChange() {
        game.nextTurn();
    }


   private void deselect() {
       selectedPosition = null;
       game.showReachablePositions(null);
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
