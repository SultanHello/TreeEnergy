package org.example.projectintern.repository;

import org.example.projectintern.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByNameAndChatId(String name, Long chatId);
    List<Category> findByParentIsNullAndChatId(Long chatId);
}
