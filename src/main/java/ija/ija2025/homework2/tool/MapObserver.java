package ija.ija2025.homework2.tool;

import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.game.Events.Event;
import ija.ija2025.homework2.game.Events.UnitMovedEvent;
import ija.ija2025.homework2.common.Position;
import javafx.scene.Node;
import java.util.List;

public class MapObserver implements Observer {
    private final Tile[][] visualTiles;

    public MapObserver(Tile[][] visualTiles) {
        this.visualTiles = visualTiles;
    }
    public void update(Event e) {

        if (e instanceof UnitMovedEvent event) {

            Position from = event.from();
            Position to = event.to();

            Tile source = visualTiles[from.getRow()][from.getCol()];
            Tile target = visualTiles[to.getRow()][to.getCol()];

            Node sprite = source.getUnitVisual();

            source.removeUnitVisual();
            target.setUnitVisual(sprite);
        }
    }
}