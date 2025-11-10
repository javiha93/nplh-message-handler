package org.example.domain.ws.UPATHCLOUD.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.UPATHCLOUD.NPLHToUPATHCLOUD.AddCase.UPATHCLOUD_AddCase;
import org.example.domain.ws.VTGWS.common.StainOrder;
import org.example.domain.ws.VTGWS.common.Technician;
import org.example.domain.ws.WSSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@JacksonXmlRootElement(localName = "arg0")
public class CaseData extends WSSegment {
    @JacksonXmlProperty(localName = "accessionNumber")
    private String accessionNumber;
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "allSpecimens")
    private List<Specimen> allSpecimens;
    @JacksonXmlProperty(localName = "assignedPathologist")
    private Pathologist assignedPathologist;
    @JacksonXmlProperty(localName = "caseTags")
    private String caseTags;
    @JacksonXmlProperty(localName = "client")
    private Client client;
    @JacksonXmlProperty(localName = "externalCaseId")
    private String externalCaseId;
    @JacksonXmlProperty(localName = "markedUrgent")
    private String markedUrgent;
    @JacksonXmlProperty(localName = "patientDetails")
    private Patient patient;
    @JacksonXmlProperty(localName = "referringPhysician")
    private Physician physician;
    @JacksonXmlProperty(localName = "site")
    private String site;
    @JacksonXmlProperty(localName = "siteDesc")
    private String siteDesc;
    @JacksonXmlProperty(localName = "tissueType")
    private String tissueType;
    @JacksonXmlProperty(localName = "workflow")
    private String workflow;


    public static CaseData fromMessage(Message message) {
        CaseData caseData = new CaseData();

        caseData.accessionNumber = message.getOrder().getSampleId();
        caseData.assignedPathologist = Pathologist.fromPathologist(message, message.getOrder().getPathologist());
        caseData.caseTags = message.getOrder().getTags();
        caseData.client = Client.fromSpecimen(message.getFirstSpecimen());
        caseData.externalCaseId = message.getOrder().getExtSampleId();
        caseData.markedUrgent = Boolean.toString("STAT".equals(message.getOrder().getStat()));
        caseData.patient = Patient.fromPatient(message.getPatient());
        caseData.physician = Physician.fromPhysician(message.getPhysician());

        // TODO check caseData.site
        // TODO check caseData.sitedesc
        caseData.tissueType = message.getFirstSpecimen().getProcedure().getTissue().getType();
        caseData.workflow = message.getOrder().getWorkFlow();

        caseData.allSpecimens = new ArrayList<>();
        for (org.example.domain.message.entity.Specimen specimen : message.getAllSpecimens()) {
            caseData.allSpecimens.add(Specimen.fromSpecimen(message, specimen));
        }

        return caseData;
    }

    public String toString(int indentationLevel) {
        String caseData = addIndentation(indentationLevel) + "<accessionNumber>" +  nullSafe(accessionNumber) + "</accessionNumber>\n";

            for (Specimen specimen: allSpecimens) {
                caseData += nullSafe(specimen, Specimen::new).toString(indentationLevel) + "\n";
            }

        caseData += nullSafe(assignedPathologist, Pathologist::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<caseTags>" +  nullSafe(caseTags) + "</caseTags>\n"
                    + nullSafe(client, Client::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<externalCaseId>" +  nullSafe(externalCaseId) + "</externalCaseId>\n"
                    + addIndentation(indentationLevel) + "<markedUrgent>" +  nullSafe(markedUrgent) + "</markedUrgent>\n"
                    + nullSafe(patient, Patient::new).toString(indentationLevel) + "\n"
                    + nullSafe(physician, Physician::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<site>" +  nullSafe(site) + "</site>\n"
                    + addIndentation(indentationLevel) + "<siteDesc>" +  nullSafe(siteDesc) + "</siteDesc>\n"
                    + addIndentation(indentationLevel) + "<tissueType>" +  nullSafe(tissueType) + "</tissueType>\n"
                    + addIndentation(indentationLevel) + "<workflow>" +  nullSafe(workflow) + "</workflow>\n";

        return caseData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CaseData caseData = (CaseData) o;

        return Objects.equals(accessionNumber, caseData.accessionNumber)
                && Objects.equals(allSpecimens, caseData.allSpecimens)
                && Objects.equals(assignedPathologist, caseData.assignedPathologist)
                && Objects.equals(caseTags, caseData.caseTags)
                && Objects.equals(client, caseData.client)
                && Objects.equals(externalCaseId, caseData.externalCaseId)
                && Objects.equals(markedUrgent, caseData.markedUrgent)
                && Objects.equals(patient, caseData.patient)
                && Objects.equals(physician, caseData.physician)
                && Objects.equals(site, caseData.site)
                && Objects.equals(siteDesc, caseData.siteDesc)
                && Objects.equals(tissueType, caseData.tissueType)
                && Objects.equals(workflow, caseData.workflow);
    }


    public static CaseData fromXml(String xml) {
        try {
            int start = xml.indexOf("<arg0");
            int end = xml.indexOf("</arg0>") + "</arg0>".length();
            String processXml = xml.substring(start, end);

            if (!processXml.contains("xmlns:xsi")) {
                processXml = processXml.replaceFirst(
                        "<arg0",
                        "<arg0 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                );
            }

            // Configurar mapper XML
            XmlMapper mapper = new XmlMapper();
            mapper.configure(FromXmlParser.Feature.EMPTY_ELEMENT_AS_NULL, true);
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mapper.setDefaultUseWrapper(false);

            return mapper.readValue(processXml, CaseData.class);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML", e);
        }
    }
}
