package ija.ija2025.project.view;

import ija.ija2025.project.model.Units.Unit;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class SceneFactory {
    public static Scene initializeScene(Stage stage, Node mapGrid, Node uiPane, Node statusBar) {
        StackPane contentStack = new StackPane(mapGrid, uiPane);

        Group zoomGroup = new Group(contentStack);

        StackPane centeredWrapper = new StackPane(zoomGroup);
        StackPane.setAlignment(zoomGroup, Pos.CENTER);

        ScrollPane gameScrollPane = new ScrollPane(centeredWrapper);
        gameScrollPane.setFitToWidth(true);
        gameScrollPane.setFitToHeight(true);

        VBox root = new VBox();
        root.getChildren().addAll(statusBar, gameScrollPane);

        VBox.setVgrow(gameScrollPane, Priority.ALWAYS);

        StackPane screenStack = new StackPane(root);

        Scene scene = new Scene(screenStack, 850, 700);

        contentStack.scaleXProperty().bind(Bindings.min(
                scene.widthProperty().divide(850),
                scene.heightProperty().divide(650)
        ));
        contentStack.scaleYProperty().bind(contentStack.scaleXProperty());

        return scene;
    }

}