package ija.ija2025.homework2.model.Map;

public enum TerrainType {
    Plain,
    Forest,
    Mountain,
    Water,
    City,
    Factory,
    HQ;

    public static TerrainType fromChar(char terrain) {
        return switch (terrain) {
            case 'p' -> Plain;
            case 'f' -> Forest;
            case 'm' -> Mountain;
            case 'w' -> Water;
            // implement later?
            case 'c' -> City;
            // case 'f' -> Factory;
            // case 'h' -> HQ;
            default -> throw new IllegalStateException("Unexpected value: " + terrain);
        };
    }

    public int defenseBonus() {
        return switch (this) {
            case Plain -> 1;
            case Forest -> 2;
            case Mountain -> 4;
            case City, Factory -> 3;
            case HQ -> 4;
            case Water -> 0;
        };
    }

    public boolean isBuilding() {
        return switch (this) {
            case City, Factory, HQ -> true;
            default -> false;
        };
    }
}