/**
 * Authors: Matej Čapek
 * Description: Class for color static values.
 */

package ija.ija2025.project.view;

import javafx.scene.paint.Color;

public class Theme {

    public static final class Basic {
        public static Color BACKGROUND = Color.DARKSLATEGRAY;
        public static Color SELECTION = Color.DARKORANGE;
        public static Color CANCEL = Color.GRAY;
    }

    public static final class Actions {
        public static Color MOVE = Color.GOLD;
        public static Color ATTACK = Color.RED;
        public static Color CAPTURE = Color.DARKORANGE;
        public static Color WAIT = Color.GRAY;
    }

    public static final class Units {
        public static Color INFANTRY = Color.LIMEGREEN;
        public static Color TANK = Color.DARKRED;
        public static Color ARTILLERY = Color.CADETBLUE;
    }
}
