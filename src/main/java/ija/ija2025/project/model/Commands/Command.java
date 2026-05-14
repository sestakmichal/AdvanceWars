/**
 * Authors: Michal Šesták
 * Description: Class describing the Command type for units.
 */

package ija.ija2025.project.model.Commands;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ija.ija2025.project.model.Game.Game;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = AttackCommand.class, name = "attack"),
        @JsonSubTypes.Type(value = BuyUnitCommand.class, name = "buy"),
        @JsonSubTypes.Type(value = CaptureCommand.class, name = "capture"),
        @JsonSubTypes.Type(value = MoveCommand.class, name = "move"),
        @JsonSubTypes.Type(value = TurnEndCommand.class, name = "turnEnd"),
        @JsonSubTypes.Type(value = WaitCommand.class, name = "wait")
})
public interface Command {
    void execute();
    void undo();

    void setGame(Game game);
    String getType();
}
