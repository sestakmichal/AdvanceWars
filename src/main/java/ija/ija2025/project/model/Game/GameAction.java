/**
 * Authors: Matej Čapek
 * Description: Enum describing the specific action unit can take.
 */

package ija.ija2025.project.model.Game;

public enum GameAction {
    MOVE("Move"),
    ATTACK("Attack"),
    CAPTURE("Capture"),
    WAIT("Wait");

    private final String displayName;

    GameAction(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {return displayName;}

}