package org.example.utils.http;

import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Factory para crear instancias de Apache HttpClient configuradas con IP de origen específica.
 * Gestiona el ciclo de vida de los clientes HTTP y sus connection managers.
 */
public class HttpClientFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpClientFactory.class);
    
    /**
     * Crea un HttpClient estándar sin binding especial
     * 
     * @return CloseableHttpClient configurado
     */
    public static CloseableHttpClient createDefaultClient() {
        logger.debug("🔧 Creando HttpClient por defecto");
        
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setConnectTimeout(Timeout.of(5, TimeUnit.SECONDS))
            .setSocketTimeout(Timeout.of(30, TimeUnit.SECONDS))
            .build();
        
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(10);
        
        return HttpClients.custom()
            .setConnectionManager(connectionManager)
            .build();
    }
    
    /**
     * Crea un HttpClient con binding a IP de origen específica
     * 
     * @param sourceIP IP de origen (debe existir en el sistema)
     * @return CloseableHttpClient configurado con binding
     * @throws IOException si la IP no existe o hay error en la configuración
     */
    public static CloseableHttpClient createClientWithSourceIP(String sourceIP) throws IOException {
        logger.info("🔧 Creando HttpClient con bind a IP: {}", sourceIP);
        
        // Crear socket factory con binding a IP específica
        SourceIPConnectionSocketFactory socketFactory = new SourceIPConnectionSocketFactory(sourceIP);
        
        // Registrar el socket factory para el esquema "http"
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
            .register("http", socketFactory)
            .build();
        
        // Configuración de timeouts
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
            .setConnectTimeout(Timeout.of(5, TimeUnit.SECONDS))
            .setSocketTimeout(Timeout.of(30, TimeUnit.SECONDS))
            .build();
        
        // Connection Manager con socket factory registry
        PoolingHttpClientConnectionManager connectionManager = 
            new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        connectionManager.setMaxTotal(50);
        connectionManager.setDefaultMaxPerRoute(10);
        
        CloseableHttpClient client = HttpClients.custom()
            .setConnectionManager(connectionManager)
            .build();
        
        logger.info("✅ HttpClient creado y configurado para IP: {}", sourceIP);
        
        return client;
    }
    
    /**
     * Cierra un HttpClient de forma segura
     * 
     * @param client Cliente a cerrar
     */
    public static void closeClient(CloseableHttpClient client) {
        if (client != null) {
            try {
                client.close();
                logger.debug("🔒 HttpClient cerrado correctamente");
            } catch (IOException e) {
                logger.error("Error cerrando HttpClient", e);
            }
        }
    }
}
