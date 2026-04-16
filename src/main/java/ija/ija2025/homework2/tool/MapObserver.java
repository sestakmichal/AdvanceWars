package ija.ija2025.homework2.tool;

import ija.ija2025.homework2.UI.SpriteFactory;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.game.Events.HighlightTilesEvent;
import ija.ija2025.homework2.game.Events.Event;
import ija.ija2025.homework2.game.Events.UnitMovedEvent;
import ija.ija2025.homework2.game.Events.UnitSpawnedEvent;
import ija.ija2025.homework2.common.Position;

import javafx.scene.Node;

public class MapObserver implements Observer {
    private Tile[][] uiTiles;

    public MapObserver(Tile[][] uiTiles) { this.uiTiles = uiTiles; }

    public void update(Event e) {
        if (e instanceof UnitMovedEvent event) {
            Position from = event.from();
            Position to = event.to();

            Tile source = uiTiles[from.getRow()][from.getCol()];
            Tile target = uiTiles[to.getRow()][to.getCol()];

            Node sprite = source.getUnitVisual();

            source.removeUnitVisual();
            target.setUnitVisual(sprite);
        }
        else if (e instanceof UnitSpawnedEvent event) {
            Position pos = event.unit().getPosition();
            uiTiles[pos.getRow()][pos.getCol()].setUnitVisual(SpriteFactory.generateSprite(event.unit()));
        }
        else if (e instanceof HighlightTilesEvent event) {
            for (Position p : event.tileCoordinates()) {
                uiTiles[p.getRow()][p.getCol()].setHighlight(event.highlight());
            }
        }
    }
}