package ija.ija2025.project.model.Events;
import ija.ija2025.project.model.Map.Position;

import java.util.List;

public record ReachablePositionsShownEvent(List<Position> positions) implements Event {}