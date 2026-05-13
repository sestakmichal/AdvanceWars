package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.Game;

public class MoveCommand implements Command {
    private Position from, to;
    private Unit unit;
    private Game game;

    public MoveCommand(Game game, Position from, Position to) {
        this.game = game;
        this.from = from;
        this.to = to;
        this.unit = game.getUnitAt(from); //unused now
    }

    @Override
    public void execute() {game.moveUnit(from, to);}

    @Override
    public void undo() {game.reverseMoveUnit(to, from);}
}