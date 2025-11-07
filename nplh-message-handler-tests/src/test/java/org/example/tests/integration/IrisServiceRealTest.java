package org.example.tests.integration;

import org.example.service.IrisService;
import org.example.tests.base.BaseTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ejemplo de test que usa IrisService REAL.
 * 
 * IMPORTANTE: 
 * - Requiere que IRIS esté disponible en localhost:1972
 * - Requiere credenciales correctas (_system/CONNECT)
 * - Los tests están @Disabled por defecto
 * - Quita @Disabled cuando IRIS esté disponible
 * 
 * Para habilitar estos tests:
 * 1. Asegúrate de que IRIS está corriendo
 * 2. Verifica las credenciales en IrisService
 * 3. Quita @Disabled de los tests
 */
@DisplayName("IrisService Real Connection Test")
@Disabled("Requiere IRIS real corriendo - habilitar manualmente cuando sea necesario")
class IrisServiceRealTest extends BaseTest {

    /**
     * Sobrescribir para usar IrisService REAL en lugar de Mock
     */

    @Test
    @DisplayName("Should connect to real IRIS instance")
    void testRealIRISConnection() {
        assertThat(irisService).isNotNull();
        logger.info("✓ Conectado a IRIS real");
        
        // Verificar que es IrisService real, no mock
        assertThat(irisService.getClass().getSimpleName())
            .isEqualTo("IrisService");
    }


    @Test
    @DisplayName("Should get real installation path from IRIS")
    void testGetRealInstallationPath() {
        String path = irisService.getInstallationPath();
        
        assertThat(path).isNotNull();
        assertThat(path).isNotEqualTo("C:\\Mock\\Installation\\Path");
        
        logger.info("IRIS Installation path: {}", path);
    }

}
