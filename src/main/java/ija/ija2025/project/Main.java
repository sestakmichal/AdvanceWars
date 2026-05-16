/**
 * Authors: Michal Šesták, Matej Čapek
 * Description: Class serving as the main entry point to the game.
 */

package ija.ija2025.project;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Commands.Command;
import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Game.GameFactory;
import ija.ija2025.project.model.Game.GameSave;
import ija.ija2025.project.view.Elements.MainMenuScreen;
import ija.ija2025.project.view.Observers.MapObserver;
import ija.ija2025.project.view.Observers.UIObserver;
import ija.ija2025.project.view.SceneFactory;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

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
                (p1, p2) -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map1.json"), p1, p2),
                (p1, p2) -> launchGame(stage, GameFactory.createGameFromJson("data/Maps/PreGen/map2.json"), p1, p2),
                (file, p1, p2) -> {
                    GameSave save = GameFactory.loadGameSave(file);
                    launchGame(stage, save.game(), p1, p2, save.history());
                },
                (size, cities, factories, p1, p2) -> {
                    Game game = GameFactory.generateNewGame(size, size, cities, factories);
                    launchGame(stage, game, p1, p2);
                }
        );
        stage.setScene(scene);
        stage.show();
    }

    private void launchGame(Stage stage, Game game, String p1Type, String p2Type) {
        launchGame(stage, game, p1Type, p2Type, null);
    }

    private void launchGame(Stage stage, Game game, String p1Type, String p2Type, List<Command> history) {
        Controller controller = new Controller(game);
        controller.setOnReturnToMenu(() -> showMenu(stage));
        controller.setPlayerType("Player1", p1Type);
        controller.setPlayerType("Player2", p2Type);

        if (history != null) {
            for (Command cmd : history) {
                cmd.setGame(game);
                controller.getCommandManager().addFromLoad(cmd);
            }
        }

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

        controller.checkAndExecuteBotTurn(); // initial bot check
    }
}