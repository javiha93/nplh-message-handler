package org.example.domain.message.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Reagent {
    private LocalDateTime expirationDateTime;
    private String intendedUseFlag;
    private String lotNumber;
    private String lotSerialNumber;
    private String catalogNumber;
    private String manufacturer;
    private LocalDateTime receivedDateTime;
    private String substanceName;
    private String substanceOtherName;
    private String substanceType;

    public static Reagent Default() {
        Reagent reagent = new Reagent();

        reagent.setExpirationDateTime(LocalDateTime.of(2016, 9, 2, 12, 0, 0));
        reagent.setIntendedUseFlag("K510");
        reagent.setLotNumber("1236");
        reagent.setLotSerialNumber("56251");
        reagent.setCatalogNumber("228664");
        reagent.setManufacturer("Ventana Medical Systems");
        reagent.setReceivedDateTime(LocalDateTime.of(2015, 9, 2, 12, 0, 0));
        reagent.setSubstanceName("UV INHIBITOR");
        reagent.setSubstanceOtherName("Other substance Name");
        reagent.setSubstanceType("ANTIBODY");

        return reagent;
    }
}
