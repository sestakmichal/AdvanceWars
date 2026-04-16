package ija.ija2025.homework2.tool;

import ija.ija2025.homework2.UI.SpriteFactory;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.common.*;
import ija.ija2025.homework2.game.*;
import ija.ija2025.homework2.game.Events.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;


public class MapObserver implements Observer {

    private final GridPane grid;
    private final Tile[][] tiles;


    public MapObserver(Position size) {
        this.grid = new GridPane();
        this.tiles = new Tile[size.getRow()][size.getCol()];

        grid.setStyle("-fx-background-color: #333;");
        grid.setHgap(1);
        grid.setVgap(1);
    }

    public void initialize(Controller controller, TerrainType[][] map) {
        Position size = new Position(map.length, map[0].length);

        for (int r = 0; r < size.getRow(); r++) {
            for (int c = 0; c < size.getCol(); c++) {

                Tile tile = new Tile(r, c, map[r][c], 60);

                int finalR = r;
                int finalC = c;
                tile.setOnMouseClicked(e ->controller.onTileClicked(new Position(finalR, finalC)));

                tiles[r][c] = tile;
                grid.add(tile, c, r);
            }
        }
    }

    public GridPane getNode() {
        return grid;
    }


    @Override
    public void update(Event e) {
        if (e instanceof UnitMovedEvent event) {

            Position from = event.from();
            Position to = event.to();

            Tile source = tiles[from.getRow()][from.getCol()];
            Tile target = tiles[to.getRow()][to.getCol()];

            Node sprite = source.getUnitVisual();

            source.removeUnitVisual();
            target.setUnitVisual(sprite);
        }

        if (e instanceof UnitSpawnedEvent event) {

            Position pos = event.position();
            Unit unit = event.unit();

            Tile tile = tiles[pos.getRow()][pos.getCol()];

            tile.setUnitVisual(SpriteFactory.generateSprite(unit));
        }
    }
}