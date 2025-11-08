package org.example.tests;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.example.client.Clients;
import org.example.client.HL7Client;
import org.example.client.WSClient;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.LIS_OML21;
import org.example.domain.hl7.LIS.LISToNPLH.response.ACK.NPLH_ACK;
import org.example.domain.hl7.LIS.NPLHToLIS.response.ACK.LIS_ACK;
import org.example.domain.hl7.VTG.NPLHToVTG.VTG_OML21;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.ws.VTGWS.VTGWSToNPLH.ProcessNewOrder.VTGWS_ProcessNewOrder;
import org.example.domain.ws.WSMessage;
import org.example.server.HL7Server;
import org.example.server.Servers;
import org.example.server.WSServer;
import org.example.service.IrisService;
import org.junit.jupiter.api.*;

import java.security.SecureRandom;

import static org.example.service.IrisService.parseList;

/**
 * Ejemplo de test básico con JUnit 5
 */
@DisplayName("Example Test Suite")
class ExampleTest {

    static Clients clients;
    static Servers servers;
    static Message message;
    static String caseId;

    static HL7Client lisClient;
    static HL7Client vtgClient;
    static WSClient vtgwsClient;

    static HL7Server lisServer;
    static HL7Server vtgServer;
    static WSServer vtgwsServer;

    @BeforeAll
    static void setupAll() {
        IrisService irisService = new IrisService();
        HostInfoList hostInfoList = new HostInfoList(parseList(irisService.getHostInfo(), HostInfo.class));
        clients = new Clients(hostInfoList, irisService);
        servers = new Servers(hostInfoList, irisService, clients);

        lisClient = (HL7Client) clients.getClient("LIS_HL7");
        vtgClient = (HL7Client) clients.getClient("VTG");
        vtgwsClient = (WSClient) clients.getClient("VANTAGE WS");

        lisServer = (HL7Server) servers.getServerByName("LIS_HL7");
        vtgServer = (HL7Server) servers.getServerByName("VTG");
        vtgwsServer = (WSServer) servers.getServerByName("VANTAGE WS");

        irisService.deleteAllMessages();
    }

    @BeforeEach
    void setup() {
        caseId = "CASE-" + NanoIdUtils.randomNanoId(new SecureRandom(), NanoIdUtils.DEFAULT_ALPHABET, 13);
        message = Message.Default(caseId);
    }

    @Test
    @DisplayName("NPLH receives from LIS and forwards to VTG")
    void testSendFromLISforwardVTG() {
        // Arrange
        HL7Message oml21 = LIS_OML21.fromMessage(message);
        
        // Act
        HL7Message response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response, NPLH_ACK.CommunicationOK(oml21.getControlId()));
        
        // Assert
        HL7Message received = vtgServer.waitForObjectMessage(caseId);
        Assertions.assertEquals(received, VTG_OML21.fromMessage(message));
    }

    @Test
    @DisplayName("NPLH receives from LIS and forwards to VTGWS")
    void testSendFromLISforwardVTGWS() {
        // Arrange
        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Act
        HL7Message response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response, NPLH_ACK.CommunicationOK(oml21.getControlId()));

        // Assert
        WSMessage received = vtgwsServer.waitForObjectMessage(caseId);
        WSMessage expected = VTGWS_ProcessNewOrder.FromMessage(message);
        Assertions.assertEquals(received, expected);
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
