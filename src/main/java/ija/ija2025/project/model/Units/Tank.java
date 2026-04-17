package ija.ija2025.project.model.Units;

public class Tank extends Vehicle {

    public Tank(String unitOwner, int row, int col) {
        super(unitOwner, row, col);
    }

    @Override
    public int getMoveRange() { return 6; }

    @Override
    public int getDamageValue(Unit fromUnit) {
        return switch (fromUnit) {
            case Artillery a -> 70;
            case Infantry i -> 5;
            case Tank t -> 55;
            case null, default -> 0;
        };
    }

    @Override
    public int getPrice() {
        return 7000;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}