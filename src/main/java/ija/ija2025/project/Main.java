package ija.ija2025.project;

import ija.ija2025.project.model.Map.GameMap;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Game.GameFactory;
import ija.ija2025.project.controller.*;

import ija.ija2025.project.view.Observers.MapObserver;
import ija.ija2025.project.view.Observers.UIObserver;
import ija.ija2025.project.view.SceneFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private Game game;
    Controller controller;

    @Override
    public void start(Stage stage) {

        game = GameFactory.createGame(GameMap.BIG.getLayout());
        controller = new Controller(game);

        MapObserver mapView = new MapObserver(game.getSize());
        UIObserver UIView = new UIObserver(controller);

        game.addObserver(mapView);
        game.addObserver(UIView);

        mapView.initializeMap(controller, game.getMap());

        Scene scene = SceneFactory.initializeScene(stage, mapView.getGrid(), UIView.getPane());

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();

        controller.initializeMockUnits(game);

    }

    public static void main(String[] args) {
        launch();
    }
}