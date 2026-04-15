package ija.ija2025.homework2.UI;

import ija.ija2025.homework2.game.Game;
import ija.ija2025.homework2.common.Position;
import javafx.scene.Node;

import java.util.List;

public class MapController {
    private final Game game;
    private final Tile[][] visualTiles;
    private Position selectedPos = null;

    public MapController(Game game, Tile[][] visualTiles) {
        this.game = game;
        this.visualTiles = visualTiles;
    }

    public void handleInteraction(Tile clickedTile) {
        Position currentPos = new Position(clickedTile.getRow(), clickedTile.getCol());

        // select a unit
        if (selectedPos == null) {
            if (game.getUnitAt(currentPos) != null) {
                selectedPos = currentPos;

                List<Position> reachable = game.getReachableTiles(currentPos);

                // highlight tiles
                clickedTile.setHighlight(true);
                for (Position p : reachable) {
                    visualTiles[p.getRow()][p.getCol()].setHighlight(true);
                }
            }
        }
        // move the unit
        else {
            if (clickedTile.isHighlighted()) {
                // move in backend
                game.moveUnit(selectedPos, currentPos);

                // sync UI
                Tile sourceTile = visualTiles[selectedPos.getRow()][selectedPos.getCol()];
                Node sprite = sourceTile.getUnitVisual();

                sourceTile.removeUnitVisual();
                clickedTile.setUnitVisual(sprite);
            }

            clearAllHighlights();
            selectedPos = null;
        }
    }

    private void clearAllHighlights() {
        for (Tile[] row : visualTiles) {
            for (Tile t : row) {
                t.setHighlight(false);
            }
        }
    }
}