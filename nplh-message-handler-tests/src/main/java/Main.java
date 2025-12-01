import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.example.client.Clients;
import org.example.client.HL7Client;
import org.example.client.WSClient;
import org.example.configuration.IrisConnectionManager;
import org.example.domain.client.message.HL7ResponseMessage;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.LIS_OML21;
import org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE.VTG_SlideUpdate;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.StainProtocol;
import org.example.domain.server.message.WSResponseMessage;
import org.example.domain.ws.VSS.VSSToNPLH.UpdateSlideStatus.VSS_UpdateSlideStatus;
import org.example.domain.ws.WSMessage;
import org.example.server.HL7Server;
import org.example.server.Servers;
import org.example.server.WSServer;
import org.example.service.DuplicateHostService;
import org.example.service.IrisService;
import org.example.service.StainProtocolsService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.example.service.IrisService.parseList;

public class Main {

    static IrisConnectionManager irisConnectionManager;
    static IrisService irisService;

    static Clients clients;
    static Servers servers;

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

    static DuplicateHostService duplicateHostService;
    static StainProtocolsService stainProtocolsService;

    static List<String> duplicatedVssHost;

    static StainProtocol advanceStainProtocol;

    public static void main(String[] args) throws InterruptedException {

        beforeAll();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(50);
        int casesHour = 200;
        long intervalSeconds = 3600L / casesHour;
        int slidesPerCase = 10;

        scheduler.scheduleAtFixedRate(() -> {
            try {
                String localCaseId ="CASE-" + NanoIdUtils.randomNanoId(new SecureRandom(), NanoIdUtils.DEFAULT_ALPHABET, 13);
                Message localMessage = Message.Default(localCaseId);
                stainingFlow(localMessage, localCaseId, slidesPerCase);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, intervalSeconds, TimeUnit.SECONDS);

        try {
            new CountDownLatch(1).await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Stopping test");
        }

    }

    public static void stainingFlow(Message localMessage, String localCaseId, int slidesPerCase) throws InterruptedException {
        localMessage.getOrder().getBlock().addSlide(slidesPerCase - 1);
        localMessage.setStainProtocol(advanceStainProtocol);
        HL7Message oml21 = LIS_OML21.fromMessage(localMessage);

        // Send OML21 LIS
        HL7ResponseMessage response = lisClient.sendWaitingHL7Response(localCaseId, oml21.toString(), oml21.getControlId());
        //Assertions.assertEquals(response.getMessage(), NPLH_LIS_ACK.CommunicationOK(oml21.getControlId()));

        sleep(1_000);

        // Send SlideUpdate VTG
        for (Slide slide : localMessage.getAllSlides()) {
            HL7Message vtgSlideUpdate = VTG_SlideUpdate.fromMessage(localMessage, slide, "PRINTED");
            slide.setLabelPrinted(LocalDateTime.now().toString());
            response = vtgClient.sendWaitingHL7Response(localCaseId, vtgSlideUpdate.toString(), vtgSlideUpdate.getControlId());
            //Assertions.assertEquals(response.getMessage(), NPLH_VTG_ACK.CommunicationOK(vtgSlideUpdate.getControlId()));
        }

        sleep(1_000);

        //Send Staining from VSS
        for (Slide slide : localMessage.getAllSlides()) {
            WSMessage vssSlideUpdate = VSS_UpdateSlideStatus.FromSlide(localMessage, slide, "SlideStaining");
            WSResponseMessage wsResponse = vssClient.sendWaitingWSResponse(vssSlideUpdate.getSoapAction(), vssSlideUpdate.toString());
            //Assertions.assertEquals(wsResponse.getMessage(), CommunicationResponse.FromSoapActionOk(vssSlideUpdate.getSoapAction()));
        }

        sleep(1_000);

        //Send Stained from VSS
        for (Slide slide : localMessage.getAllSlides()) {
            WSMessage vssSlideUpdate = VSS_UpdateSlideStatus.FromSlide(localMessage, slide, "SlideStained");
            WSResponseMessage wsResponse = vssClient.sendWaitingWSResponse(vssSlideUpdate.getSoapAction(), vssSlideUpdate.toString());
            //Assertions.assertEquals(wsResponse.getMessage(), CommunicationResponse.FromSoapActionOk(vssSlideUpdate.getSoapAction()));
        }
    }


    static void beforeAll() throws InterruptedException {
        beforeAllBase();
        beforeAllDuplicateHost();
        beforeAllAssignAdvanceProtocol();
    }

    static void beforeAllBase() throws InterruptedException {
        irisConnectionManager = new IrisConnectionManager();
        irisService = new IrisService(irisConnectionManager);

        duplicateHostService = new DuplicateHostService(irisConnectionManager);

        duplicatedVssHost = List.of("VSS Denver", "VSS Seattle", "VSS Atlanta", "VSS Colorado", "VSS Cincinnati");
        duplicateHostService.duplicateHost("VSS", duplicatedVssHost);

        HostInfoList hostInfoList = new HostInfoList(parseList(irisService.getHostInfo(), HostInfo.class));
        clients = new Clients(hostInfoList, irisService);
        servers = new Servers(hostInfoList, irisService, clients);

        sleep(1000);

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
    }

    static void beforeAllDuplicateHost() {

    }

    static void beforeAllAssignAdvanceProtocol() {
        stainProtocolsService = new StainProtocolsService(irisConnectionManager);

        advanceStainProtocol = StainProtocol.Default("1234", "ADV", "VSSprotocol");
        stainProtocolsService.createStainProtocolAndAssign("VSS", advanceStainProtocol.getNumber(), advanceStainProtocol.getName(), advanceStainProtocol.getDescription());
        for (String hostName : duplicatedVssHost) {
            stainProtocolsService.createStainProtocolAndAssign(hostName, advanceStainProtocol.getNumber(), advanceStainProtocol.getName(), advanceStainProtocol.getDescription());
        }
    }
}
