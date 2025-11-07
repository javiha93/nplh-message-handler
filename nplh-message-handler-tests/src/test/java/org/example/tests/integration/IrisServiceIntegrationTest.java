package org.example.tests.integration;

import org.example.tests.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de ejemplo mostrando cómo usar IrisService en tests.
 * 
 * Por defecto usa MockIrisService (no requiere IRIS real).
 * Para usar IRIS real, sobrescribe createIrisService().
 */
@DisplayName("IrisService Integration Test")
class IrisServiceIntegrationTest extends BaseTest {

    @Test
    @DisplayName("Should have access to IrisService instance")
    void testIrisServiceAccess() {
        // IrisService está disponible gracias a BaseTest
        assertThat(irisService).isNotNull();
        
        logger.info("IrisService tipo: {}", irisService.getClass().getSimpleName());
        logger.info("IrisService disponible para usar en tests");
    }


    @Test
    @DisplayName("Should check TCP connection status")
    void testCheckTCPConnectionStatus() {
        String connectionId = "TEST_CONNECTION";
        
        boolean status = irisService.checkTCPConnectionStatus(connectionId);
        
        logger.info("TCP Connection status para '{}': {}", connectionId, status);
        // Con MockIrisService siempre retorna false
        assertThat(status).isFalse();
    }

    @Test
    @DisplayName("Should enable TCP connection")
    void testEnableTCPConnection() {
        String serverName = "TEST_SERVER";
        String connectionId = "TEST_CONNECTION";
        
        // Con MockIrisService esto no hace nada real
        irisService.enableTCPConnection(serverName, connectionId);
        
        logger.info("TCP Connection habilitado para servidor: {}, conexión: {}", 
            serverName, connectionId);
    }

    @Test
    @DisplayName("Should get installation path")
    void testGetInstallationPath() {
        String path = irisService.getInstallationPath();
        
        assertThat(path).isNotNull();
        logger.info("Installation path: {}", path);
    }

    @Test
    @DisplayName("Should delete all messages")
    void testDeleteAllMessages() {
        // Con MockIrisService esto no hace nada real
        irisService.deleteAllMessages();
        
        logger.info("Mensajes eliminados (mock)");
    }
}
