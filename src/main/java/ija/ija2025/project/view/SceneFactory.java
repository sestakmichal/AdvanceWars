package ija.ija2025.project.view;

import ija.ija2025.project.model.Units.Unit;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class SceneFactory {
    public static Scene initializeScene(Stage stage, Node mapGrid, Node uiPane) {
        StackPane contentStack = new StackPane(mapGrid, uiPane);

        Group zoomGroup = new Group(contentStack);

        StackPane centeredWrapper = new StackPane(zoomGroup);
        StackPane.setAlignment(zoomGroup, Pos.CENTER);

        ScrollPane root = new ScrollPane(centeredWrapper);
        root.setFitToWidth(true);
        root.setFitToHeight(true);

        Scene scene = new Scene(root, 850, 650);

        contentStack.scaleXProperty().bind(Bindings.min(
                scene.widthProperty().divide(850),
                scene.heightProperty().divide(650)
        ));
        contentStack.scaleYProperty().bind(contentStack.scaleXProperty());

        return scene;
    }

}