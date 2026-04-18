package ija.ija2025.project.view;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Events.*;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Map.TerrainType;
import ija.ija2025.project.model.Units.Unit;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

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

    public GridPane getGrid() {
        return grid;
    }

    public void initializeMap(Controller controller, TerrainType[][] map) {
        Position size = new Position(map.length, map[0].length);

        for (int r = 0; r < size.getRow(); r++) {
            for (int c = 0; c < size.getCol(); c++) {

                Tile tile = new Tile(r, c, map[r][c], 60);

                int finalR = r, finalC = c;
                tile.setOnMouseClicked(e -> controller.handleTileClick(tile.getPos()));

                tiles[r][c] = tile;
                grid.add(tile, c, r);
            }
        }
    }

    private void clearAllHighlights() {
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.removeHighlight();
            }
        }
    }

    private Tile getTile(Position pos) {
        return tiles[pos.getRow()][pos.getCol()];
    }

    @Override
    public void update(Event event) {
        if (event instanceof UnitMovedEvent(Unit unit, Position from, Position to)) {
            clearAllHighlights();

            Tile fromTile = getTile(from), toTile = getTile(to);
            Node sprite = fromTile.getUnitVisual();

            getTile(from).removeUnitVisual();
            toTile.setUnitVisual(sprite);

            toTile.setHighlight();
            /// should display options after moving
        }

        if (event instanceof UnitSpawnedEvent(Unit unit, Position pos)) {
            getTile(pos).setUnitVisual(SpriteFactory.generateSprite(unit));
        }

        if (event instanceof PositionSelectedEvent(Position pos)) {
            getTile(pos).setHighlight();
        }

        if (event instanceof ReachablePositionsShownEvent(List<Position> positions)) {
            clearAllHighlights();
            for (Position position : positions) {
                getTile(position).setHighlightReach();
            }
        }

        if (event instanceof PositionsHiddenEvent) {
            clearAllHighlights();
        }
    }
}