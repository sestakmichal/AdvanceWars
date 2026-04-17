package ija.ija2025.homework2.model.Units;

import ija.ija2025.homework2.model.Map.TerrainType;

public abstract class Vehicle extends Unit {

    public Vehicle(String unitOwner, int row, int col) {
        super(unitOwner, row, col);
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