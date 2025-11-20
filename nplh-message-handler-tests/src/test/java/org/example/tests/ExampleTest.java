package org.example.tests;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.example.client.Clients;
import org.example.client.HL7Client;
import org.example.client.WSClient;
import org.example.configuration.IrisConnectionManager;
import org.example.domain.client.message.HL7ResponseMessage;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.LIS_OML21;
import org.example.domain.hl7.LIS.LISToNPLH.response.ACK.NPLH_LIS_ACK;
import org.example.domain.hl7.LIS.NPLHToLIS.SCAN_SLIDE.LIS_SCAN_SLIDE;
import org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE.LIS_SLIDE_UPDATE;
import org.example.domain.hl7.VTG.NPLHToVTG.OML21.VTG_OML21;
import org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE.VTG_SlideUpdate;
import org.example.domain.hl7.VTG.VTGToNPLH.response.ACK.NPLH_VTG_ACK;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.StainProtocol;
import org.example.domain.server.message.WSResponseMessage;
import org.example.domain.ws.UPATHCLOUD.NPLHToUPATHCLOUD.AddCase.UPATHCLOUD_AddCase;
import org.example.domain.ws.UPATHCLOUD.UPATHCLOUDToNPLH.SendScannedSlide.UPATHCLOUD_SendScannedSlide;
import org.example.domain.ws.VSS.NPLHToVSS.ProcessOrder.VSS_ProcessOrder;
import org.example.domain.ws.VSS.VSSToNPLH.UpdateSlideStatus.VSS_UpdateSlideStatus;
import org.example.domain.ws.VSS.VSSToNPLH.response.CommunicationResponse;
import org.example.domain.ws.VTGWS.NPLHTpVTGWS.ProcessNewOrder.VTGWS_ProcessNewOrder;
import org.example.domain.ws.VTGWS.NPLHTpVTGWS.ProcessStainingStatusUpdate.VTGWS_ProcessStainingStatusUpdate;
import org.example.domain.ws.WSMessage;
import org.example.server.HL7Server;
import org.example.server.Servers;
import org.example.server.WSServer;
import org.example.service.DuplicateHostService;
import org.example.service.IrisService;
import org.example.service.StainProtocolsService;
import org.junit.jupiter.api.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Thread.sleep;
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
    static List<String> duplicatedVssHost;

    @BeforeAll
    static void setupAll() {
        IrisConnectionManager irisConnectionManager = new IrisConnectionManager();
        IrisService irisService = new IrisService(irisConnectionManager);
        stainProtocolsService = new StainProtocolsService(irisConnectionManager);
        duplicateHostService = new DuplicateHostService(irisConnectionManager);

//        duplicatedVtgwsHost = List.of("VANTAGE WS Oklahoma", "VANTAGE WS Milwaukee");
//        duplicatedVtgwsHost = List.of("VANTAGE WS Denver", "VANTAGE WS Seattle");
//        duplicateHostService.duplicateHost("VANTAGEWS", duplicatedVtgwsHost);

        duplicatedVssHost = List.of("VSS Denver", "VSS Seattle", "VSS Atlanta", "VSS Colorado", "VSS Cincinnati");
        duplicateHostService.duplicateHost("VSS", duplicatedVssHost);

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

        advanceStainProtocol = StainProtocol.Default("1234", "ADV", "VSSprotocol");
        stainProtocolsService.createStainProtocolAndAssign("VSS", advanceStainProtocol.getNumber(), advanceStainProtocol.getName(), advanceStainProtocol.getDescription());

        for (String hostName : duplicatedVssHost) {
            stainProtocolsService.createStainProtocolAndAssign(hostName, advanceStainProtocol.getNumber(), advanceStainProtocol.getName(), advanceStainProtocol.getDescription());
        }

//        duplicatedVtgwsHost = List.of("VANTAGE WS Oklahoma", "VANTAGE WS Milwaukee");
//        duplicatedVtgwsHost = List.of("VANTAGE WS Denver", "VANTAGE WS Seattle");
//        duplicateHostService.duplicateHost("VANTAGEWS", duplicatedVtgwsHost);
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
        HL7ResponseMessage response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response.getMessage(), NPLH_LIS_ACK.CommunicationOK(oml21.getControlId()));
        
        // Assert
        HL7Message received = vtgServer.waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(received, VTG_OML21.fromMessage(message));
    }

    @Test
    @DisplayName("NPLH receives from LIS a new message and forwards to VTGWS")
    void testSendFromLISforwardVTGWS() {
        // Arrange
        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Act
        HL7ResponseMessage response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response.getMessage(), NPLH_LIS_ACK.CommunicationOK(oml21.getControlId()));

        // Assert
        WSMessage received = vtgwsServer.waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(received, VTGWS_ProcessNewOrder.FromMessage(message));

        received = ((WSServer) servers.getServerByName("VANTAGE WS Oklahoma")).waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(received, VTGWS_ProcessNewOrder.FromMessage(message));

        received = ((WSServer) servers.getServerByName("VANTAGE WS Milwaukee")).waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(received, VTGWS_ProcessNewOrder.FromMessage(message));
    }

    @Test
    @DisplayName("NPLH receives from LIS a new message with advance protocol and forwards to VSS")
    void testSendFromLISforwardVSS() {
        // Arrange
        message.setStainProtocol(advanceStainProtocol);
        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Act
        HL7ResponseMessage response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response.getMessage(), NPLH_LIS_ACK.CommunicationOK(oml21.getControlId()));

        // Assert
        WSMessage received = vssServer.waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(received, VSS_ProcessOrder.fromMessage(message, "NewOrder"));
    }

    @Test
    @DisplayName("NPLH receives from LIS a new message scanned by UPATH CLOUD and forwards to UPATH CLOUD")
    void testSendFromLISforwardUPATHCLOUD() {
        // Arrange
        WSMessage sendScannedSlide = UPATHCLOUD_SendScannedSlide.fromMessage(message.getSingleSlide());
        WSResponseMessage wsResponse = upathCloudClient.sendWaitingWSResponse(sendScannedSlide.getSoapAction(), sendScannedSlide.toString());

        HL7Message sendScanReceived = lisServer.waitForObjectMessage(wsResponse.getSentTime(), caseId);
        Assertions.assertEquals(sendScanReceived, LIS_SCAN_SLIDE.fromMessage(message.getSingleSlide(), message));

        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Act
        HL7ResponseMessage response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response.getMessage(), NPLH_LIS_ACK.CommunicationOK(oml21.getControlId()));

        // Assert
        WSMessage received = upathCloudServer.waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(received, UPATHCLOUD_AddCase.fromMessage(message));
    }

    @Test
    @DisplayName("Test")
    void testPp() {
        Slide slide = message.getSingleSlide();
        WSMessage vssSlideUpdate = VSS_UpdateSlideStatus.FromSlide(message, slide, "SlideStaining");

        WSResponseMessage wsResponse = vssClient.sendWaitingWSResponse(vssSlideUpdate.getSoapAction(), vssSlideUpdate.toString());
        Assertions.assertEquals(wsResponse.getMessage(), CommunicationResponse.FromSoapActionOk(vssSlideUpdate.getSoapAction()));

        WSMessage vtgwsStainingUpdate = vtgwsServer.waitForObjectMessage(wsResponse.getSentTime(), slide.getId() + "<");
    }

    @Test
    @DisplayName("Performance PathGroup")
    void testPerformancePathGroup() {
        // Arrange
        message.getOrder().getBlock().addSlide(9);
        message.setStainProtocol(advanceStainProtocol);
        HL7Message oml21 = LIS_OML21.fromMessage(message);

        // Send OML21 LIS
        HL7ResponseMessage response = lisClient.sendWaitingHL7Response(caseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response.getMessage(), NPLH_LIS_ACK.CommunicationOK(oml21.getControlId()));

        // Receive OML21 VTG
        HL7Message vtgReceived = vtgServer.waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(vtgReceived, VTG_OML21.fromMessage(message));

        //Receive ProcessOrder VSS
        WSMessage vssProcessOrder = vssServer.waitForObjectMessage(response.getSentTime(), caseId);
        Assertions.assertEquals(vssProcessOrder, VSS_ProcessOrder.fromMessage(message, "NewOrder"));
        for (String vssServer : duplicatedVssHost) {
            vssProcessOrder =  ((WSServer) servers.getServerByName(vssServer)).waitForObjectMessage(response.getSentTime(), caseId);
            Assertions.assertEquals(vssProcessOrder, VSS_ProcessOrder.fromMessage(message, "NewOrder"));
        }

        // Send SlideUpdate VTG
        Map<Slide, LocalDateTime> vtgPrintedUpdates = new HashMap<>();
        for (Slide slide : message.getAllSlides()) {
            HL7Message vtgSlideUpdate = VTG_SlideUpdate.fromMessage(message, slide, "PRINTED");
            slide.setLabelPrinted(LocalDateTime.now().toString());
            response = vtgClient.sendWaitingHL7Response(caseId, vtgSlideUpdate.toString(), vtgSlideUpdate.getControlId());
            Assertions.assertEquals(response.getMessage(), NPLH_VTG_ACK.CommunicationOK(vtgSlideUpdate.getControlId()));

            vtgPrintedUpdates.put(slide, response.getSentTime());
        }
        Map<Slide, LocalDateTime> sortedUpdates = new TreeMap<>(
                Comparator.comparing(vtgPrintedUpdates::get)
        );
        sortedUpdates.putAll(vtgPrintedUpdates);

        // Receive ProcessOrder VSS
        for (Map.Entry<Slide, LocalDateTime> entry : sortedUpdates.entrySet()) {
            Slide slide = entry.getKey();
            LocalDateTime sentTime = entry.getValue();

            vssProcessOrder = vssServer.waitForObjectMessage(sentTime, slide.getId() + "<");
            Assertions.assertEquals(vssProcessOrder, VSS_ProcessOrder.fromMessage(message, slide, "ChangeOrder"));

            for (String vssServer : duplicatedVssHost) {
                vssProcessOrder =  ((WSServer) servers.getServerByName(vssServer)).waitForObjectMessage(response.getSentTime(), slide.getId() + "<");
                Assertions.assertEquals(vssProcessOrder, VSS_ProcessOrder.fromMessage(message, slide, "ChangeOrder"));
            }
        }

        // Receive SlideUpdate LIS
        for (Map.Entry<Slide, LocalDateTime> entry : sortedUpdates.entrySet()) {
            Slide slide = entry.getKey();
            LocalDateTime sentTime = entry.getValue();

            HL7Message slideUpdate = lisServer.waitForObjectMessage(sentTime, slide.getId());
            Assertions.assertEquals(slideUpdate, LIS_SLIDE_UPDATE.fromMessage(message, slide, "PRINTED"));

        }

        //Send Staining from VSS
        Map<Slide, LocalDateTime> vssStainingUpdates = new HashMap<>();
        for (Slide slide : message.getAllSlides()) {
            WSMessage vssSlideUpdate = VSS_UpdateSlideStatus.FromSlide(message, slide, "SlideStaining");
            //slide.setLabelPrinted(LocalDateTime.now().toString());
            WSResponseMessage wsResponse = vssClient.sendWaitingWSResponse(vssSlideUpdate.getSoapAction(), vssSlideUpdate.toString());
            Assertions.assertEquals(wsResponse.getMessage(), CommunicationResponse.FromSoapActionOk(vssSlideUpdate.getSoapAction()));

            vssStainingUpdates.put(slide, response.getSentTime());
        }

        sortedUpdates = new TreeMap<>(
                Comparator.comparing(vssStainingUpdates::get)
        );
        sortedUpdates.putAll(vssStainingUpdates);

        //Receive StainingStatusUpdate VTG
        for (Map.Entry<Slide, LocalDateTime> entry : sortedUpdates.entrySet()) {
            Slide slide = entry.getKey();
            LocalDateTime sentTime = entry.getValue();

            WSMessage vtgwsStainingUpdate = vtgwsServer.waitForObjectMessage(sentTime, slide.getId() + "<");
            Assertions.assertEquals(vtgwsStainingUpdate, VTGWS_ProcessStainingStatusUpdate.fromSlide(message, slide, "STAINING"));
        }
    }


    @AfterEach
    void tearDown() {
        System.out.println("Tearing down test...");
    }

    @AfterAll
    static void tearDownAll() {

        System.out.println("Tearing down test suite...");
        try {
            sleep(10_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep was interrupted");
        }

//        for (String hostName : duplicatedVtgwsHost) {
//            duplicateHostService.deleteHost(hostName);
//        }

        for (String hostName : duplicatedVssHost) {
            duplicateHostService.deleteHost(hostName);
        }
    }
}
