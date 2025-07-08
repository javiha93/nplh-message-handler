package org.example.logging;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MessageLogger {

    private final Logger delegate;
    private final String clientLogger;

    public MessageLogger(Logger logger, String clientLogger) {
        this.delegate = logger;
        this.clientLogger = clientLogger;
    }

    public void info(String format, Object... arguments) {
        delegate.info(format, arguments);
    }

    public void error(String format, Object... arguments) {
        delegate.error(format, arguments);
    }

    public void debug(String format, Object... arguments) {
        delegate.debug(format, arguments);
    }

    public void warn(String format, Object... arguments) {
        delegate.warn(format, arguments);
    }

    public void injectReceive(String controlId, String receiveText) {
        try {
            Path logFile = Paths.get("C:/tmp/mocks/client/Client_" + clientLogger + ".log");
            List<String> lines = Files.readAllLines(logFile);

            String timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());

            List<String> receiveBlock = new ArrayList<>();
            receiveBlock.add("\t" + timestamp + " INFO  - [RECEIVE]: \n");
            receiveBlock.addAll(Arrays.stream(receiveText.split("\r?\n"))
                    .map(line -> "\t" + line)
                    .collect(Collectors.toList()));
            receiveBlock.add(""); // línea en blanco

            List<String> modifiedLines = new ArrayList<>();
            boolean insideTargetBlock = false;
            boolean receiveInserted = false;

            for (String line : lines) {
                if (line.contains("[SEND][" + controlId + "]")) {
                    insideTargetBlock = true;
                }

                if (insideTargetBlock && line.trim().startsWith("####") && !receiveInserted) {
                    modifiedLines.addAll(receiveBlock);
                    receiveInserted = true;
                    insideTargetBlock = false;
                }

                modifiedLines.add(line);
            }

            Files.write(logFile, modifiedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            delegate.error("Error injecting RECEIVE block into log for controlId {}: {}", controlId, e.getMessage(), e);
        }
    }
}

