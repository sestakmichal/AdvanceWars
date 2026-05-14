/**
 * Authors: Michal Šesták
 * Description: Class for storing and managing the command history.
 */

package ija.ija2025.project.controller;
import ija.ija2025.project.model.Commands.Command;
import java.util.Stack;

public class CommandManager {
    private Stack<Command> commandHistory = new Stack<>();

    public void execute(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    public void executeFromLoad(Command command) {
        commandHistory.push(command);
    }

    public void undoLastCommand() {
        if (commandHistory.isEmpty()) {return;};
        Command command = commandHistory.pop();
        command.undo();
    }

    public Stack<Command> getCommandHistory() {
        return commandHistory;
    }

    public void setCommandHistory(Stack<Command> history) {
        this.commandHistory = history;
    }

    public void printLen() {
        System.out.println(commandHistory.toArray().length);
    }
}
