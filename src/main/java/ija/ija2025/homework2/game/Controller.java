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
        if (selectedPosition == null) {
            if (game.getUnitAt(tile.getPos()) != null) {
                selectedPosition = tile.getPos();
            }
            return;
        }

        boolean moved = game.moveUnit(selectedPosition, tile.getPos());

        selectedPosition = null;
    }
}
