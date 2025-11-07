package org.example.domain.ws.VTGWS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.message.MessageHeader;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Slide;
import org.example.domain.ws.WSSegment;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class LabOrder extends WSSegment {
    private String caseId;
    private String prefix;
    private String isUrgent;
    private Patient patient;
    private String receivingApplication;
    private String receivingFacility;
    private Physician requestingPhysician;
    private String sendingApplication;
    private String sendingFacility;
    private List<StainOrder> stainOrderList;
    private String techConsultCaseId;
    public String workflowInformation;
    private Pathologist pathologist;
    private String registrationDateTime;
    private String siteDescription;
    private String siteName;
    private Technician technician;

    public static LabOrder FromMessage(Message message) {
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
                    +  addIndentation(indentationLevel) + "<StainOrders>" + "\n";

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
