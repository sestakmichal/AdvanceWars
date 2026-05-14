/**
 * Authors: Michal Šesták
 * Description: Class for creating and updating sprites on the map.
 */

package ija.ija2025.project.view;

import ija.ija2025.project.model.Units.Unit;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;


public class SpriteFactory {
    public static final String HEALTH_ARC_ID = "health-arc";

    public static Node generateSprite(Unit u) {
        Node unitVisual = false ? createImageView(u) : createCircleMock(u);

        Node healthArc = createHealthArc(u.getHealth());

        Circle borderRing = new Circle(22);
        borderRing.setFill(Color.WHITE);
        borderRing.setOpacity(0.9);

        StackPane container = new StackPane(borderRing, healthArc, unitVisual);
        container.setAlignment(Pos.CENTER);

        container.getProperties().put("arc_ref", healthArc);

        return container;
    }

    private static Node createCircleMock(Unit u) {
        Color ownerColor = u.getOwner().equalsIgnoreCase("Player1") ? Color.RED : Color.BLUE;

        Circle base = new Circle(16);
        base.setFill(ownerColor);
        base.setStroke(Color.TRANSPARENT); // Color.WHITE
        base.setStrokeWidth(1.5);

        String initial = (u.getType() != null && !u.getType().isEmpty()) ? u.getType().substring(0, 1).toUpperCase() : "?";
        Label label = new Label(initial);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        return new StackPane(base, label);
    }

    private static Node createImageView(Unit u) {
        try {
            String path = "/assets/" + u.getType().toLowerCase() + "_" + u.getOwner().toLowerCase() + ".png";
            Image img = new Image(SpriteFactory.class.getResourceAsStream(path));

            ImageView iv = new ImageView(img);
            iv.setFitWidth(40);
            iv.setFitHeight(40);
            iv.setPreserveRatio(true);
            return iv;
        } catch (Exception e) {
            // image failed to load image, load circle
            return createCircleMock(u);
        }
    }

    private static Node createHealthArc(int currentHp) {
        Circle centerObject = new Circle(20); // anchor at 0,0 - fixes Group bounds
        centerObject.setCenterX(0);
        centerObject.setCenterY(0);
        centerObject.setFill(Color.TRANSPARENT);
        centerObject.setStroke(Color.TRANSPARENT);

        Arc healthArc = new Arc(0, 0, 20, 20, 90, 0);
        healthArc.setType(ArcType.ROUND);
        healthArc.setId(HEALTH_ARC_ID);

        updateHealthArc(healthArc, currentHp);

        return new Group(centerObject, healthArc);
    }

    private static void updateHealthArc(Arc arc, int hp) {
        arc.setLength(hp * -3.6);

        Color hpColor;
        if (hp > 60) {
            hpColor = Color.GREEN;
        } else if (hp > 25) {
            hpColor = Color.GOLD;
        } else {
            hpColor = Color.RED;
        }

        arc.setFill(hpColor.deriveColor(0, 1, 1, 0.8));
        //arc.setStroke(hpColor.darker());
    }

    public static void updateUnitHealth(Node unitNode, int hp) {
        if (unitNode == null) return;

        // Use lookup to find the arc by its ID
        Node found = unitNode.lookup("#" + HEALTH_ARC_ID);

        if (found instanceof Arc healthArc) {
            updateHealthArc(healthArc, hp);
        }
    }
}