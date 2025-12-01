package org.example.tests;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.example.client.Clients;
import org.example.client.HL7Client;
import org.example.client.WSClient;
import org.example.configuration.IrisConnectionManager;
import org.example.domain.host.HostInfo;
import org.example.domain.host.HostInfoList;
import org.example.domain.message.Message;
import org.example.server.HL7Server;
import org.example.server.Servers;
import org.example.server.WSServer;
import org.example.service.IrisService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import java.security.SecureRandom;

import static java.lang.Thread.sleep;
import static org.example.service.IrisService.parseList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseTest {
    IrisConnectionManager irisConnectionManager;
    IrisService irisService;

    Clients clients;
    Servers servers;

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

    Message message;
    String caseId;

    @BeforeAll
    void beforeAllBase() {
        irisConnectionManager = new IrisConnectionManager();
        irisService = new IrisService(irisConnectionManager);

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
    }

    @BeforeEach
    void setup() {
        caseId = "CASE-" + NanoIdUtils.randomNanoId(new SecureRandom(), NanoIdUtils.DEFAULT_ALPHABET, 13);
        message = Message.Default(caseId);
    }

    @AfterAll
    void afterAllBase() {
        try {
            sleep(5_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep was interrupted");
        }
    }
}
