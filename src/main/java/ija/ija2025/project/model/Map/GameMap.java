package ija.ija2025.project.model.Map;

public enum GameMap {
    FOREST(new String[] { "P P F", "F F F" }),
    MOUNTAIN(new String[] { "M M P", "P M M" }),

    BIG(new String[] {
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
