package ija.ija2025.project.model.Observation;

import ija.ija2025.project.model.Events.Event;

public interface Observer {
    void update(Event e);
}
