package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISConnection;
import org.example.configuration.IrisConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class IrisService {
    static final Logger logger = LoggerFactory.getLogger(IrisService.class);

    private final IrisConnectionManager connectionManager;

    public IrisService(IrisConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        logger.info("✅ IrisService initialized using shared connection");
    }

    private IRIS iris() {
        return connectionManager.getIris();
    }


    public void deleteAllMessages() {
        connectionManager.callVoid("hca.utl.chcaUTL", "CleanOrdersAP");
        connectionManager.callVoid("hca.utl.chcaUTL", "CleanQueues", "1");
        logger.info("Executed commands for delete all messages");
    }

    public String getInstallationPath() {
        return connectionManager.callString("%SYSTEM.Util", "InstallDirectory");
    }

//    public HostInfoList getHostInfo() {
//        return new HostInfoList(parseList(iris.classMethodString("Automation.HostController", "GetHostsStatus"), HostInfo.class));
//    }

    public String getHostInfo() {
        String x = connectionManager.callString("Automation.HostController", "GetHostsStatus");
        return x;
    }

    public String getSharingPath() {
        return connectionManager.callString("co.sys.cParameters", "GetValue", "SHARING_PATH", -1, -1);
    }

    public void enableTCPConnection(String hostName, String connectionId) {
        connectionManager.callVoid("Automation.HostController", "EnableTCPConnection", hostName, connectionId);
    }

    public boolean checkTCPConnectionStatus(String connectionId) {
        return connectionManager.callBoolean("Automation.HostController", "CheckTCPConnectionStatus", connectionId);
    }

    public void enableWSConnection(String hostName, String connectionId) {
        connectionManager.callVoid("Automation.HostController", "EnableWSConnection", hostName, connectionId);
    }

    public boolean checkWSConnectionStatus(String connectionId) {
        return connectionManager.callBoolean("Automation.HostController", "CheckWSConnectionStatus", connectionId);
    }

    public void configWSConnection(String connectionId, String wsLocation) {
        connectionManager.callVoid("Automation.HostController", "ConfigWSConnection", connectionId, wsLocation);
    }

    public void configWSConnection(String connectionId, String wsLocation, String apiKeyFile, String apiKeyValue) {
        connectionManager.callVoid("Automation.HostController", "ConfigWSConnection", connectionId, wsLocation, apiKeyFile, apiKeyValue);
    }



    public static <T> List<T> parseList(String json, Class<T> clazz) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON to List<" + clazz.getSimpleName() + ">", e);
        }
    }

}
