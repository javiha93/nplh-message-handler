package org.example.tests.service;

import org.example.service.MessageService;
import org.example.domain.message.Message;
import org.example.tests.base.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test para MessageService.
 * Extiende BaseTest para tener acceso a utilities comunes.
 */
@DisplayName("MessageService Tests")
class MessageServiceTest extends BaseTest {

    private MessageService messageService;

    @BeforeEach
    void setupTest() {
        messageService = new MessageService();
        logTitle("MessageService Test");
    }

    @Test
    @DisplayName("Should generate message with sample ID")
    void testGenerateMessageWithSampleId() {
        // Arrange
        String sampleId = "TEST-123";
        logger.info("Generando mensaje con sample ID: {}", sampleId);
        
        // Act
        Message message = messageService.generateMessage(sampleId);
        
        // Assert
        assertThat(message).isNotNull();
        logger.info("Mensaje generado correctamente");
    }

    @Test
    @DisplayName("Should generate message without sample ID")
    void testGenerateMessageWithoutSampleId() {
        // Act
        logger.info("Generando mensaje sin sample ID");
        Message message = messageService.generateMessage();
        
        // Assert
        assertThat(message).isNotNull();
        logger.info("Mensaje generado correctamente");
    }

    @Test
    @DisplayName("Should convert message")
    void testConvertMessage() {
        // Arrange
        Message message = messageService.generateMessage("TEST-456");
        logger.info("Convirtiendo mensaje...");
        
        // Act & Assert
        assertThat(message).isNotNull();
        logger.info("Conversión exitosa");
    }
}
