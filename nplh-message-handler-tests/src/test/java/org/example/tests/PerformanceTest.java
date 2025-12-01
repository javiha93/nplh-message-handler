package org.example.tests;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.example.domain.client.message.HL7ResponseMessage;
import org.example.domain.hl7.HL7Message;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.LIS_OML21;
import org.example.domain.hl7.LIS.LISToNPLH.response.ACK.NPLH_LIS_ACK;
import org.example.domain.hl7.VTG.VTGToNPLH.SLIDEUPDATE.VTG_SlideUpdate;
import org.example.domain.hl7.VTG.VTGToNPLH.response.ACK.NPLH_VTG_ACK;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.StainProtocol;
import org.example.domain.server.message.WSResponseMessage;
import org.example.domain.ws.VSS.NPLHToVSS.ProcessOrder.VSS_ProcessOrder;
import org.example.domain.ws.VSS.VSSToNPLH.UpdateSlideStatus.VSS_UpdateSlideStatus;
import org.example.domain.ws.VSS.VSSToNPLH.response.CommunicationResponse;
import org.example.domain.ws.WSMessage;
import org.example.server.WSServer;
import org.example.service.DuplicateHostService;
import org.example.service.StainProtocolsService;
import org.junit.jupiter.api.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.example.service.IrisService.parseList;

public class PerformanceTest extends BaseTest {

    DuplicateHostService duplicateHostService;
    StainProtocolsService stainProtocolsService;

    List<String> duplicatedVssHost;

    static StainProtocol advanceStainProtocol;

    @BeforeAll
    void beforeAll() {
        beforeAllDuplicateHost();
        beforeAllAssignAdvanceProtocol();
    }

    void beforeAllDuplicateHost() {
        duplicateHostService = new DuplicateHostService(irisConnectionManager);

        duplicatedVssHost = List.of("VSS Denver", "VSS Seattle", "VSS Atlanta", "VSS Colorado", "VSS Cincinnati");
        duplicateHostService.duplicateHost("VSS", duplicatedVssHost);

        HostInfoList hostInfoList = new HostInfoList(parseList(irisService.getHostInfo(), HostInfo.class));
        clients.updateClients(hostInfoList);
        servers.updateServers(hostInfoList);
    }

    void beforeAllAssignAdvanceProtocol() {
        stainProtocolsService = new StainProtocolsService(irisConnectionManager);

        advanceStainProtocol = StainProtocol.Default("1234", "ADV", "VSSprotocol");
        stainProtocolsService.createStainProtocolAndAssign("VSS", advanceStainProtocol.getNumber(), advanceStainProtocol.getName(), advanceStainProtocol.getDescription());
        for (String hostName : duplicatedVssHost) {
            stainProtocolsService.createStainProtocolAndAssign(hostName, advanceStainProtocol.getNumber(), advanceStainProtocol.getName(), advanceStainProtocol.getDescription());
        }
    }

    @AfterAll
    void afterAllDeleteDuplicateHost() {
        for (String hostName : duplicatedVssHost) {
            duplicateHostService.deleteHost(hostName);
        }
    }

    @Test
    @DisplayName("Performance PathGroup")
    void testPerformancePathGroup() throws InterruptedException {

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

        long durationMinutes = 6000;

        try {
            System.out.println("Starting performance simulation. Running for " + durationMinutes + " minutes...");
            TimeUnit.MINUTES.sleep(durationMinutes);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Stopping test simulation due to interruption.");
        } finally {
            // 3. Detener el scheduler y esperar su terminación para una salida limpia
            scheduler.shutdownNow();
            if (!scheduler.awaitTermination(30, TimeUnit.SECONDS)) {
                System.err.println("Scheduler did not terminate gracefully.");
            }
        }
    }

    public void stainingFlow(Message localMessage, String localCaseId, int slidesPerCase) throws InterruptedException {
        localMessage.getOrder().getBlock().addSlide(slidesPerCase - 1);
        localMessage.setStainProtocol(advanceStainProtocol);
        HL7Message oml21 = LIS_OML21.fromMessage(localMessage);

        // Send OML21 LIS
        HL7ResponseMessage response = lisClient.sendWaitingHL7Response(localCaseId, oml21.toString(), oml21.getControlId());
        Assertions.assertEquals(response.getMessage(), NPLH_LIS_ACK.CommunicationOK(oml21.getControlId()));

        sleep(1_000);

        // Send SlideUpdate VTG
        for (Slide slide : localMessage.getAllSlides()) {
            HL7Message vtgSlideUpdate = VTG_SlideUpdate.fromMessage(localMessage, slide, "PRINTED");
            slide.setLabelPrinted(LocalDateTime.now().toString());
            response = vtgClient.sendWaitingHL7Response(localCaseId, vtgSlideUpdate.toString(), vtgSlideUpdate.getControlId());
            Assertions.assertEquals(response.getMessage(), NPLH_VTG_ACK.CommunicationOK(vtgSlideUpdate.getControlId()));
        }

        sleep(1_000);

        //Send Staining from VSS
        for (Slide slide : localMessage.getAllSlides()) {
            WSMessage vssSlideUpdate = VSS_UpdateSlideStatus.FromSlide(localMessage, slide, "SlideStaining");
            WSResponseMessage wsResponse = vssClient.sendWaitingWSResponse(vssSlideUpdate.getSoapAction(), vssSlideUpdate.toString());
            Assertions.assertEquals(wsResponse.getMessage(), CommunicationResponse.FromSoapActionOk(vssSlideUpdate.getSoapAction()));
        }

        sleep(1_000);

        //Send Stained from VSS
        for (Slide slide : localMessage.getAllSlides()) {
            WSMessage vssSlideUpdate = VSS_UpdateSlideStatus.FromSlide(localMessage, slide, "SlideStained");
            WSResponseMessage wsResponse = vssClient.sendWaitingWSResponse(vssSlideUpdate.getSoapAction(), vssSlideUpdate.toString());
            Assertions.assertEquals(wsResponse.getMessage(), CommunicationResponse.FromSoapActionOk(vssSlideUpdate.getSoapAction()));
        }
    }
}
