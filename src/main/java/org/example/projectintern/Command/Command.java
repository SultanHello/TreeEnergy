package org.example.projectintern.Command;

import java.io.IOException;

// This is an interface for all commands, in the implementations you can see the implementation of the Command Pattern
public interface Command {
    /**
     * Executes the command logic based on the given input.
     *
     * @param command the command text input from the user (e.g., "/addElement").
     * @param chatId  the ID of the chat where the command is executed.
     * @return the result of executing the command (could be a response message or data).
     * @throws IOException if an error occurs during command execution (e.g., file handling).
     */
    Object execute(String command, Long chatId) throws IOException;
}
