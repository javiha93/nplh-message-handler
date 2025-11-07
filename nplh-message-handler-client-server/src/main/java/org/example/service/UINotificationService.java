package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.server.message.ServerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service to add messages to servers via the UI middleware
 */
public class UINotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(UINotificationService.class);
    private static final String UI_ENDPOINT = "http://localhost:8084/api/ui/servers/addServerMessage";
    
    // Configure HttpClient to use HTTP/1.1 only (no HTTP/2 upgrade)
    private static final HttpClient httpClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_1_1)
        .build();
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Adds messages to a server through the UI middleware
     * The UI will then forward the request to the backend
     * 
     * @param serverName The name of the server that received messages
     * @param serverMessage List of response messages
     */
    public static void addServerMessage(String serverName, ServerMessage serverMessage) {
        // Execute asynchronously to avoid blocking the main thread
        CompletableFuture.runAsync(() -> {
            try {
                // Create request body
                Map<String, Object> requestBody = new HashMap<>();
                requestBody.put("serverName", serverName);
                requestBody.put("serverMessage", serverMessage);
                
                String jsonBody = objectMapper.writeValueAsString(requestBody);

                logger.info("📤 Request URL: {}", UI_ENDPOINT);
                logger.debug("Request body: {}", jsonBody);
                
                // Build HTTP request
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(UI_ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
                
                logger.info("📤 Sending HTTP request...");
                
                // Send request
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                
                logger.info("📥 Received response with status: {}", response.statusCode());
                
                if (response.statusCode() == 200) {
                    logger.info("✅ Messages sent successfully to UI for server '{}'", serverName);
                    logger.debug("Response body: {}", response.body());
                } else {
                    logger.warn("⚠️ UI returned status {}: {}", response.statusCode(), response.body());
                }
                
            } catch (Exception e) {
                logger.error("❌ Error sending messages to UI for server '{}': {}", serverName, e.getMessage());
                logger.error("❌ Exception type: {}", e.getClass().getName());
                logger.error("❌ Stack trace:", e);
            }
        }).exceptionally(throwable -> {
            logger.error("❌ CompletableFuture exception for server '{}': {}", serverName, throwable.getMessage());
            logger.error("❌ Full stack trace:", throwable);
            return null;
        });
    }
}
