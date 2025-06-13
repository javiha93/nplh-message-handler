package org.example.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandler {

    public static List<String> extractUUIDs(String message) {
        List<String> uuids = new ArrayList<>();
        Pattern uuidPattern = Pattern.compile(
                "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = uuidPattern.matcher(message);
        while (matcher.find()) {
            uuids.add(matcher.group());
        }

        return uuids;
    }

    public static String textToLlp(String textMessage) {
        return HL7LLPCharacters.VT.getCharacter()
                + textMessage.trim().replaceAll("\\n", HL7LLPCharacters.CR.getCharacterAsString())
                + HL7LLPCharacters.CR.getCharacter()
                + HL7LLPCharacters.FS.getCharacter()
                + HL7LLPCharacters.CR.getCharacter();
    }

    public static String llpToText(String llpMessage) {
        if (llpMessage == null || llpMessage.isEmpty()) {
            return llpMessage;
        }

        String trimmedMessage = llpMessage.startsWith(HL7LLPCharacters.VT.getCharacterAsString())
                ? llpMessage.substring(1)
                : llpMessage;

        int fsIndex = trimmedMessage.indexOf(HL7LLPCharacters.FS.getCharacter());
        if (fsIndex >= 0) {
            trimmedMessage = trimmedMessage.substring(0, fsIndex);
        }

        return trimmedMessage.replaceAll(HL7LLPCharacters.CR.getCharacterAsString(), "\n").trim();
    }
}
