package org.example.domain.hl7.common;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.professional.Physician;

import java.util.Objects;

@Data
@NoArgsConstructor
public class PV1 extends HL7Segment {

    @HL7Position(position = 7, subPosition = 1)
    protected String requestorCode;

    @HL7Position(position = 7, subPosition = 2)
    protected String requestorLastName;

    @HL7Position(position = 7, subPosition = 3)
    protected String requestorFirstName;

    @HL7Position(position = 7, subPosition = 4)
    protected String requestorMiddleName;

    @HL7Position(position = 7, subPosition = 5)
    protected String requestorSuffix;

    @HL7Position(position = 7, subPosition = 6)
    protected String requestorPrefix;

    @HL7Position(position = 7, subPosition = 7)
    protected String requestorAddress;

    @HL7Position(position = 7, subPosition = 9)
    protected String requestorCity;

    @HL7Position(position = 7, subPosition = 10)
    protected String requestorCountry;

    @HL7Position(position = 7, subPosition = 11)
    protected String requestorState;

    @HL7Position(position = 7, subPosition = 12)
    protected String requestorHomeTel;

    @HL7Position(position = 7, subPosition = 13)
    protected String requestorMobileTel;

    @HL7Position(position = 7, subPosition = 14)
    protected String requestorWorkTel;

    @HL7Position(position = 7, subPosition = 15)
    protected String requestorZipCode;

    public static PV1 Default() {
        PV1 pv1 = new PV1();

        pv1.requestorCode = "IndiID";
        pv1.requestorLastName = "ILastName";
        pv1.requestorFirstName = "IFirstName";
        pv1.requestorMiddleName = "ImiddleName";
        pv1.requestorSuffix = "Isufix";
        pv1.requestorPrefix = "Iprefix";
        pv1.requestorAddress = "Iaddress";
        pv1.requestorCity = "city";
        pv1.requestorCountry = "Icountry";
        pv1.requestorState = "state";
        pv1.requestorHomeTel = "hometel";
        pv1.requestorMobileTel = "mobiletel";
        pv1.requestorWorkTel = "worktel";
        pv1.requestorZipCode = "zipcode";

        return pv1;
    }

    public static PV1 Default(PV1 pv1) {
        pv1.requestorCode = "IndiID";
        pv1.requestorLastName = "ILastName";
        pv1.requestorFirstName = "IFirstName";
        pv1.requestorMiddleName = "ImiddleName";
        pv1.requestorSuffix = "Isufix";
        pv1.requestorPrefix = "Iprefix";
        pv1.requestorAddress = "Iaddress";
        pv1.requestorCity = "city";
        pv1.requestorCountry = "Icountry";
        pv1.requestorState = "state";
        pv1.requestorHomeTel = "hometel";
        pv1.requestorMobileTel = "mobiletel";
        pv1.requestorWorkTel = "worktel";
        pv1.requestorZipCode = "zipcode";

        return pv1;
    }

    public static PV1 FromPhysician(Physician physician, PV1 pv1) {
        pv1.requestorCode = physician.getCode();
        pv1.requestorLastName = physician.getLastName();
        pv1.requestorFirstName = physician.getFirstName();
        pv1.requestorMiddleName = physician.getMiddleName();
        pv1.requestorSuffix = physician.getSuffix();
        pv1.requestorPrefix = physician.getPrefix();
        pv1.requestorAddress = physician.getAddress();
        pv1.requestorCity = physician.getCity();
        pv1.requestorCountry = physician.getCountry();
        pv1.requestorState = physician.getState();
        pv1.requestorHomeTel = physician.getHomePhone();
        pv1.requestorMobileTel = physician.getMobile();
        pv1.requestorWorkTel = physician.getWorkPhone();
        pv1.requestorZipCode = physician.getZip();

        return pv1;
    }

    @Override
    public String toString() {
        String value = "PV1|||||||" +
                nullSafe(requestorCode) + "^" + nullSafe(requestorLastName) + "^" + nullSafe(requestorFirstName) + "^" + nullSafe(requestorMiddleName) + "^" + nullSafe(requestorSuffix) + "^" + nullSafe(requestorPrefix) + "^" +
                nullSafe(requestorAddress) + "^^" + nullSafe(requestorCity) + "^" + nullSafe(requestorCountry) + "^" + nullSafe(requestorState) + "^" +
                nullSafe(requestorHomeTel) + "^" + nullSafe(requestorMobileTel) + "^" + nullSafe(requestorWorkTel) + "^" +
                nullSafe(requestorZipCode) + "|";

        return cleanSegment(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PV1 pv1 = (PV1) o;
        return Objects.equals(requestorCode, pv1.requestorCode) &&
                Objects.equals(requestorLastName, pv1.requestorLastName) &&
                Objects.equals(requestorFirstName, pv1.requestorFirstName) &&
                Objects.equals(requestorMiddleName, pv1.requestorMiddleName) &&
                Objects.equals(requestorSuffix, pv1.requestorSuffix) &&
                Objects.equals(requestorPrefix, pv1.requestorPrefix) &&
                Objects.equals(requestorAddress, pv1.requestorAddress) &&
                Objects.equals(requestorCity, pv1.requestorCity) &&
                Objects.equals(requestorCountry, pv1.requestorCountry) &&
                Objects.equals(requestorState, pv1.requestorState) &&
                Objects.equals(requestorHomeTel, pv1.requestorHomeTel) &&
                Objects.equals(requestorMobileTel, pv1.requestorMobileTel) &&
                Objects.equals(requestorWorkTel, pv1.requestorWorkTel) &&
                Objects.equals(requestorZipCode, pv1.requestorZipCode);
    }

    protected static PV1 parsePV1(String line, PV1 pv1) {
        String[] fields = line.split("\\|");

        // Campo 8 (position 8) - Attending Doctor (ID^LastName^FirstName^MiddleName^Suffix^Prefix)
        if (fields.length > 7) {
            String[] physician = fields[7].split("\\^");
            if (physician.length > 0) pv1.setRequestorCode(physician[0]);
            if (physician.length > 1) pv1.setRequestorLastName(physician[1]);
            if (physician.length > 2) pv1.setRequestorFirstName(physician[2]);
            if (physician.length > 3) pv1.setRequestorMiddleName(physician[3]);
            if (physician.length > 4) pv1.setRequestorSuffix(physician[4]);
            if (physician.length > 5) pv1.setRequestorPrefix(physician[5]);
        }

        return pv1;
    }

}
