package org.example.domain.hl7.LIS.NPLHToLIS.SLIDE_UPDATE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.hl7.common.OBX;
import org.example.domain.message.Message;
import org.example.domain.message.Patient;
import org.example.domain.message.entity.StainingInfo;
import org.example.domain.message.professional.Physician;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

import java.util.Objects;

@Data
@NoArgsConstructor
public class OBX_SLIDE_UPDATE extends OBX {

    private String patientID;
    private String patientName;
    private String patientLastName;
    private String patientMiddleName;
    private String patientBirthday;
    private String gender;
    private String surgicalProcedure;
    private String surgeryDate;
    private String facilityCode;
    private String facilityName;
    private String physicianName;
    private String physicianLastName;
    private String physicianMiddleName;
    private String caseId;
    private String slideId;
    private String blockId;
    private String specimenId;
    private String slideCount;
    private String stainingHostId;
    private String stainingHostVersion;
    private String stainerSerialNumber;
    private String stainerType;
    private String stainingRunNumber;
    private String StainingRunEstimatedMins;
    private String StainingRunSlidePosition;
    private String StainingRunMins;
    private String protocolNumber;
    private String protocolName;
    private String protocolIdentifier;

    public static OBX_SLIDE_UPDATE fromSlide(Slide slide, Message message) {
        OBX_SLIDE_UPDATE obx = new OBX_SLIDE_UPDATE();

        Block block = slide.getBlockParent(message);
        Specimen specimen = block.getSpecimenParent(message);
        Patient patient = message.getPatient();
        Physician physician = message.getPhysician();
        StainingInfo stainingInfo = slide.getStainingInfo();

        obx.setPatientID(patient.getCode());
        obx.setPatientName(patient.getFirstName());
        obx.setPatientLastName(patient.getLastName());
        obx.setPatientMiddleName(patient.getMiddleName());
        obx.setPatientBirthday(patient.getDateOfBirth());
        obx.setGender(patient.getSex());

        obx.setSurgicalProcedure(specimen.getProcedure().getSurgical().getName());
        obx.setSurgeryDate(specimen.getCollectDateTime());
        obx.setFacilityCode(specimen.getFacilityCode());
        obx.setFacilityName(specimen.getFacilityName());

        obx.setPhysicianName(physician.getFirstName());
        obx.setPhysicianLastName(physician.getLastName());
        obx.setPhysicianMiddleName(physician.getMiddleName());

        obx.setCaseId(message.getOrder().getSampleId());
        obx.setSpecimenId(specimen.getId());
        obx.setBlockId(block.getId());
        obx.setSlideId(slide.getId());
        obx.setSlideCount(String.valueOf(message.getAllSlides().size()));

        obx.setStainingHostId(stainingInfo.getHostID());
        obx.setStainingHostVersion(stainingInfo.getHostVersion());
        obx.setStainerSerialNumber(stainingInfo.getStainerSerialNumber());
        obx.setStainerType(stainingInfo.getStainerEffectiveType());
        obx.setStainingRunNumber(stainingInfo.getRunNumber());
        obx.setStainingRunEstimatedMins(stainingInfo.getRunEstimatedTime());
        obx.setStainingRunSlidePosition(stainingInfo.getSlidePosition());
        obx.setStainingRunMins(stainingInfo.getRunTime());

        obx.setProtocolNumber(slide.getStainProtocol().getNumber());
        obx.setProtocolName(slide.getStainProtocol().getName());
        obx.setProtocolIdentifier(slide.getStainProtocol().getIdentifier());

        return obx;
    }

    @Override
    public String toString() {
        int obxSegmentNumber = 0;
        return createOBXSegment("PatientID", getPatientID(), "ST", obxSegmentNumber ++) +
                createOBXSegment("PatientName", getPatientLastName() + "^" + getPatientName() + "^" + getPatientMiddleName(), "ST", obxSegmentNumber ++) +
                createOBXSegment("BirthDate", getPatientBirthday(), "DT", obxSegmentNumber ++) +
                createOBXSegment("Gender", getGender(), "ST", obxSegmentNumber ++) +
                createOBXSegment("SurgicalProcedure", getSurgicalProcedure(), "ST", obxSegmentNumber ++) +
                createOBXSegment("SurgeryDate", getSurgeryDate(), "ST", obxSegmentNumber ++) +
                createOBXSegment("Institution", getFacilityCode() + "^" + getFacilityName(), "ST", obxSegmentNumber ++) +
                createOBXSegment("Requester", getPhysicianLastName() + "^" + getPhysicianName() + "^" + getPhysicianMiddleName(), "ST", obxSegmentNumber ++) +
                createOBXSegment("CaseID", getCaseId(), "ST", obxSegmentNumber ++) +
                createOBXSegment("Custom1", getSlideId(), "ST", obxSegmentNumber ++) +
                createOBXSegment("Custom2", getBlockId(), "ST", obxSegmentNumber ++) +
                createOBXSegment("PanelID", getSpecimenId(), "ST", obxSegmentNumber ++) +
                createOBXSegment("PanelSlideCount", getSlideCount(), "NM", obxSegmentNumber ++) +
                createOBXSegment("StainingHostID", getStainingHostId(), "NM", obxSegmentNumber ++) +
                createOBXSegment("StainingHostVersion", getStainingHostVersion() , "ST", obxSegmentNumber ++) +
                createOBXSegment("StainerSerialNumber", getStainerSerialNumber(), "NM", obxSegmentNumber ++) +
                createOBXSegment("StainerType", getStainerType(), "ST", obxSegmentNumber ++) +
                createOBXSegment("StainingRunNumber", getStainingRunNumber(), "NM", obxSegmentNumber ++) +
                createOBXSegment("StainingRunEstimatedMins", getStainingRunEstimatedMins(), "NM", obxSegmentNumber ++) +
                createOBXSegment("StainingRunSlidePosition", getStainingRunSlidePosition(), "NM", obxSegmentNumber ++) +
                createOBXSegment("StainingRunMins", getStainingRunMins(), "NM", obxSegmentNumber ++);
    }

    private String createOBXSegment(String key, String value, String action, int obxSegmentNumber) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        return "OBX|" + obxSegmentNumber + "|" + action + "|" + nullSafe(protocolNumber) + "^" + nullSafe(protocolName) + "^" + nullSafe(protocolIdentifier) + "|" + key + "|" + value + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBX_SLIDE_UPDATE that = (OBX_SLIDE_UPDATE) o;

        if (!super.equals(o)) return false;

        return Objects.equals(patientID, that.patientID) &&
                Objects.equals(patientName, that.patientName) &&
                Objects.equals(patientLastName, that.patientLastName) &&
                Objects.equals(patientMiddleName, that.patientMiddleName) &&
                Objects.equals(patientBirthday, that.patientBirthday) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(surgicalProcedure, that.surgicalProcedure) &&
                Objects.equals(surgeryDate, that.surgeryDate) &&
                Objects.equals(facilityCode, that.facilityCode) &&
                Objects.equals(facilityName, that.facilityName) &&
                Objects.equals(physicianName, that.physicianName) &&
                Objects.equals(physicianLastName, that.physicianLastName) &&
                Objects.equals(physicianMiddleName, that.physicianMiddleName) &&
                Objects.equals(caseId, that.caseId) &&
                Objects.equals(slideId, that.slideId) &&
                Objects.equals(blockId, that.blockId) &&
                Objects.equals(specimenId, that.specimenId) &&
                Objects.equals(slideCount, that.slideCount) &&
                Objects.equals(stainingHostId, that.stainingHostId) &&
                Objects.equals(stainingHostVersion, that.stainingHostVersion) &&
                Objects.equals(stainerSerialNumber, that.stainerSerialNumber) &&
                Objects.equals(stainerType, that.stainerType) &&
                Objects.equals(stainingRunNumber, that.stainingRunNumber) &&
                Objects.equals(StainingRunEstimatedMins, that.StainingRunEstimatedMins) &&
                Objects.equals(StainingRunSlidePosition, that.StainingRunSlidePosition) &&
                Objects.equals(StainingRunMins, that.StainingRunMins) &&
                Objects.equals(protocolNumber, that.protocolNumber) &&
                Objects.equals(protocolName, that.protocolName) &&
                Objects.equals(protocolIdentifier, that.protocolIdentifier);
    }

    protected static OBX_SLIDE_UPDATE parseOBX(String allObx) {
        OBX_SLIDE_UPDATE obx = new OBX_SLIDE_UPDATE();

        String[] lines = allObx.split("\n");

        boolean first = true;
        for (String line : lines) {

            String[] fields = line.split("\\|");

            if (first && fields.length > 3 && fields[3] != null && !fields[3].isEmpty()) {
                String[] protocol = fields[3].split("\\^");
                if (protocol.length > 0) obx.setProtocolNumber(protocol[0]);
                if (protocol.length > 1) obx.setProtocolName(protocol[1]);
                if (protocol.length > 2) obx.setProtocolIdentifier(protocol[2]);
            }
            first = false;

            String key = getFieldValue(fields, 4);
            String value = getFieldValue(fields, 5);

            switch (key) {
                case "PatientID" -> obx.setPatientID(value);
                case "BirthDate" -> obx.setPatientBirthday(value);
                case "Gender" -> obx.setGender(value);
                case "SurgicalProcedure" -> obx.setSurgicalProcedure(value);
                case "SurgeryDate" -> obx.setSurgeryDate(value);
                case "CaseID" -> obx.setCaseId(value);
                case "Custom1" -> obx.setSlideId(value);
                case "Custom2" -> obx.setBlockId(value);
                case "PanelID" -> obx.setSpecimenId(value);
                case "PanelSlideCount" -> obx.setSlideCount(value);
                case "StainingHostID" -> obx.setStainingHostId(value);
                case "StainingHostVersion" -> obx.setStainingHostVersion(value);
                case "StainerSerialNumber" -> obx.setStainerSerialNumber(value);
                case "StainerType" -> obx.setStainerType(value);
                case "StainingRunNumber" -> obx.setStainingRunNumber(value);
                case "StainingRunEstimatedMins" -> obx.setStainingRunEstimatedMins(value);
                case "StainingRunSlidePosition" -> obx.setStainingRunSlidePosition(value);
                case "StainingRunMins" -> obx.setStainingRunMins(value);
                case "PatientName" -> {
                    String[] patient = value.split("\\^");
                    if (patient.length > 0) obx.setPatientLastName(patient[0]);
                    if (patient.length > 1) obx.setPatientName(patient[1]);
                    if (patient.length > 2) obx.setPatientMiddleName(patient[2]);
                }
                case "Institution" -> {
                    String[] facility = value.split("\\^");
                    if (facility.length > 0) obx.setFacilityCode(facility[0]);
                    if (facility.length > 1) obx.setFacilityName(facility[1]);
                }
                case "Requester" -> {
                    String[] physician = value.split("\\^");
                    if (physician.length > 0) obx.setPhysicianLastName(physician[0]);
                    if (physician.length > 1) obx.setPhysicianName(physician[1]);
                    if (physician.length > 2) obx.setPhysicianMiddleName(physician[2]);
                }
            }
        }

        return obx;
    }

}
