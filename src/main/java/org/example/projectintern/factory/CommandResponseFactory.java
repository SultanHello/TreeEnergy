package org.example.projectintern.factory;

// This is the base interface for the Factory Method Pattern
public interface CommandResponseFactory {
    String createResponse(String messageText);
}
