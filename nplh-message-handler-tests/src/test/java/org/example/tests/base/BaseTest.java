package org.example.tests.base;

import org.example.service.IrisService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase base para todos los tests.
 * 
 * Proporciona:
 * - IrisService (mock por defecto, puede ser real si se configura)
 * - Logger compartido
 * - Setup común para todos los tests
 * - Métodos helper utilities
 * 
 * Para usar IrisService REAL:
 * - Sobrescribe createIrisService() en tu clase de test
 * - Asegúrate de que IRIS esté disponible y accesible
 * 
 * Uso:
 * <pre>
 * {@code
 * @DisplayName("My Test Suite")
 * class MyTest extends BaseTest {
 *     
 *     @Test
 *     void myTest() {
 *         // Usa irisService directamente
 *         HostInfoList hosts = irisService.getHostInfoList();
 *         logger.info("Test running...");
 *     }
 * }
 * }
 * </pre>
 */
public abstract class BaseTest {

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    /**
     * IrisService disponible en todos los tests.
     * Por defecto es un Mock, sobrescribe createIrisService() para usar uno real.
     */
    protected static IrisService irisService;

    /**
     * Inicialización una sola vez antes de todos los tests de la clase.
     * Se ejecuta antes del primer test.
     */
    @BeforeAll
    public static void setUpAll() {

        logger.info("=".repeat(100));
        logger.info("Inicializando entorno de tests - BaseTest");
        logger.info("=".repeat(100));

    }

    /**
     * Se ejecuta antes de cada test individual.
     * Útil para reset de estado si es necesario.
     */
    @BeforeEach
    public void setUp() {
        logger.debug("Preparando test...");
    }

    /**
     * Crea una instancia de IrisService.
     * 
     * Por defecto retorna MockIrisService (no requiere IRIS real).
     * Sobrescribe este método en tu test para usar IrisService real:
     * 
     * <pre>
     * {@code
     * @Override
     * protected static IrisService createIrisService() {
     *     return new IrisService(); // Conexión real a IRIS
     * }
     * }
     * </pre>
     */

    /**
     * Utility method para pausar la ejecución (útil para debugging)
     */
    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Utility method para imprimir un separador visual en los logs
     */
    protected void logSeparator() {
        logger.info("-".repeat(80));
    }

    /**
     * Utility method para imprimir un título destacado en los logs
     */
    protected void logTitle(String title) {
        logger.info("=".repeat(80));
        logger.info(title);
        logger.info("=".repeat(80));
    }
}
