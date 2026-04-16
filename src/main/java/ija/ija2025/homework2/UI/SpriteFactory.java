package ija.ija2025.homework2.UI;

import ija.ija2025.homework2.common.Unit;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class SpriteFactory {
    public static Node generateSprite(Unit u) {
        boolean useImages = false; // for now only Mocks

        if (useImages) {
            return createImageView(u);
        } else {
            return createCircleMock(u);
        }
    }

    private static Node createCircleMock(Unit u) {
        Color ownerColor = u.getOwner().equalsIgnoreCase("Player1") ? Color.RED : Color.BLUE;

        Circle base = new Circle(18);
        base.setFill(ownerColor);
        base.setStroke(Color.WHITE);
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

}