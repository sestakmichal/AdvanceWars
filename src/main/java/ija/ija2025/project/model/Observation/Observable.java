/**
 * Authors: Matej Čapek
 * Description: Enum describing the type of terrain for a tile.
 */

package ija.ija2025.project.model.Observation;

public interface Observable {
    void addObserver(Observer gameObserver);
    void removeObserver(Observer gameObserver);
}
