package ija.ija2025.project.model.Commands;

import ija.ija2025.project.model.Map.TerrainType;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.Game;

public class AttackCommand implements Command {
    private Game game;
    private Unit attacker, target;
    private int previousHP;
    private boolean wasDestroyed;

    public AttackCommand(Game game, Unit attacker, Unit target) {
        this.game = game;
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public void execute() {
        AttackResult result = game.attackUnit(attacker, target);
        previousHP = result.previousHP();
        wasDestroyed = result.destroyed();
    }

    @Override
    public void undo() {
        game.reverseAttackUnit(attacker, target, previousHP, wasDestroyed);
    };
}