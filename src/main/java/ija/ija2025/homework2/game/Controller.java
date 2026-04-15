package ija.ija2025.homework2.game;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.common.Position;

public class Controller {
    Game game;
    Position selectedPosition;

   public Controller(Game game) {
       this.game = game;
   }



    public void onTileClicked(Tile tile) {
        Position pos = new Position(tile.getRow(), tile.getCol());

        if (selectedPosition == null) {
            if (game.getUnitAt(pos) != null) {
                selectedPosition = pos;
            }
            return;
        }

        boolean moved = game.moveUnit(selectedPosition, pos);

        selectedPosition = null;
    }
}
