package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

@Service
public class IrisService {
    static final Logger logger = LoggerFactory.getLogger(IrisService.class);
    private static final String HOST = "localhost";
    private static final int PORT = 1972;
    private static final String NAMESPACE = "CONN";
    private static final String USER = "_system";
    private static final String PASS = "CONNECT";

    IRISConnection conn;
    private IRIS iris;

    public IrisService() {
        connect();
    }

    private void connect() {
        try {
            conn = (IRISConnection) DriverManager.getConnection(
                    "jdbc:IRIS://" + HOST + ":" + PORT + "/" + NAMESPACE,
                    USER,
                    PASS
            );
            iris = IRIS.createIRIS(conn);
        } catch (SQLException e) {
            logger.error("Unable to establish IRIS connection");
            throw new RuntimeException(e);
        }
    }

    private void reconnectIfNeeded() {
        try {
            if (!conn.isValid(2)) {
                connect();
            }
        } catch (SQLException e) {
            logger.error("Failed during IRIS connection validation");
            throw new RuntimeException(e);
        }
    }

    private void callVoid(String className, String methodName, Object... args) {
        reconnectIfNeeded();
        iris.classMethodVoid(className, methodName, args);
    }

    private String callString(String className, String methodName, Object... args) {
        reconnectIfNeeded();
        return iris.classMethodString(className, methodName, args);
    }

    private boolean callBoolean(String className, String methodName, Object... args) {
        reconnectIfNeeded();
        return iris.classMethodBoolean(className, methodName, args);
    }


    public void deleteAllMessages() {
        callVoid("hca.utl.chcaUTL", "CleanOrdersAP");
        callVoid("hca.utl.chcaUTL", "CleanQueues", "1");
        logger.info("Executed commands for delete all messages");
    }

    public String getInstallationPath() {
        return iris.classMethodString("%SYSTEM.Util", "InstallDirectory");
    }

//    public HostInfoList getHostInfo() {
//        return new HostInfoList(parseList(iris.classMethodString("Automation.HostController", "GetHostsStatus"), HostInfo.class));
//    }

    public String getHostInfo() {
        return callString("Automation.HostController", "GetHostsStatus");
    }

    public String getSharingPath() {
        return callString("co.sys.cParameters", "GetValue", "SHARING_PATH", -1, -1);
    }

    public void enableTCPConnection(String hostName, String connectionId) {
        callVoid("Automation.HostController", "EnableTCPConnection", hostName, connectionId);
    }

    public boolean checkTCPConnectionStatus(String connectionId) {
        return callBoolean("Automation.HostController", "CheckTCPConnectionStatus", connectionId);
    }

    public void enableWSConnection(String hostName, String connectionId) {
        callVoid("Automation.HostController", "EnableWSConnection", hostName, connectionId);
    }

    public boolean checkWSConnectionStatus(String connectionId) {
        return callBoolean("Automation.HostController", "CheckWSConnectionStatus", connectionId);
    }

    public void configWSConnection(String connectionId, String wsLocation) {
        callVoid("Automation.HostController", "ConfigWSConnection", connectionId, wsLocation);
    }

    public void configWSConnection(String connectionId, String wsLocation, String apiKeyFile, String apiKeyValue) {
        callVoid("Automation.HostController", "ConfigWSConnection", connectionId, wsLocation, apiKeyFile, apiKeyValue);
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
