package ija.ija2025.homework2.model.Game;

public enum GameActions {
    ATTACK("Attack"),
    CAPTURE("Capture"),
    WAIT("Wait");

    private final String displayName;

    GameActions(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}