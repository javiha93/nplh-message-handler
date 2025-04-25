package org.example.domain.hl7.common;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.HL7Position;
import org.example.domain.hl7.HL7Segment;
import org.example.domain.message.Patient;
import org.example.domain.message.professional.Physician;

@Data
@NoArgsConstructor
public class PV1 extends HL7Segment {

    @HL7Position(position = 7, subPosition = 1)
    private String requestorCode;

    @HL7Position(position = 7, subPosition = 2)
    private String requestorLastName;

    @HL7Position(position = 7, subPosition = 3)
    private String requestorFirstName;

    @HL7Position(position = 7, subPosition = 4)
    private String requestorMiddleName;

    @HL7Position(position = 7, subPosition = 5)
    private String requestorSuffix;

    @HL7Position(position = 7, subPosition = 6)
    private String requestorPrefix;

    @HL7Position(position = 7, subPosition = 7)
    private String requestorAddress;

    @HL7Position(position = 7, subPosition = 9)
    private String requestorCity;

    @HL7Position(position = 7, subPosition = 10)
    private String requestorCountry;

    @HL7Position(position = 7, subPosition = 11)
    private String requestorState;

    @HL7Position(position = 7, subPosition = 12)
    private String requestorHomeTel;

    @HL7Position(position = 7, subPosition = 13)
    private String requestorMobileTel;

    @HL7Position(position = 7, subPosition = 14)
    private String requestorWorkTel;

    @HL7Position(position = 7, subPosition = 15)
    private String requestorZipCode;

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

}
