package ija.ija2025.homework2;

import ija.ija2025.homework2.model.Map.GameMap;
import ija.ija2025.homework2.model.Map.Position;
import ija.ija2025.homework2.model.Game.Game;
import ija.ija2025.homework2.model.Game.GameFactory;
import ija.ija2025.homework2.controller.*;

import ija.ija2025.homework2.view.MapObserver;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;


public class Main extends Application {

    private Game game;
    GameController gameController;

    @Override
    public void start(Stage stage) {

        game = GameFactory.createGame(GameMap.BIG.getLayout());
        gameController = new GameController(game);
        Position size = game.getSize();

        MapObserver mapView = new MapObserver(size);

        game.addObserver(mapView);

        mapView.initializeMap(gameController, game.getMap());
        gameController.initializeMockUnits(game);

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