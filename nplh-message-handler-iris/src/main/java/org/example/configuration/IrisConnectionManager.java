package org.example.configuration;

import com.intersystems.jdbc.IRIS;
import com.intersystems.jdbc.IRISConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class IrisConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(IrisConnectionManager.class);

    private static final String HOST = "localhost";
    private static final int PORT = 1972;
    private static final String NAMESPACE = "CONN";
    private static final String USER = "_system";
    private static final String PASS = "CONNECT";

    private IRISConnection conn;
    private IRIS iris;

    public synchronized IRIS getIris() {
        reconnectIfNeeded();
        return iris;
    }

    private void connect() {
        try {
            logger.info("🔗 Connecting to IRIS...");
            conn = (IRISConnection) DriverManager.getConnection(
                    "jdbc:IRIS://" + HOST + ":" + PORT + "/" + NAMESPACE,
                    USER, PASS
            );
            iris = IRIS.createIRIS(conn);
        } catch (SQLException e) {
            logger.error("❌ Unable to establish IRIS connection", e);
            throw new RuntimeException(e);
        }
    }

    public void callVoid(String className, String methodName, Object... args) {
        reconnectIfNeeded();
        iris.classMethodVoid(className, methodName, args);
    }

    public String callString(String className, String methodName, Object... args) {
        reconnectIfNeeded();
        return iris.classMethodString(className, methodName, args);
    }

    public boolean callBoolean(String className, String methodName, Object... args) {
        reconnectIfNeeded();
        return iris.classMethodBoolean(className, methodName, args);
    }

    private void reconnectIfNeeded() {
        try {
            if (conn == null || !conn.isValid(2)) {
                logger.warn("⚠️ IRIS connection invalid. Reconnecting...");
                connect();
            }
        } catch (SQLException e) {
            logger.error("❌ Error validating IRIS connection", e);
            connect(); // intento de recuperación
        }
    }
}
