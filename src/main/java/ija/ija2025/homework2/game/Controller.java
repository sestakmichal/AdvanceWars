package ija.ija2025.homework2.game;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;

public class Controller {
    private final Game game;
    Position selectedPosition;

   public Controller(Game game) {
       this.game = game;
   }


    public void onTileClicked(Tile tile) {
        if (selectedPosition == null) {
            if (game.getUnitAt(tile.getPos()) != null) {
                selectedPosition = tile.getPos();
                game.highlightUnitReach(selectedPosition);
            }
            return;
        }

        boolean moved = game.moveUnit(selectedPosition, tile.getPos());

        game.clearHilights();
        selectedPosition = null;
    }

    public void SpawnUnit(String unitType, String owner, Position where) {
        game.createUnit(unitType, owner, where.getRow(), where.getCol());
        // handle money?
    }

    public Game getGame() { return game; }
}
