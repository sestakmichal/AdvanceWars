package main.java.ija.ija2025.homework2.common.units;

import main.java.ija.ija2025.homework2.common.TerrainType;
import main.java.ija.ija2025.homework2.common.Unit;

public abstract class Vehicle extends Unit {

    public Vehicle(String type, String unitOwner, int row, int col) {
        super(type, unitOwner, row, col);
    }

    @Override
    public int getMovementPrice(TerrainType terrain) {
        return switch (terrain) {
            case Plain -> 1;
            case Forest -> 2;
            case City -> 1;
            case Factory -> 1;
            case HQ -> 1;
            default -> 999; // impassable
        };
    }

}