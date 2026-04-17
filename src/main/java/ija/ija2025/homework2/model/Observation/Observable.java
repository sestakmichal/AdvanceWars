package ija.ija2025.homework2.model.Observation;

public interface Observable {
    void addObserver(Observer gameObserver);
    void removeObserver(Observer gameObserver);
    //void notifyObservers(Event event);
}
