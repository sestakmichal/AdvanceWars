package ija.ija2025.project.controller;

import ija.ija2025.project.model.Commands.Command;

import java.util.Stack;

public class CommandManager {
    Stack<Command> commandHistory = new Stack<>();

    public void execute(Command command) {
        command.execute();
        commandHistory.push(command);
    }

    public void undoLastCommand() {
        Command command = commandHistory.pop();
        command.undo();
    }
}
