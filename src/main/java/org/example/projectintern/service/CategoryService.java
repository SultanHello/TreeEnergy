package org.example.projectintern.service;


import org.example.projectintern.model.Category;

import java.util.List;

public interface CategoryService {
    String addRoot(String name, Long chatId);
    String addChild(String name, String child, Long chatId);
    String remove(String name, Long chatId);
    boolean categoryExists(String name, Long chatId);
    List<Category> findByParentIsNullAndChatId(Long chatId);
    String viewCategoryTree(Long chatId);
}
