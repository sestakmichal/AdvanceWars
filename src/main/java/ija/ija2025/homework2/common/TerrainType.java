package main.java.ija.ija2025.homework2.common;

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
            // case 'c' -> City;
            // case 'f' -> Factory;
            // case 'h' -> HQ;
            default -> throw new IllegalStateException("Unexpected value: " + terrain);
        };
    }
}