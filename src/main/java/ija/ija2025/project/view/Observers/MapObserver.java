package ija.ija2025.project.view.Observers;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Events.*;
import ija.ija2025.project.model.Map.*;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.view.SpriteFactory;
import ija.ija2025.project.view.Theme;
import ija.ija2025.project.view.Tile;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.util.List;

public class MapObserver implements Observer {
    private final GridPane grid;
    private final Tile[][] tiles;
    private Tile selectedTile = null;

    public MapObserver(Position size) {
        this.grid = new GridPane();

        this.tiles = new Tile[size.getRow()][size.getCol()];

        grid.setBackground(new Background(new BackgroundFill(Theme.Basic.BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY)));
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

        if (event instanceof UnitAttackedEvent(Unit attacker, Unit target, int damage)) {
            Position targetPos = target.getPosition();

            SpriteFactory.updateUnitHealth(getTile(targetPos).getUnitVisual(), target.getHealth());
        }

        if (event instanceof UnitDestroyedEvent(Unit target)) {
            getTile(target.getPosition()).removeUnitVisual();
        }

            if (event instanceof UnitPositionShownEvent(Position pos, Unit unit)) {
                getTile(pos).setHighlight();
            }

        if (event instanceof MovablePositionsShownEvent(List<Position> positions)) {
            for (Position position : positions) {
                getTile(position).setMovementHighlight();
            }
        }

        if (event instanceof AttackablePositionsShownEvent(List<Position> positions)) {
            for (Position position : positions) {
                getTile(position).setAttackHighlight();
            }
        }

        if (event instanceof PositionsHiddenEvent) {
            clearAllHighlights();
        }
    }
}