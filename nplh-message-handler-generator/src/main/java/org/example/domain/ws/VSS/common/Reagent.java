package org.example.domain.ws.VSS.common;

import org.example.domain.ws.WSSegment;

import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public class Reagent extends WSSegment {
    private String substanceType;
    private String substanceName;
    private String lotNumber;
    private String lotSerialNumber;
    private String catalogNumber;
    private String substanceOtherName;
    private String manufacturer;
    private String expirationDateTime;
    private String receivedDateTime;
    private String usedFlag;

    public static Reagent FromReagent(org.example.domain.message.entity.Reagent entityReagent) {
        Reagent reagent = new Reagent();

        reagent.substanceType = entityReagent.getSubstanceType();
        reagent.substanceName = entityReagent.getSubstanceName();
        reagent.lotNumber = entityReagent.getLotNumber();
        reagent.lotSerialNumber = entityReagent.getLotSerialNumber();
        reagent.catalogNumber = entityReagent.getCatalogNumber();
        reagent.substanceOtherName = entityReagent.getSubstanceOtherName();
        reagent.manufacturer = entityReagent.getManufacturer();
        reagent.usedFlag = entityReagent.getIntendedUseFlag();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        reagent.expirationDateTime = entityReagent.getExpirationDateTime().format(formatter);
        reagent.receivedDateTime = entityReagent.getReceivedDateTime().format(formatter);

        return reagent;
    }
    public boolean isEmpty() {
        return Stream.of(substanceType, substanceName, lotNumber, lotSerialNumber)
                .allMatch(value -> value == null || value.trim().isEmpty());
    }


    public String toString(int indentationLevel) {
        String specimen = addIndentation(indentationLevel) + "<ReagentInfo>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            specimen += addIndentation(indentationLevel) + "<Substance>" +  nullSafe(substanceType) + "</Substance>\n"
                    + addIndentation(indentationLevel) + "<SubstanceName>" +  nullSafe(substanceName) + "</SubstanceName>\n"
                    + addIndentation(indentationLevel) + "<LotNumber>" +  nullSafe(lotNumber) + "</LotNumber>\n"
                    + addIndentation(indentationLevel) + "<LotSerialNumber>" +  nullSafe(lotSerialNumber) + "</LotSerialNumber>\n"
                    + addIndentation(indentationLevel) + "<CatalogNumber>" +  nullSafe(catalogNumber) + "</CatalogNumber>\n"
                    + addIndentation(indentationLevel) + "<SubstanceOtherName>" +  nullSafe(substanceOtherName) + "</SubstanceOtherName>\n"
                    + addIndentation(indentationLevel) + "<Manufacturer>" +  nullSafe(manufacturer) + "</Manufacturer>\n"
                    + addIndentation(indentationLevel) + "<ExpirationDateTime>" +  nullSafe(expirationDateTime) + "</ExpirationDateTime>\n"
                    + addIndentation(indentationLevel) + "<ReceivedDateTime>" +  nullSafe(receivedDateTime) + "</ReceivedDateTime>\n"
                    + addIndentation(indentationLevel) + "<UsedFlag>" +  nullSafe(usedFlag) + "</UsedFlag>\n";

            indentationLevel --;
        }

        specimen += addIndentation(indentationLevel) + "</ReagentInfo>";
        return specimen;
    }
}
