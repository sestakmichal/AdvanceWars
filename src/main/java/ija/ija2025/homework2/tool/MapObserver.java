package ija.ija2025.homework2.tool;

import ija.ija2025.homework2.UI.SpriteFactory;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.common.*;
import ija.ija2025.homework2.game.Events.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.List;

public class MapObserver implements Observer {

    private final GridPane grid;
    private final Tile[][] tiles;
    private Tile selectedTile = null;

    public MapObserver(Position size) {
        this.grid = new GridPane();
        this.tiles = new Tile[size.getRow()][size.getCol()];

        grid.setStyle("-fx-background-color: #333;");
        grid.setHgap(1);
        grid.setVgap(1);
    }

    private Tile getTile(Position pos) {
        return tiles[pos.getRow()][pos.getCol()];
    }

    public void initialize(Controller controller, TerrainType[][] map) {
        Position size = new Position(map.length, map[0].length);

        for (int r = 0; r < size.getRow(); r++) {
            for (int c = 0; c < size.getCol(); c++) {

                Tile tile = new Tile(r, c, map[r][c], 60);

                int finalR = r;
                int finalC = c;
                tile.setOnMouseClicked(e -> tileClick(tile, controller));

                tiles[r][c] = tile;
                grid.add(tile, c, r);
            }
        }
    }

    private void tileClick(Tile tile, Controller controller) {
        if (selectedTile != null) {
            selectedTile.removeHighlight();
        }

        tile.setHighlight();
        selectedTile = tile;

        controller.handleTileClick(tile.getPos());
    }

    private void clearAllHighlights() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.removeHighlight();
            }
        }
    }

    public GridPane getNode() {
        return grid;
    }

    @Override
    public void update(Event event) {
        if (event instanceof UnitMovedEvent(Unit unit, Position from, Position to)) {
            Tile fromTile = getTile(from);
            Tile toTile = getTile(to);

            Node sprite = fromTile.getUnitVisual();

            fromTile.removeUnitVisual();
            toTile.setUnitVisual(sprite);

            clearAllHighlights();
        }

        if (event instanceof UnitSpawnedEvent(Unit unit, Position pos)) {
            Tile tile = getTile(pos);
            tile.setUnitVisual(SpriteFactory.generateSprite(unit));
        }

        if (event instanceof ReachablePositionsEvent(List<Position> positions)) {
            clearAllHighlights();
            for (Position position : positions) {
                getTile(position).setHighlightReach();
            }
        }
    }
}