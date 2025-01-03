package org.example.projectintern.factory;

// This is the base interface for the Factory Method Pattern
public interface CommandResponseFactory {
    /**
     * Creates a response message based on the provided message text.
     *
     * @param messageText the text of the message from the user.
     * @return the generated response message.
     */
    String createResponse(String messageText);
}
