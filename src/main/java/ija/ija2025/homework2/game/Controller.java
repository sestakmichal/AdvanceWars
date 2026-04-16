package ija.ija2025.homework2.game;
import ija.ija2025.homework2.UI.SpriteFactory;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;

public class Controller {
    Game game;
    Position selectedPosition;

   public Controller(Game game) {
       this.game = game;
   }


    public void onTileClicked(Position position) {
        if (selectedPosition == null) {
            if (game.getUnitAt(position) != null) {
                selectedPosition = position;
            }
            return;
        }

        boolean moved = game.moveUnit(selectedPosition, position);

        selectedPosition = null;
    }

    public void initializeMockUnits(Game game) {
        game.createUnit("Tank", "Player1", 0, 0);
        game.createUnit("Infantry", "Player1", 0, 1);
        game.createUnit("Artillery", "Player1", 0, 2);

        game.createUnit("Tank", "Player2", 1, 9);
        game.createUnit("Infantry", "Player2", 1, 10);
        game.createUnit("Artillery", "Player2", 1, 11);
    }
}
