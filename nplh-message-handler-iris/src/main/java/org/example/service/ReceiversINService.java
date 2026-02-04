package org.example.service;

import org.example.configuration.IrisConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReceiversINService {

    static final Logger logger = LoggerFactory.getLogger(ReceiversINService.class);
    private final IrisConnectionManager connectionManager;

    public ReceiversINService(IrisConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        logger.info("✅ ReceiversINService service initialized using shared connection");
    }

    public String getMessageReceiversINPendingToProcess() {
        return connectionManager.callString("Automation.PerformanceTest", "GetMessageReceiversINPendingToProcess");
    }

    public String getMessageReceiversINProcessed() {
        return connectionManager.callString("Automation.PerformanceTest", "GetMessageReceiversINProcessed");
    }
}
