/**
 * Authors: Michal Šesták, Matej Čapek
 * Description: Class for observing the Map state of the game.
 */

package ija.ija2025.project.view.Observers;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Buildings.Building;
import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Events.Positions.*;
import ija.ija2025.project.model.Events.UnitActions.*;
import ija.ija2025.project.model.Observation.Observer;
import ija.ija2025.project.model.Map.*;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.UnitAbilities;
import ija.ija2025.project.view.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Map;

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

    public void initializeMap(Controller controller, TerrainType[][] map, Map<Position, Building> buildings) {
        Position size = new Position(map.length, map[0].length);

        for (int r = 0; r < size.getRow(); r++) {
            for (int c = 0; c < size.getCol(); c++) {

                Tile tile = new Tile(r, c, map[r][c], 60);

                tile.setOnMouseClicked(e -> controller.handleTileClick(tile.getPos()));

                if (map[r][c].isBuilding()) {
                    tile.updateOwner(buildings.get(new Position(r, c)).getOwner());
                }

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
        }

        if (event instanceof UnitSpawnedEvent(Unit unit, Position pos)) {
            getTile(pos).setUnitVisual(SpriteFactory.generateSprite(unit));
        }

        if (event instanceof UnitHealthChangedEvent(Position pos, int newHealth)) {
            SpriteFactory.updateUnitHealth(getTile(pos).getUnitVisual(), newHealth);
        }

        if (event instanceof UnitDestroyedEvent(Position pos)) {
            getTile(pos).removeUnitVisual();
        }

        if (event instanceof UnitPositionShownEvent(Position pos, Unit unit, UnitAbilities abilities)) {
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

        if (event instanceof UnitHealthChangedEvent(Position pos, int health)) {
            SpriteFactory.updateUnitHealth(getTile(pos).getUnitVisual(), health);
        }

        if (event instanceof BuildingCapturedEvent(Position pos, String newOwner)) {
            Tile tile = getTile(pos);
            tile.updateOwner(newOwner);
        }
    }
}