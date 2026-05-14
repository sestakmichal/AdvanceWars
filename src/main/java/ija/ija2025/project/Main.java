package ija.ija2025.project;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Game.GameFactory;
import ija.ija2025.project.view.Elements.MainMenuScreen;
import ija.ija2025.project.view.Observers.MapObserver;
import ija.ija2025.project.view.Observers.UIObserver;
import ija.ija2025.project.view.SceneFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

public class Main extends Application {

    private final MainMenuScreen mainMenuScreen = new MainMenuScreen();

    @Override
    public void start(Stage stage) {
        showMenu(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showMenu(Stage stage) {
        Scene scene = mainMenuScreen.createScene(
                stage,
                () -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map1.json")),
                () -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map2.json")),
                file -> launchGame(stage, GameFactory.createGameFromJson(file.getAbsolutePath())),
                (size, cities, factories, p1, p2) -> {
                    Game game = GameFactory.generateNewGame(size, size, cities, factories);
                    launchGame(stage, game);
                }
        );

        stage.setScene(scene);
        stage.show();
    }

    private void launchGame(Stage stage, Game game) {
        Controller controller = new Controller(game);
        controller.setOnReturnToMenu(() -> showMenu(stage));

        MapObserver mapView = new MapObserver(game.getSize());
        UIObserver uiView = new UIObserver(controller);

        game.addObserver(mapView);
        game.addObserver(uiView);

        mapView.initializeMap(controller, game.getMap(), game.getBuildingsMap());
        game.notifyInitialState();

        Scene scene = SceneFactory.initializeScene(stage, mapView.getGrid(), uiView.getPane(), uiView.getStatusBar());

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();
    }
}