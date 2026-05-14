package ija.ija2025.project.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
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

        DoubleBinding scale = Bindings.createDoubleBinding(() -> {
            double viewportWidth = gameScrollPane.getViewportBounds().getWidth();
            double viewportHeight = gameScrollPane.getViewportBounds().getHeight();
            double contentWidth = contentStack.getBoundsInLocal().getWidth();
            double contentHeight = contentStack.getBoundsInLocal().getHeight();

            if (contentWidth == 0 || contentHeight == 0) return 1.0;

            return Math.min(viewportWidth / contentWidth, viewportHeight / contentHeight);
            }, gameScrollPane.viewportBoundsProperty(), contentStack.boundsInLocalProperty()
        );

        contentStack.scaleXProperty().bind(scale);
        contentStack.scaleYProperty().bind(scale);

        return scene;
    }

}