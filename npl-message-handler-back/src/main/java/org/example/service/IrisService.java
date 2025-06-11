package org.example.service;

import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.*;

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
        logger.info("Executed commands for delete all messages");
    }
}
