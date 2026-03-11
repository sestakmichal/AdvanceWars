package ija.ija2025.homework2.common.units;

import ija.ija2025.homework2.common.TerrainType;
import ija.ija2025.homework2.common.Unit;

public class Vehicle extends Unit {

    public Vehicle(String type, String unitOwner, int x, int y) {
        super(type, unitOwner, x, y);
    }

    @Override
    public int getMovementPrice(TerrainType terrain) {
        return switch (terrain) {
            case Plain -> 1;
            case Forest -> 2;
            case City -> 1;
            case Factory -> 1;
            case HQ -> 1;
            default -> 0;
        };
    }

}