package org.example.domain;

import lombok.Getter;

@Getter
public enum HL7LLPCharacters {

    VT(0x0b), FS(0x1c), CR(0x0d);

    final char character;

    HL7LLPCharacters(int character) {
        this.character = (char) character;
    }

    public String getCharacterAsString() {
        return String.valueOf(character);
    }

}