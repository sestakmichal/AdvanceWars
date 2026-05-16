/**
 * Authors: Michal Šesták
 * Description: Class describing tile as part of the map space.
 */

package ija.ija2025.project.view;

import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Map.TerrainType;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Tile extends StackPane {
    private final Position pos;
    private final Rectangle background = new Rectangle(50, 50);
    private final Rectangle highlight = new Rectangle(46, 46);
    private Label ownerLabel = new Label("");
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

        // ownership label
        if (type.isBuilding()) {
            ownerLabel = new Label("N");
            ownerLabel.setFont(Font.font("Arial", FontWeight.BOLD, size * 0.4));
            ownerLabel.setTextFill(Color.rgb(255, 255, 255, 0.3));
            StackPane.setAlignment(ownerLabel, javafx.geometry.Pos.BOTTOM_RIGHT);
            ownerLabel.setPadding(new javafx.geometry.Insets(0, 5, 2, 0));
        }

        this.getChildren().addAll(background, ownerLabel, highlight);
    }

    public void setUnitVisual(Node visualNode) {
        removeUnitVisual();

        if (visualNode != null) {
            this.currentUnitNode = visualNode;
            int index = this.getChildren().indexOf(highlight);
            this.getChildren().add(index, visualNode);
        }
    }

    public void removeUnitVisual() {
        if (currentUnitNode != null) {
            this.getChildren().remove(currentUnitNode);
            currentUnitNode = null;
        }
    }

    public void updateOwner(String owner) {
        if ("Player1".equals(owner)) {
            ownerLabel.setText("R");
            ownerLabel.setTextFill(Color.rgb(255, 0, 0, 0.5));
        }
        else if ("Player2".equals(owner)) {
            ownerLabel.setText("B");
            ownerLabel.setTextFill(Color.rgb(0, 0, 255, 0.5));
        }
        else {
            ownerLabel.setText("N");
            ownerLabel.setTextFill(Color.rgb(255, 255, 255, 0.4));
        }
    }

    public void setHighlight() {
        highlight.setStroke(Theme.Basic.SELECTION);
        highlight.setVisible(true);
    }

    public void setMovementHighlight() {
        highlight.setStroke(Theme.Actions.MOVE);
        highlight.setVisible(true);
    }

    public void setAttackHighlight() {
        highlight.setStroke(Theme.Actions.ATTACK);
        highlight.setVisible(true);
    }

    public void removeHighlight() {
        highlight.setVisible(false);
    }

    private Color getTerrainColor(TerrainType type) {
        return switch (type) {
            case Plain -> Theme.Terrain.PLAIN;
            case Forest -> Theme.Terrain.FOREST;
            case Mountain -> Theme.Terrain.MOUNTAIN;
            case Water -> Theme.Terrain.WATER;
            case HQ -> (pos.equals(new Position(1, 1))) ? Theme.Terrain.HQP1 : Theme.Terrain.HQP2;
            case Factory -> Theme.Terrain.FACTORY;
            case City -> Theme.Terrain.CITY;
        };
    }

    public Node getUnitVisual() { return this.currentUnitNode; }
    public Position getPos() { return pos; }
}