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

    public void printLen() {
        System.out.println(commandHistory.toArray().length);
    }

    public void undoLastCommand() {
        Command command = commandHistory.pop();
        command.undo();
    }

    public Stack<Command> getCommandHistory() {
        return commandHistory;
    }

    public void setHistory(Stack<Command> history) {
        this.commandHistory = history;
    }
}
