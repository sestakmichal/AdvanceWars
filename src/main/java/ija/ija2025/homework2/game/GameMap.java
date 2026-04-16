package ija.ija2025.homework2.game;

public enum GameMap {
    Forest_Map(new String[] { "P P F", "F F F" }),
    Mountain_Map(new String[] { "M M P", "P M M" }),

    Big_Map(new String[] {
            "P P P F F F F P P P P P",
            "P P F F F F F F P P P P",
            "P F F F M M F F F P P P",
            "P F F M M M M F F F P P",
            "W W W W W W W W P P P P",
            "P P P P P P W W W P P P",
            "P P P C P P P W W W P P",
            "P P P P P P P P W W W P",
            "M M P P P P P P P W W W",
            "M M M P P P P P P P W W",
    });

    private final String[] layout;
    GameMap(String[] layout) { this.layout = layout; }
    public String[] getLayout() { return layout; }
}
