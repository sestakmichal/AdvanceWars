package ija.ija2025.homework2.tool;

import ija.ija2025.homework2.common.GameEvent;

public interface Observable {
    void addObserver(Observer gameObserver);
    void removeObserver(Observer gameObserver);
    void notifyObservers(GameEvent event);
}
