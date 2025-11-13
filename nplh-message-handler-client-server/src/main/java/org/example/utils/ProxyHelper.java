package org.example.utils;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.example.utils.http.HttpClientFactory;
import org.example.utils.http.HttpRequestExecutor;
import org.example.utils.http.NetworkInterfaceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ProxyHelper {
    
    private static final Logger logger = LoggerFactory.getLogger(ProxyHelper.class);

    public static String sendWSMessage(String targetUrl, String sourceIP, String soapAction, Map<String, String> headers, String soapBody) throws IOException {
        if (sourceIP != null && !sourceIP.isEmpty()) {
            CloseableHttpClient client = HttpClientFactory.createClientWithSourceIP(sourceIP);
            return sendWSMessage(client, targetUrl, soapAction, headers, soapBody);
        }
        return sendWSMessage(targetUrl, soapAction, headers, soapBody);

    }
    public static String sendWSMessage(String targetUrl, String soapAction, Map<String, String> headers, String soapBody) throws IOException {
        CloseableHttpClient client = HttpClientFactory.createDefaultClient();
        return sendWSMessage(client, targetUrl, soapAction, headers, soapBody);
    }
    private static String sendWSMessage(CloseableHttpClient client, String targetUrl, String soapAction, Map<String, String> headers, String soapBody) throws IOException {
        try {
            String response = HttpRequestExecutor.sendSoapPost(
                    client, targetUrl, soapAction, headers, soapBody);

            logger.debug("✅ Respuesta SOAP recibida");
            return response;

        } finally {
            HttpClientFactory.closeClient(client);
        }
    }

    //TODO ADD LOGS
    public static void configureIP(String ip) {
        if (!ProxyHelper.isIPAvailable(ip)) {
            addIpAlias(ip);
        }
    }

    public static boolean isIPAvailable(String ip) {
        return NetworkInterfaceValidator.isLocalIP(ip);
    }

    public static void addIpAlias(String ipAddress) {

        // Construye el comando de PowerShell dinámicamente
        String command = String.format(
                "New-NetIPAddress -InterfaceAlias 'Loopback Pseudo-Interface 1' -IPAddress '%s' -PrefixLength 24 -ErrorAction Stop",
                ipAddress
        );

        System.out.println("Ejecutando comando de PowerShell: " + command);
        System.out.println("Asegúrate de estar ejecutando esto como Administrador...");

        ProcessBuilder processBuilder = new ProcessBuilder(
                "powershell.exe",
                "-Command",
                command
        );

        try {
            Process process = processBuilder.start();

            // Consumir las salidas del proceso
            StreamGobbler stdOut = new StreamGobbler(process.getInputStream(), System.out::println);
            StreamGobbler stdErr = new StreamGobbler(process.getErrorStream(), System.err::println);
            Executors.newSingleThreadExecutor().submit(stdOut);
            Executors.newSingleThreadExecutor().submit(stdErr);

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("\n¡Éxito! La IP " + ipAddress + " fue añadida.");
            } else {
                System.err.println("\nError. El comando falló con el código de salida: " + exitCode);
                System.err.println("Comprueba los mensajes de error. ¿Ya existe esa IP?");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("Ocurrió una excepción al ejecutar el proceso:");
            e.printStackTrace();
        }
    }


    static class StreamGobbler implements Runnable {
        private final BufferedReader reader;
        private final Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
            this.consumer = consumer;
        }

        @Override
        public void run() {
            reader.lines().forEach(consumer);
        }
    }
}
