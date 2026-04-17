package ija.ija2025.homework2.common.units;
import ija.ija2025.homework2.common.Unit;

public class Artillery extends Vehicle {

    public Artillery(String unitOwner, int row, int col) {
        super(unitOwner, row, col);
    }

    @Override
    public int getMoveRange() { return 5; }

    @Override
    public int getDamageValue(Unit fromUnit) {
        return switch (fromUnit) {
            case Artillery a -> 75;
            case Infantry i -> 15;
            case Tank t -> 70;
            case null, default -> 0;
        };
    }

    @Override
    public int getPrice() {
        return 6000;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}