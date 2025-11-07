package org.example.tests;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.example.client.Client;
import org.example.client.Clients;
import org.example.client.HL7Client;
import org.example.domain.client.message.ClientMessageResponse;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.LIS_OML21;
import org.example.domain.hl7.VTG.NPLHToVTG.VTG_OML21;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.message.Message;
import org.example.server.HL7Server;
import org.example.server.Servers;
import org.example.service.IrisService;
import org.junit.jupiter.api.*;

import java.security.SecureRandom;
import java.util.Optional;

import static org.example.service.IrisService.parseList;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Ejemplo de test básico con JUnit 5
 */
@DisplayName("Example Test Suite")
class ExampleTest {

    static Clients clients;
    static Servers servers;
    static Message message;
    static String caseId;

    @BeforeAll
    static void setupAll() {
        IrisService irisService = new IrisService();
        HostInfoList hostInfoList = new HostInfoList(parseList(irisService.getHostInfo(), HostInfo.class));
        clients = new Clients(hostInfoList, irisService);
        servers = new Servers(hostInfoList, irisService, clients);

        irisService.deleteAllMessages();
    }

    @BeforeEach
    void setup() {
        caseId = "CASE-" + NanoIdUtils.randomNanoId(new SecureRandom(), NanoIdUtils.DEFAULT_ALPHABET, 13);
        message = Message.Default(caseId);
    }

    @Test
    @DisplayName("Should pass a simple assertion")
    void testSimpleAssertion() {
        // Arrange
        LIS_OML21 oml21 = LIS_OML21.FromMessage(message);
        HL7Client lis = (HL7Client) clients.getClient("LIS");
        HL7Server vtgServer = (HL7Server) servers.getServerByName("VTG");
        
        // Act
        lis.send(oml21.toString(), oml21.getControlId());
        Optional<ClientMessageResponse> response = lis.waitForResponse(oml21.getControlId(), 10_000);
        
        // Assert
        VTG_OML21 vtgOml21 = VTG_OML21.FromMessage(message);
        String messageReceived = vtgServer.waitForMessage(caseId);
        assertEquals(vtgOml21.toString(), messageReceived, "2 + 3 should equal 5");
    }

    @Test
    @DisplayName("Should use AssertJ fluent assertions")
    void testWithAssertJ() {
        // Arrange
        String text = "Hello JUnit 5";
        
        // Assert
        assertThat(text)
            .isNotNull()
            .startsWith("Hello")
            .contains("JUnit")
            .endsWith("5");
    }

    @Test
    @DisplayName("Should handle exceptions")
    void testException() {
        Exception exception = assertThrows(
            IllegalArgumentException.class,
            () -> {
                throw new IllegalArgumentException("Test exception");
            }
        );
        
        assertThat(exception.getMessage()).isEqualTo("Test exception");
    }

    @Disabled("This test is disabled for demonstration")
    @Test
    void testDisabled() {
        fail("This test should not run");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Tearing down test...");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tearing down test suite...");
    }
}
