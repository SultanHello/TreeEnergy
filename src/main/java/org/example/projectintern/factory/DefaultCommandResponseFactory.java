package org.example.projectintern.factory;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// Factory Method Pattern is used here
@Component
public class DefaultCommandResponseFactory implements CommandResponseFactory {
    private static final String ALLRULES = """
            Команды, которые доступны в боте:
            
            1) /viewTree - 📜 Просмотреть текущее дерево категорий.
            
            2) /addElement <название элемента> - ➕ Добавить новый корневой элемент.
            
            3) /addElement <родитель> <дочерний элемент> - ➕ Добавить дочерний элемент к существующему родителю.
            
            4) /removeElement <название элемента> - 🗑️ Удалить категорию и всех её потомков.
            
            5) /help - ℹ️ Список доступных команд.""";

    private static final String START = """
            Приветствую вас в Category Bot! 👋
            Здесь вы можете легко управлять деревом категорий.
            Введите "/help", чтобы ознакомиться с возможностями бота.
            """;

    private static final String UNKNOWN =
            "❌ Команда не распознана. Напишите /help, чтобы увидеть список доступных команд.";

    private final Map<String, String> predefinedResponses = new HashMap<>();

    public DefaultCommandResponseFactory() {
        predefinedResponses.put("/start", START);
        predefinedResponses.put("/help", ALLRULES);

    }

    @Override
    public String createResponse(String messageText) {
        if (predefinedResponses.containsKey(messageText)) {
            return predefinedResponses.get(messageText);
        }

        return UNKNOWN;
    }
}
