package org.example.utils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Clase de utilidad para validar IPs locales disponibles en las interfaces de red del sistema.
 */
public class NetworkInterfaceValidator {
    
    private static final Logger logger = LoggerFactory.getLogger(NetworkInterfaceValidator.class);

    public static boolean isLocalIP(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            
            // Verificar si es localhost
            if (addr.isLoopbackAddress()) {
                logger.debug("✅ IP {} es localhost", ip);
                return true;
            }
            
            // Verificar todas las interfaces de red
            var interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface netInterface = interfaces.nextElement();
                var addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress interfaceAddr = addresses.nextElement();
                    if (interfaceAddr.equals(addr)) {
                        logger.info("✅ IP {} encontrada en interfaz: {}", 
                            ip, netInterface.getDisplayName());
                        return true;
                    }
                }
            }
            
            logger.error("❌ IP {} NO encontrada en interfaces locales", ip);
            logger.error("❌ Debes añadir la IP primero. Ver: manage-ip-alias.ps1");
            return false;
            
        } catch (Exception e) {
            logger.error("❌ Error verificando IP local: {}", ip, e);
            return false;
        }
    }
}
