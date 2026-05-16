/**
 * Authors: Michal Šesták
 * Description: Class for storing and managing the command history.
 */

package ija.ija2025.project.controller;
import ija.ija2025.project.model.Commands.Command;
import java.util.Stack;

public class CommandManager {
    private Stack<Command> commandHistory = new Stack<>();
    private Stack<Command> redoHistory = new Stack<>();

    public void execute(Command command) {
        command.execute();
        commandHistory.push(command);
        redoHistory.clear();
    }

    public void addFromLoad(Command command) {
        commandHistory.push(command);
    }

    public void undoLastCommand() {
        if (commandHistory.isEmpty()) {return;};
        Command command = commandHistory.pop();
        command.undo();
        redoHistory.push(command);
        System.out.println(command.toString());
    }

    public void redoLastCommand() {
        if (redoHistory.isEmpty()) {return;};
        Command command = redoHistory.pop();
        command.execute();
        commandHistory.push(command);
        System.out.println(command.toString());
    }

    public Stack<Command> getCommandHistory() {
        return commandHistory;
    }
    public void setCommandHistory(Stack<Command> history) {
        this.commandHistory = history;
    }
}