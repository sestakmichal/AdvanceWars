package ija.ija2025.project.model.Observation;

public interface Observable {
    void addObserver(Observer gameObserver);
    void removeObserver(Observer gameObserver);
    //void notifyObservers(Event event);
}
