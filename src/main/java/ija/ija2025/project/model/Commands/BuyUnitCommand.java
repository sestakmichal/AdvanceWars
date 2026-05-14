package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Game.Game;
import ija.ija2025.project.model.Map.Position;

public class BuyUnitCommand implements Command {
    private Game game;
    private String type;
    private Position position;
    private int price;

    public BuyUnitCommand(Game game, String type, Position position, int price) {
        this.game = game;
        this.type = type;
        this.position = position;
        this.price = price;
    }

    @Override
    public void execute() {game.buyUnit(type, position, price);}

    @Override
    public void undo() {game.reverseBuyUnit(type, position, price);}
}