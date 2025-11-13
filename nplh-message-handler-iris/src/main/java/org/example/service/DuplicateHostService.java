package org.example.service;

import org.example.configuration.IrisConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DuplicateHostService {

    static final Logger logger = LoggerFactory.getLogger(DuplicateHostService.class);
    private final IrisConnectionManager connectionManager;

    public DuplicateHostService(IrisConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        logger.info("✅ StainProtocols service initialized using shared connection");
    }

    public void duplicateHost(String host, List<String> newHostList) {
        connectionManager.callVoid("termutils.DuplicateHost", "DuplicateHostWrapper", host, String.join(",", newHostList));
        logger.info("Duplicated host {} successfully", host);
    }

    public void deleteHost(String host) {
        connectionManager.callVoid("hca.utl.chcaUTL", "DelHost", host);
        logger.info("Deleted host {} successfully", host);
    }
}
