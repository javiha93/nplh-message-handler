package org.example.utils.http;

import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Factory personalizado para crear sockets con bind a una IP específica.
 * Se utiliza con Apache HttpClient 5 para forzar el binding a una IP local concreta.
 */
public class SourceIPConnectionSocketFactory extends PlainConnectionSocketFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(SourceIPConnectionSocketFactory.class);
    
    private final InetAddress sourceAddress;
    
    /**
     * Constructor
     * 
     * @param sourceIP IP de origen a la que hacer bind (debe existir en el sistema)
     * @throws IOException si la IP no es válida
     */
    public SourceIPConnectionSocketFactory(String sourceIP) throws IOException {
        super();
        
        // Validar que la IP existe en el sistema
        if (!NetworkInterfaceValidator.isLocalIP(sourceIP)) {
            throw new IOException("La IP " + sourceIP + " no existe en las interfaces de red del sistema");
        }
        
        this.sourceAddress = InetAddress.getByName(sourceIP);
        logger.info("🔧 SourceIPConnectionSocketFactory creado para IP: {}", sourceIP);
    }
    
    @Override
    public Socket createSocket(HttpContext context) throws IOException {
        Socket socket = new Socket();
        
        // Hacer bind a la IP específica antes de conectar
        InetSocketAddress bindAddress = new InetSocketAddress(sourceAddress, 0); // Puerto 0 = aleatorio
        socket.bind(bindAddress);
        
        logger.debug("🔌 Socket bindeado a IP: {}", sourceAddress.getHostAddress());
        
        return socket;
    }
}
