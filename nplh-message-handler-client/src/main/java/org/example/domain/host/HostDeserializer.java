package org.example.domain.host;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class HostDeserializer extends JsonDeserializer<Host> {
    @Override
    public Host deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();

        for (HL7Host host : HL7Host.values()) {
            if (host.name().equals(text)) {
                return host;
            }
        }
        for (WSHost host : WSHost.values()) {
            if (host.name().equals(text)) {
                return host;
            }
        }
        if ("HISTOBOT".equals(text)) {
            return new RESTHost("histobot", "histobotApiKeyValue");
        }
        if ("HISTOBOT_SENSITIVE_DATA".equals(text)) {
            return new RESTHost("histobotSensitiveData", "histobotSensitiveDataApiKeyValue");
        }

        throw new RuntimeException("Host no reconocido: " + text);
    }
}
