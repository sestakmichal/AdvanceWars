package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ija.ija2025.project.model.Map.Position;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.Game;

public class MoveCommand implements Command {
    private Position from, to;
    private Unit unit;

    @JsonIgnore
    private Game game;

    public MoveCommand() {}

    public MoveCommand(Game game, Position from, Position to) {
        this.game = game;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() { if (game != null) game.moveUnit(from, to); }

    @Override
    public void undo() { if (game != null) game.reverseMoveUnit(to, from); }

    public Position getFrom() { return from; }
    public Position getTo() { return to; }

    @Override
    public void setGame(Game game) { this.game = game; }
    @Override
    public String getType() {
        return "move";
    }
}