/**
 * Authors: Matej Čapek
 * Description: Event for handling UI updates.
 */

package ija.ija2025.project.model.Events.Positions;
import ija.ija2025.project.model.Events.Event;
import ija.ija2025.project.model.Map.Position;

import java.util.List;

public record MovablePositionsShownEvent(List<Position> positions) implements Event {}