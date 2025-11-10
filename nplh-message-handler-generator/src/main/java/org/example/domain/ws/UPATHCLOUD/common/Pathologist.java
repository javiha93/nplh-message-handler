package org.example.domain.ws.UPATHCLOUD.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.WSSegment;

import java.util.Objects;

@Data
@NoArgsConstructor
public class Pathologist extends WSSegment {
    @JacksonXmlProperty(localName = "client")
    private Client client;
    @JacksonXmlProperty(localName = "contactInfo")
    private ContactInfo contactInfo;
    @JacksonXmlProperty(localName = "firstName")
    private String firstName;
    @JacksonXmlProperty(localName = "lastName")
    private String lastName;
    @JacksonXmlProperty(localName = "middleName")
    private String middleName;
    @JacksonXmlProperty(localName = "password")
    private String password;
    @JacksonXmlProperty(localName = "site")
    private String site;
    @JacksonXmlProperty(localName = "suffix")
    private String suffix;
    @JacksonXmlProperty(localName = "userId")
    private String userId;
    @JacksonXmlProperty(localName = "userRole")
    private String userRole;

    public static Pathologist fromPathologist(Message message, org.example.domain.message.professional.Pathologist entityPathologist) {
        Pathologist pathologist = new Pathologist();

        pathologist.client = Client.fromSpecimen(message.getFirstSpecimen());
        pathologist.contactInfo = ContactInfo.fromContactInfo(entityPathologist);
        pathologist.firstName = entityPathologist.getFirstName();
        pathologist.lastName = entityPathologist.getLastName();
        pathologist.middleName = entityPathologist.getMiddleName();
        //TODO check pathologist.site  =
        pathologist.suffix = entityPathologist.getSuffix();
        pathologist.userId = entityPathologist.getCode();
        pathologist.userRole = "Pathologist";

        return pathologist;
    }

    public String toString(int indentationLevel) {
        String pathologist = addIndentation(indentationLevel) + "<assignedPathologist>\n";

        indentationLevel ++;

        pathologist += nullSafe(client, Client::new).toString(indentationLevel) + "\n"
                + nullSafe(contactInfo, ContactInfo::new).toString(indentationLevel) + "\n"
                + addIndentation(indentationLevel) + "<firstName>" +  nullSafe(firstName) + "</firstName>\n"
                + addIndentation(indentationLevel) + "<lastName>" +  nullSafe(lastName) + "</lastName>\n"
                + addIndentation(indentationLevel) + "<middleName>" +  nullSafe(middleName) + "</middleName>\n"
                + addIndentation(indentationLevel) + "<password>" +  nullSafe(password) + "</password>\n"
                + addIndentation(indentationLevel) + "<site>" +  nullSafe(site) + "</site>\n"
                + addIndentation(indentationLevel) + "<suffix>" +  nullSafe(suffix) + "</suffix>\n"
                + addIndentation(indentationLevel) + "<userId>" +  nullSafe(userId) + "</userId>\n"
                + addIndentation(indentationLevel) + "<userRole>" +  nullSafe(userRole) + "</userRole>\n";

        indentationLevel --;

        pathologist += addIndentation(indentationLevel) + "</assignedPathologist>";

        return pathologist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pathologist that = (Pathologist) o;

        return Objects.equals(client, that.client)
                && Objects.equals(contactInfo, that.contactInfo)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName)
                && Objects.equals(middleName, that.middleName)
                && Objects.equals(password, that.password)
                && Objects.equals(site, that.site)
                && Objects.equals(suffix, that.suffix)
                && Objects.equals(userId, that.userId)
                && Objects.equals(userRole, that.userRole);
    }

}
