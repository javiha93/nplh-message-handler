package org.example.domain.host;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum WSHost implements Host {
    UPATH_CLOUD("/csp/conn/ws.ext.UpathCloud.UpathCloudToVCWebServiceSoap.CLS",
            Map.of("X-VANTAGE-VCONNECT-APIKEY", "PruebaAPIKey1")),
    VANTAGE_WS("/csp/conn/ws.ext.VANTAGEWS.VANTAGEWSToVCService.cls",
            Map.of("X-VANTAGE-VCONNECT-APIKEY", "PruebaAPIKey1"));

    private final String path;
    private final Map<String, String> header;
}
