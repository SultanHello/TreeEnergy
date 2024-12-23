package org.example.projectintern;

import org.example.projectintern.config.TelegramBotConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = TelegramBotConfig.class)
public class ProjectInternApplicationTests {
    @Test
    void contextLoads() {
        // Тесты
    }
}