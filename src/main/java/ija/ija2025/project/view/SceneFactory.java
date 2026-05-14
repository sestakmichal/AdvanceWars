/**
 * Authors: Michal Šesták, Matej Čapek
 * Description: Class for creating the initial visual scene.
 */

package ija.ija2025.project.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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

        // drag to move
        double[] lastMouse = new double[2];
        double dragSensitivity = 2.0;
        gameScrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if (e.isPrimaryButtonDown()) {
                lastMouse[0] = e.getSceneX();
                lastMouse[1] = e.getSceneY();
            }
        });
        gameScrollPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            if (e.isPrimaryButtonDown()) {
                double deltaX = e.getSceneX() - lastMouse[0];
                double deltaY = e.getSceneY() - lastMouse[1];

                double contentWidth = gameScrollPane.getContent().getBoundsInLocal().getWidth();
                double contentHeight = gameScrollPane.getContent().getBoundsInLocal().getHeight();

                double curH = gameScrollPane.getHvalue();
                double curV = gameScrollPane.getVvalue();

                gameScrollPane.setHvalue(curH - (deltaX * dragSensitivity) / contentWidth);
                gameScrollPane.setVvalue(curV - (deltaY * dragSensitivity) / contentHeight);

                lastMouse[0] = e.getSceneX();
                lastMouse[1] = e.getSceneY();

                e.consume();
            }
        });

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