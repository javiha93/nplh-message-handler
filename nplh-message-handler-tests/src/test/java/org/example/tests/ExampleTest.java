package org.example.tests;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.example.client.Clients;
import org.example.client.HL7Client;
import org.example.client.WSClient;
import org.example.configuration.IrisConnectionManager;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.LIS_OML21;
import org.example.domain.hl7.LIS.LISToNPLH.response.ACK.NPLH_ACK;
import org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE.LIS_SCAN_SLIDE;
import org.example.domain.hl7.VTG.NPLHToVTG.VTG_OML21;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.message.Message;
import org.example.domain.message.entity.StainProtocol;
import org.example.domain.ws.UPATHCLOUD.NPLHToUPATHCLOUD.AddCase.UPATHCLOUD_AddCase;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendScannedSlide.UPATHCLOUD_SendScannedSlide;
import org.example.domain.ws.VSS.NPLHToVSS.ProcessOrder.VSS_ProcessOrder;
import org.example.domain.ws.VTGWS.NPLHTpVTGWS.ProcessNewOrder.VTGWS_ProcessNewOrder;
import org.example.domain.ws.WSMessage;
import org.example.server.HL7Server;
import org.example.server.Servers;
import org.example.server.WSServer;
import org.example.service.DuplicateHostService;
import org.example.service.IrisService;
import org.example.service.StainProtocolsService;
import org.junit.jupiter.api.*;

import java.security.SecureRandom;
import java.util.List;

import static org.example.service.IrisService.parseList;

/**
 * Ejemplo de test básico con JUnit 5
 */
@DisplayName("Example Test Suite")
class ExampleTest {

    static StainProtocolsService stainProtocolsService;
    static DuplicateHostService duplicateHostService;

    static Clients clients;
    static Servers servers;
    static Message message;
    static String caseId;

    static HL7Client lisClient;
    static HL7Client vtgClient;
    static WSClient vtgwsClient;
    static WSClient vssClient;
    static WSClient upathCloudClient;

    static HL7Server lisServer;
    static HL7Server vtgServer;
    static WSServer vtgwsServer;
    static WSServer vssServer;
    static WSServer upathCloudServer;

    static StainProtocol advanceStainProtocol;
    static List<String> duplicatedVtgwsHost;

    @BeforeAll
    static void setupAll() {
        IrisConnectionManager irisConnectionManager = new IrisConnectionManager();
        IrisService irisService = new IrisService(irisConnectionManager);
        stainProtocolsService = new StainProtocolsService(irisConnectionManager);
        duplicateHostService = new DuplicateHostService(irisConnectionManager);

        duplicatedVtgwsHost = List.of("VANTAGE WS Oklahoma", "VANTAGE WS Milwaukee");
        duplicateHostService.duplicateHost("VANTAGEWS", duplicatedVtgwsHost);

        HostInfoList hostInfoList = new HostInfoList(parseList(irisService.getHostInfo(), HostInfo.class));
        clients = new Clients(hostInfoList, irisService);
        servers = new Servers(hostInfoList, irisService, clients);

        lisClient = (HL7Client) clients.getClient("LIS_HL7");
        vtgClient = (HL7Client) clients.getClient("VTG");
        vtgwsClient = (WSClient) clients.getClient("VANTAGE WS");
        vssClient = (WSClient) clients.getClient("VSS");
        upathCloudClient = (WSClient) clients.getClient("UPATH CLOUD");

        lisServer = (HL7Server) servers.getServerByName("LIS_HL7");
        vtgServer = (HL7Server) servers.getServerByName("VTG");
        vtgwsServer = (WSServer) servers.getServerByName("VANTAGE WS");
        vssServer = (WSServer) servers.getServerByName("VSS");
        upathCloudServer = (WSServer) servers.getServerByName("UPATH CLOUD");

        irisService.deleteAllMessages();

        //advanceStainProtocol = StainProtocol.Default("1234", "ADV", "VSSprotocol");
        //stainProtocolsService.createStainProtocolAndAssign("VSS", advanceStainProtocol.getNumber(), advanceStainProtocol.getName(), advanceStainProtocol.getDescription());

        duplicatedVtgwsHost = List.of("VANTAGE WS Oklahoma", "VANTAGE WS Milwaukee");
        duplicateHostService.duplicateHost("VANTAGEWS", duplicatedVtgwsHost);
    }

    @BeforeEach
    void setup() {
        caseId = "CASE-" + NanoIdUtils.randomNanoId(new SecureRandom(), NanoIdUtils.DEFAULT_ALPHABET, 13);
        message = Message.Default(caseId);
    }

    @Test
    @DisplayName("NPLH receives from LIS a new message and forwards to VTG")
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
    @DisplayName("NPLH receives from LIS a new message and forwards to VTGWS")
    void testSendFromLISforwardVTGWS() {
        // Arrange
        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Act
        HL7Message response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response, NPLH_ACK.CommunicationOK(oml21.getControlId()));

        // Assert
        WSMessage received = vtgwsServer.waitForObjectMessage(caseId);
        Assertions.assertEquals(received, VTGWS_ProcessNewOrder.FromMessage(message));

        received = ((WSServer) servers.getServerByName("VANTAGE WS Oklahoma")).waitForObjectMessage(caseId);
        Assertions.assertEquals(received, VTGWS_ProcessNewOrder.FromMessage(message));

        received = ((WSServer) servers.getServerByName("VANTAGE WS Milwaukee")).waitForObjectMessage(caseId);
        Assertions.assertEquals(received, VTGWS_ProcessNewOrder.FromMessage(message));
    }

    @Test
    @DisplayName("NPLH receives from LIS a new message with advance protocol and forwards to VSS")
    void testSendFromLISforwardVSS() {
        // Arrange
        message.setStainProtocol(advanceStainProtocol);
        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Act
        HL7Message response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response, NPLH_ACK.CommunicationOK(oml21.getControlId()));

        // Assert
        WSMessage received = vssServer.waitForObjectMessage(caseId);
        Assertions.assertEquals(received, VSS_ProcessOrder.fromMessage(message));
    }

    @Test
    @DisplayName("NPLH receives from LIS a new message scanned by UPATH CLOUD and forwards to UPATH CLOUD")
    void testSendFromLISforwardUPATHCLOUD() {
        // Arrange
        WSMessage sendScannedSlide = UPATHCLOUD_SendScannedSlide.fromMessage(message.getSingleSlide());
        upathCloudClient.send(sendScannedSlide.getSoapAction(), sendScannedSlide.toString());

        HL7Message sendScanReceived = lisServer.waitForObjectMessage(caseId);
        Assertions.assertEquals(sendScanReceived, LIS_SCAN_SLIDE.fromMessage(message.getSingleSlide(), message));

        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Act
        HL7Message response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response, NPLH_ACK.CommunicationOK(oml21.getControlId()));

        // Assert
        WSMessage received = upathCloudServer.waitForObjectMessage(caseId);
        Assertions.assertEquals(received, UPATHCLOUD_AddCase.fromMessage(message));
    }


    @AfterEach
    void tearDown() {
        System.out.println("Tearing down test...");
    }

    @AfterAll
    static void tearDownAll() {

        for (String hostName : duplicatedVtgwsHost) {
            //duplicateHostService.deleteHost(hostName);
        }

        System.out.println("Tearing down test suite...");
        try {
            Thread.sleep(5000); // espera 5 segundos (5000 milisegundos)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restaurar el estado de interrupción
            System.err.println("Sleep was interrupted");
        }
    }
}
