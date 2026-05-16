package ija.ija2025.project.model.Game;

public class UnitAbilities {
    boolean move;
    boolean attack;
    boolean capture;

    public UnitAbilities(boolean move, boolean attack, boolean capture) {
        this.move = move;
        this.attack = attack;
        this.capture = capture;
    }

    public boolean canMove() { return move; }
    public boolean canAttack() { return attack; }
    public boolean canCapture() { return capture; }
}
