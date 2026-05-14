package ija.ija2025.project.model.Commands;
import ija.ija2025.project.model.Commands.Results.AttackResult;
import ija.ija2025.project.model.Units.Unit;
import ija.ija2025.project.model.Game.Game;

public class AttackCommand implements Command {
    private Game game;
    private Unit attacker, target;
    private int previousTargetHP;
    private int previousAttackerHP;
    private boolean targetDestroyed;
    private boolean attackerDestroyed;

    public AttackCommand(Game game, Unit attacker, Unit target) {
        this.game = game;
        this.attacker = attacker;
        this.target = target;
    }

    @Override
    public void execute() {
        AttackResult result = game.attackUnit(attacker, target);
        previousTargetHP = result.hpBefore();
        previousAttackerHP = result.attackerHpBefore();
        targetDestroyed = result.destroyed();
        attackerDestroyed = result.attackerDestroyed();
    }

    @Override
    public void undo() {
        game.reverseAttackUnit(attacker, target, previousTargetHP, previousAttackerHP, targetDestroyed, attackerDestroyed);
    }
}