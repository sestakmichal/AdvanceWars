package ija.ija2025.homework2;

import ija.ija2025.homework2.common.Position;
import ija.ija2025.homework2.common.Unit;
import ija.ija2025.homework2.game.Game;
import ija.ija2025.homework2.game.GameFactory;
import ija.ija2025.homework2.UI.MapController;
import ija.ija2025.homework2.UI.Tile;
import ija.ija2025.homework2.UI.SpriteFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class Main extends Application {

    private Game game;
    private MapController controller;
    private Tile[][] visualTiles;
    private int TileSize = 60; // pixels

    private final String[] mapDefinition = {
            "P P P F F F F P P P P P",
            "P P F F F F F F P P P P",
            "P F F F M M F F F P P P",
            "P F F M M M M F F F P P",
            "W W W W W W W W P P P P",
            "P P P P P P W W W P P P",
            "P P P C P P P W W W P P",
            "P P P P P P P P W W W P",
            "M M P P P P P P P W W W",
            "M M M P P P P P P P W W",
    };

    @Override
    public void start(Stage stage) {

        game = GameFactory.createGame(mapDefinition);
        Position size = game.getSize();

        visualTiles = new Tile[size.getRow()][size.getCol()];
        this.controller = new MapController(game, visualTiles);

        GridPane grid = new GridPane();
        grid.setStyle("-fx-background-color: #333;");
        grid.setHgap(1);
        grid.setVgap(1);

        for (int r = 0; r < size.getRow(); r++) {
            for (int c = 0; c < size.getCol(); c++) {
                Tile tile = new Tile(r, c, game.getTerrainAt(new Position(r, c)), TileSize);

                tile.setOnMouseClicked(e -> controller.handleInteraction(tile));

                visualTiles[r][c] = tile;
                grid.add(tile, c, r);
            }
        }

        // add mock units
        Unit u = game.createUnit("Tank", "Player1", 0, 0);
        visualTiles[0][0].setUnitVisual(SpriteFactory.generateSprite(u.getType(), u.getOwner()));
        u = game.createUnit("Infantry", "Player1", 0, 1);
        visualTiles[0][1].setUnitVisual(SpriteFactory.generateSprite(u.getType(), u.getOwner()));
        u = game.createUnit("Artillery", "Player1", 0, 2);
        visualTiles[0][2].setUnitVisual(SpriteFactory.generateSprite(u.getType(), u.getOwner()));

        u = game.createUnit("Tank", "Player2", 1, 9);
        visualTiles[1][9].setUnitVisual(SpriteFactory.generateSprite(u.getType(), u.getOwner()));
        u = game.createUnit("Infantry", "Player2", 1, 10);
        visualTiles[1][10].setUnitVisual(SpriteFactory.generateSprite(u.getType(), u.getOwner()));
        u = game.createUnit("Artillery", "Player2", 1, 11);
        visualTiles[1][11].setUnitVisual(SpriteFactory.generateSprite(u.getType(), u.getOwner()));

        ScrollPane root = new ScrollPane(grid);
        Scene scene = new Scene(root, 850, 650);

        stage.setTitle("IJA 2025");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}