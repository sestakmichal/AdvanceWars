package ija.ija2025.homework2.model.Observation;

import ija.ija2025.homework2.model.Events.Event;

public interface Observer {
    void update(Event e);
}
