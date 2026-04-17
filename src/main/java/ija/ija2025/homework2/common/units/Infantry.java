package ija.ija2025.homework2.common.units;

import ija.ija2025.homework2.common.TerrainType;
import ija.ija2025.homework2.common.Unit;

public class Infantry extends Unit {

    public Infantry(String unitOwner, int x, int y) {
        super(unitOwner, x, y);
    }

    @Override
    public int getMovementPrice(TerrainType terrain) {
        return switch (terrain) {
            case Plain -> 1;
            case Forest -> 1;
            case Mountain -> 2;
            case City -> 1;
            case Factory -> 1;
            case HQ -> 1;
            default -> 999; // impassable
        };
    }

    @Override
    public int getMoveRange() { return 3; }

    @Override
    public int getDamageValue(Unit fromUnit) {
        return switch (fromUnit) {
            case Artillery a -> 90;
            case Infantry i -> 55;
            case Tank t -> 75;
            case null, default -> 0;
        };
    }

    @Override
    public int getPrice() {
        return 1000;
    }

    @Override
    public String getType() {
        return this.getClass().getSimpleName();
    }
}