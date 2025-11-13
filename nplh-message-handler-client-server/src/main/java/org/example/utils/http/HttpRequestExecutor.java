package org.example.utils.http;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * Ejecutor de peticiones HTTP usando Apache HttpClient.
 * Gestiona el envío de peticiones POST y la recepción de respuestas.
 */
public class HttpRequestExecutor {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestExecutor.class);

    public static String sendSoapPost(
            CloseableHttpClient client,
            String url,
            String soapAction,
            Map<String, String> headers,
            String soapBody) throws IOException {
        
        logger.debug("📤 Enviando petición SOAP - Action: {}", soapAction);
        
        HttpPost httpPost = new HttpPost(url);
        
        // Headers SOAP estándar
        httpPost.setHeader("Content-Type", "text/xml; charset=utf-8");
        httpPost.setHeader("SOAPAction", soapAction);
        
        // Headers adicionales
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                httpPost.setHeader(header.getKey(), header.getValue());
                logger.debug("  Header: {} = {}", header.getKey(), header.getValue());
            }
        }
        
        // Body SOAP
        StringEntity entity = new StringEntity(soapBody, ContentType.TEXT_XML);
        httpPost.setEntity(entity);
        
        // Ejecutar
        return client.execute(httpPost, response -> {
            int statusCode = response.getCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            
            logger.debug("📥 Respuesta SOAP recibida - Status: {}", statusCode);
            
            return responseBody;
        });
    }
}
