package ija.ija2025.homework2.UI;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.TerrainType;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {
    private final Position pos;
    private final Rectangle background = new Rectangle(50, 50);
    private final Rectangle highlight = new Rectangle(46, 46);
    private Node currentUnitNode = null;

    public Tile(int row, int col, TerrainType type, int size) {
        pos = new Position(row, col);

        // background color
        background.setWidth(size);
        background.setHeight(size);
        background.setFill(getTerrainColor(type));
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(0.5);

        // highlight when selected
        highlight.setWidth(size - 4);
        highlight.setHeight(size - 4);
        highlight.setFill(Color.TRANSPARENT);
        highlight.setStroke(Color.YELLOW);
        highlight.setStrokeWidth(3);
        highlight.setVisible(false);

        this.getChildren().addAll(background, highlight);
    }

    public void setUnitVisual(Node visualNode) {
        removeUnitVisual(); // Always clear the existing one first

        if (visualNode != null) {
            this.currentUnitNode = visualNode;
            this.getChildren().add(visualNode); // Add to the StackPane
        }
    }

    public void removeUnitVisual() {
        if (currentUnitNode != null) {
            this.getChildren().remove(currentUnitNode);
            currentUnitNode = null;
        }
    }

    public void setHighlight() {
        highlight.setStroke(Color.ORANGE);
        highlight.setVisible(true);
    }

    public void setHighlightReach() {
        highlight.setStroke(Color.BLUE);
        highlight.setVisible(true);
    }

    public void removeHighlight() {
        highlight.setVisible(false);
    }

    public boolean isHighlighted() { return highlight.isVisible(); }

    private Color getTerrainColor(TerrainType type) {
        return switch (type) {
            case Plain -> Color.PALEGREEN;
            case Forest -> Color.FORESTGREEN;
            case Mountain -> Color.SADDLEBROWN;
            case Water -> Color.DODGERBLUE;
            case City, Factory, HQ -> Color.DARKGRAY;
        };
    }

    public Node getUnitVisual() { return this.currentUnitNode; }
    public Position getPos() { return pos; }
}
