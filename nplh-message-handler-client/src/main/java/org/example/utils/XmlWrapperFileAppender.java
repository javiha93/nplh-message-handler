package org.example.utils;

import ch.qos.logback.core.FileAppender;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class XmlWrapperFileAppender<E> extends FileAppender<E> {

    public String rootTag = "serverMessages";

    @Override
    public void start() {
        super.start();

        try {
            File file = new File(getFile());

            if (file.exists()) {
                String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                String closingTag = "</" + rootTag + ">";
                String openingTag = "<" + rootTag + ">\n";

                if (content.trim().endsWith(closingTag)) {
                    int index = content.lastIndexOf(closingTag);
                    if (index != -1) {
                        String newContent = content.substring(0, index).trim() + "\n";
                        Files.write(file.toPath(), newContent.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
                    }
                } else {
                    System.out.println("XmlWrapperFileAppender: not found final tag");
                }

                if (new File(getFile()).length() == 0 && getOutputStream() != null) {
                    getOutputStream().write(openingTag.getBytes(StandardCharsets.UTF_8));
                    getOutputStream().flush();
                }
            }

        } catch (IOException e) {
            addError("Error writing XML", e);
        }
    }


    @Override
    public void stop() {
        String closingTag = "</" + rootTag + ">";
        try {
            if (getOutputStream() != null) {
                getOutputStream().write(closingTag.getBytes(StandardCharsets.UTF_8));
                getOutputStream().flush();
            }
        } catch (IOException e) {
            addError("Error writing XML footer", e);
        }

        super.stop();
    }

}
