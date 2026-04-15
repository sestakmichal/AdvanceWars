package ija.ija2025.homework2.tool;

import ija.ija2025.homework2.game.Events.Event;

public interface Observable {
    void addObserver(Observer gameObserver);
    void removeObserver(Observer gameObserver);
    //void notifyObservers(Event event);
}
