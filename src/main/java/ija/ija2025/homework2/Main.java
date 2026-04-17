package ija.ija2025.homework2;

import ija.ija2025.homework2.common.*;
import ija.ija2025.homework2.tool.*;
import ija.ija2025.homework2.game.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {

    private Game game;
    Controller controller;

    @Override
    public void start(Stage stage) {

        game = GameFactory.createGame(GameMap.BIG.getLayout());
        controller = new Controller(game);
        Position size = game.getSize();

        MapObserver mapView = new MapObserver(size);

        game.addObserver(mapView);

        mapView.initializeMap(controller, game.getMap());
        controller.initializeMockUnits(game);

        ScrollPane root = new ScrollPane(mapView.getNode());
        Scene scene = new Scene(root, 850, 650);

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}