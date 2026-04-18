package ija.ija2025.project.view;

import ija.ija2025.project.controller.Controller;
import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Events.PositionSelectedEvent;
import ija.ija2025.project.model.Events.PositionsHiddenEvent;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Observation.Observer;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class UIObserver implements Observer {
    private final Pane uiLayer; // The JavaFX Pane where the menu sits
    private final Controller controller;
    private ActionMenu currentMenu = null;

    public UIObserver(Controller controller) {
        this.uiLayer = new Pane();;
        this.controller = controller;

        // click passtrough, so the grid can be selected
        this.uiLayer.setPickOnBounds(false);
    }

    @Override
    public void update(Event event) {
        if (event instanceof PositionSelectedEvent(Position position)) {
            showMenu(position);
        } else if (event instanceof PositionsHiddenEvent) {
            hideMenu();
        }
    }

    public Pane getPane() { return uiLayer; }

    private void showMenu(Position pos) {
        hideMenu();

        currentMenu = new ActionMenu(pos, controller, this::hideMenu);

        double x = pos.getCol() * 60;
        double y = pos.getRow() * 60;

        // offset the menu
        currentMenu.setLayoutX(x + 50);
        currentMenu.setLayoutY(y + 10);

        uiLayer.getChildren().add(currentMenu);
        currentMenu.toFront();
    }

    private void hideMenu() {
        if (currentMenu != null) {
            uiLayer.getChildren().remove(currentMenu);
            currentMenu = null;
        }
    }
}