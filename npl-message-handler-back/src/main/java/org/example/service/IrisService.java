package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISConnection;
import org.example.domain.host.Host;
import org.example.domain.host.HostInfo;
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
    private final IRIS iris;

    public IrisService() {
        try {
            conn = (IRISConnection) DriverManager.getConnection(
                    "jdbc:IRIS://" + HOST + ":" + PORT + "/" + NAMESPACE,
                    USER,
                    PASS
            );
            iris = IRIS.createIRIS(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAllMessages() {
        iris.classMethodVoid("hca.utl.chcaUTL", "CleanOrdersAP");
        iris.classMethodVoid("hca.utl.chcaUTL", "CleanQueues", "1");
        var x = getInstallationPath();
        logger.info("Executed commands for delete all messages");
    }

    public String getInstallationPath() {
        return iris.classMethodString("%SYSTEM.Util", "InstallDirectory");
    }

    public List<HostInfo> getHostInfo() {
        return parseList(iris.classMethodString("termutils.Canelita", "GetHostsStatus"), HostInfo.class);
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
