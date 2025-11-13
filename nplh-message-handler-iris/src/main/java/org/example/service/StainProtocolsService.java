package org.example.service;

import org.example.configuration.IrisConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StainProtocolsService {

    static final Logger logger = LoggerFactory.getLogger(StainProtocolsService.class);
    private final IrisConnectionManager connectionManager;

    public StainProtocolsService(IrisConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        logger.info("✅ StainProtocols service initialized using shared connection");
    }

    public void assignAdvancedStainProtocol(String host, String stainProtocolId) {
        connectionManager.callVoid("Automation.StainProtocolController", "AssignAdvancedStainProtocol", host, stainProtocolId);
        logger.info("Assigned stain protocol");
    }

    public void createStainProtocolAndAssign(String host, String testIdName, String testAbbreviation, String testName) {
        connectionManager.callVoid("Automation.StainProtocolController", "CreateStainProtocolAndAssign", host, testIdName, testAbbreviation, testName);
        logger.info("Create and Assigned stain protocol: {}^{}^{} to {}", testIdName, testAbbreviation, testName, host);
    }
}
