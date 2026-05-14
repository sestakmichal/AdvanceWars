/**
 * Authors: Michal Šesták, Matej Čapek
 * Description: Class describing the main menu screen as introduction space to the game.
 */

package ija.ija2025.project.view.Elements;

import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.function.Consumer;

public class MainMenuScreen {

    public interface FileLoadHandler {
        void load(File file, String p1, String p2);
    }

    public interface MapHandler {
        void handle(String p1, String p2);
    }

    public interface GenerateHandler {
        void generate(int size, int cities, int factories, String p1, String p2);
    }

    public Scene createScene(
            Stage stage,
            MapHandler onLoadMap1,
            MapHandler onLoadMap2,
            FileLoadHandler onLoadSaveFile,
            GenerateHandler onGenerate
    ) {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));
        root.setBackground(new Background(
                new BackgroundFill(Color.web("#2c3e50"), CornerRadii.EMPTY, Insets.EMPTY)
        ));

        Label title = new Label("IJA 2025 - ADVANCE WARS");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font(32));

        /// Players
        HBox players = new HBox(20);
        players.setAlignment(Pos.CENTER);

        ComboBox<String> p1 = new ComboBox<>(FXCollections.observableArrayList("Player", "Bot"));
        p1.setValue("Player");

        ComboBox<String> p2 = new ComboBox<>(FXCollections.observableArrayList("Player", "Bot"));
        p2.setValue("Bot");

        players.getChildren().addAll(whiteLabel("P1:"), p1, whiteLabel("P2:"), p2);

        /// Settings
        VBox settings = new VBox(10);
        settings.setAlignment(Pos.CENTER);
        settings.setMaxWidth(300);
        settings.setPadding(new Insets(15));
        settings.setBackground(new Background(
                new BackgroundFill(Color.web("#34495e"), new CornerRadii(10), Insets.EMPTY)
        ));

        Label settingsTitle = new Label("Random Map Settings");
        settingsTitle.setTextFill(Color.LIGHTBLUE);

        Slider size = slider(10, 20, 15);
        Label sizeLabel = whiteLabel("Size: 15x15");
        size.valueProperty().addListener((o, a, b) ->
                sizeLabel.setText("Size: " + b.intValue() + "x" + b.intValue()));

        Slider cities = slider(0, 20, 5);
        Label cityLabel = whiteLabel("Extra Cities: 5");
        cities.valueProperty().addListener((o, a, b) ->
                cityLabel.setText("Extra Cities: " + b.intValue()));

        Slider factories = slider(0, 10, 2);
        Label factoryLabel = whiteLabel("Extra Factories: 2");
        factories.valueProperty().addListener((o, a, b) ->
                factoryLabel.setText("Extra Factories: " + b.intValue()));

        settings.getChildren().addAll(settingsTitle, sizeLabel, size, cityLabel, cities, factoryLabel, factories);

        ///  Buttons
        Button generate = button("Generate & Launch", e ->
                onGenerate.generate(
                        (int) size.getValue(),
                        (int) cities.getValue(),
                        (int) factories.getValue(),
                        p1.getValue(),
                        p2.getValue()
                )
        );

        Button map1 = button("Load Preset Map 1", e -> onLoadMap1.handle(p1.getValue(), p2.getValue()));
        Button map2 = button("Load Preset Map 2", e -> onLoadMap2.handle(p1.getValue(), p2.getValue()));
        Button load = button("Load Save File", e -> openFile(stage, (file) -> onLoadSaveFile.load(file, p1.getValue(), p2.getValue())));
        root.getChildren().addAll(title, players, settings, generate, map1, map2, load);

        return new Scene(root, 800, 600);
    }

    ///  Helpers
    private Label whiteLabel(String text) {
        Label l = new Label(text);
        l.setTextFill(Color.WHITE);
        return l;
    }

    private Slider slider(double min, double max, double value) {
        Slider s = new Slider(min, max, value);
        s.setMajorTickUnit(2);
        s.setMinorTickCount(1);
        s.setBlockIncrement(1);
        s.setSnapToTicks(true);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        return s;
    }

    private Button button(String text, javafx.event.EventHandler<javafx.event.ActionEvent> action) {
        Button b = new Button(text);
        b.setPrefWidth(220);
        b.setFont(Font.font(14));
        b.setOnAction(action);
        return b;
    }

    private void openFile(Stage stage, Consumer<File> handler) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("data/Maps"));
        File f = fc.showOpenDialog(stage);
        if (f != null) handler.accept(f);
    }
}