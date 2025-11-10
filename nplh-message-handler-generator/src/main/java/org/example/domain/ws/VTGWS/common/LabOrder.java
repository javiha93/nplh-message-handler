package org.example.domain.ws.VTGWS.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.MessageHeader;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class LabOrder extends WSSegment {
    @JacksonXmlProperty(localName = "CaseId")
    private String caseId;
    @JacksonXmlProperty(localName = "Prefix")
    private String prefix;
    @JacksonXmlProperty(localName = "IsUrgent")
    private String isUrgent;
    @JacksonXmlProperty(localName = "Patient")
    private Patient patient;
    @JacksonXmlProperty(localName = "ReceivingApplication")
    private String receivingApplication;
    @JacksonXmlProperty(localName = "ReceivingFacility")
    private String receivingFacility;
    @JacksonXmlProperty(localName = "RequestingPhysician")
    private Physician requestingPhysician;
    @JacksonXmlProperty(localName = "SendingApplication")
    private String sendingApplication;
    @JacksonXmlProperty(localName = "SendingFacility")
    private String sendingFacility;
    @JacksonXmlElementWrapper(localName = "StainOrders")
    @JacksonXmlProperty(localName = "StainOrder")
    private List<StainOrder> stainOrderList;
    @JacksonXmlProperty(localName = "TechConsultCaseId")
    private String techConsultCaseId;
    @JacksonXmlProperty(localName = "WorkflowInformation")
    public String workflowInformation;
    @JacksonXmlProperty(localName = "Pathologist")
    private Pathologist pathologist;
    @JacksonXmlProperty(localName = "RegistrationDateTime")
    private String registrationDateTime;
    @JacksonXmlProperty(localName = "SiteDescription")
    private String siteDescription;
    @JacksonXmlProperty(localName = "SiteName")
    private String siteName;
    @JacksonXmlProperty(localName = "Technician")
    private Technician technician;

    public static LabOrder fromMessage(Message message) {
        LabOrder labOrder = new LabOrder();

        Order order = message.getOrder();
        MessageHeader header = message.getHeader();

        labOrder.setCaseId(order.getSampleId());
        labOrder.setTechConsultCaseId(order.getSampleId());
        labOrder.setPrefix(order.getPrefix());
        labOrder.setReceivingApplication(header.getReceivingApplication());
        labOrder.setReceivingFacility(header.getReceivingFacility());
        labOrder.setSendingApplication(header.getSendingApplication());
        labOrder.setSendingFacility(header.getSendingFacility());
        labOrder.setWorkflowInformation(order.getWorkFlow());
        labOrder.setRegistrationDateTime(String.valueOf(order.getRegisterDate()));
        labOrder.setSiteName(order.getOriginCode());
        labOrder.setSiteDescription(order.getOriginDescription());

        labOrder.setPatient(Patient.FromPatient(message.getPatient()));
        labOrder.setRequestingPhysician(Physician.FromPhysician(message.getPhysician()));
        labOrder.setPathologist(Pathologist.FromPathologist(order.getPathologist()));
        labOrder.setTechnician(Technician.FromTechnician(order.getTechnician()));

        boolean isUrgent = order.getStat() != null && order.getStat().equals("STAT");
        labOrder.setIsUrgent(Boolean.toString(isUrgent));

        List<Slide> allSlides = message.getAllSlides();
        List<StainOrder> stainOrders = new ArrayList<>();
        for (Slide slide: allSlides) {
            stainOrders.add(StainOrder.FromOrder(message, slide));
        }
        labOrder.setStainOrderList(stainOrders);

        return labOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LabOrder labOrder = (LabOrder) o;

        return Objects.equals(caseId, labOrder.caseId)
                && Objects.equals(prefix, labOrder.prefix)
                && Objects.equals(isUrgent, labOrder.isUrgent)
                && Objects.equals(patient, labOrder.patient)
                && Objects.equals(receivingApplication, labOrder.receivingApplication)
                && Objects.equals(receivingFacility, labOrder.receivingFacility)
                && Objects.equals(requestingPhysician, labOrder.requestingPhysician)
                && Objects.equals(sendingApplication, labOrder.sendingApplication)
                && Objects.equals(sendingFacility, labOrder.sendingFacility)
                && Objects.equals(stainOrderList, labOrder.stainOrderList)
                && Objects.equals(workflowInformation, labOrder.workflowInformation)
                && Objects.equals(pathologist, labOrder.pathologist)
                && Objects.equals(registrationDateTime, labOrder.registrationDateTime)
                && Objects.equals(siteDescription, labOrder.siteDescription)
                && Objects.equals(siteName, labOrder.siteName)
                && Objects.equals(technician, labOrder.technician);
    }

    private boolean isEmpty() {
        return stainOrderList == null || stainOrderList.isEmpty();
    }

    public String toString(int indentationLevel) {
        String labOrder = addIndentation(indentationLevel) + "<labOrder>\n";

        if (!this.isEmpty()) {
            indentationLevel ++;

            labOrder += addIndentation(indentationLevel) + "<CaseId>" +  nullSafe(caseId) + "</CaseId>\n"
                    + addIndentation(indentationLevel) + "<Prefix>" +  nullSafe(prefix) + "</Prefix>\n"
                    + addIndentation(indentationLevel) + "<IsUrgent>" +  nullSafe(isUrgent) + "</IsUrgent>\n"
                    + nullSafe(patient, Patient::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<ReceivingApplication>" +  nullSafe(receivingApplication) + "</ReceivingApplication>\n"
                    + addIndentation(indentationLevel) + "<ReceivingFacility>" +  nullSafe(receivingFacility) + "</ReceivingFacility>\n"
                    + nullSafe(requestingPhysician, Physician::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<SendingApplication>" +  nullSafe(sendingApplication) + "</SendingApplication>\n"
                    + addIndentation(indentationLevel) + "<SendingFacility>" +  nullSafe(sendingFacility) + "</SendingFacility>\n"
                    + addIndentation(indentationLevel) + "<StainOrders>" + "\n";

            indentationLevel ++;

            for (StainOrder stainOrder: stainOrderList) {
                labOrder += nullSafe(stainOrder, StainOrder::new).toString(indentationLevel) + "\n";
            }

            indentationLevel --;

            labOrder += addIndentation(indentationLevel) + "</StainOrders>" + "\n"
                    + addIndentation(indentationLevel) + "<TechConsultCaseId>" +  nullSafe(techConsultCaseId) + "</TechConsultCaseId>\n"
                    + addIndentation(indentationLevel) + "<WorkFlowInformation>" +  nullSafe(workflowInformation) + "</WorkFlowInformation>\n"
                    + nullSafe(pathologist, Pathologist::new).toString(indentationLevel) + "\n"
                    + addIndentation(indentationLevel) + "<RegistrationDateTime>" +  nullSafe(registrationDateTime) + "</RegistrationDateTime>\n"
                    + addIndentation(indentationLevel) + "<SiteDescription>" +  nullSafe(siteDescription) + "</SiteDescription>\n"
                    + addIndentation(indentationLevel) + "<SiteName>" +  nullSafe(siteName) + "</SiteName>\n"
                    + nullSafe(technician, Technician::new).toString(indentationLevel) + "\n";

            indentationLevel --;
        }

        labOrder += addIndentation(indentationLevel) + "</labOrder>";
        return labOrder;
    }
}
