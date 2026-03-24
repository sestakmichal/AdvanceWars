package main.java.ija.ija2025.homework2.common.units;

import main.java.ija.ija2025.homework2.common.TerrainType;
import main.java.ija.ija2025.homework2.common.Unit;

public class Infantry extends Unit {

    public Infantry(String type, String unitOwner, int x, int y) {
        super(type, unitOwner, x, y);
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
    public int getDamageValue(String fromType) {
        return switch (fromType) {
            case "Artillery" -> 90;
            case "Infantry" -> 55;
            case "Tank" -> 75;
            default -> 0;
        };
    }
}