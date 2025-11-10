package org.example.domain.ws.UPATHCLOUD.common;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.entity.SupplementalInfo;
import org.example.domain.message.entity.list.SupplementalInfoList;
import org.example.domain.ws.WSSegment;

import java.util.Objects;

@Data
@NoArgsConstructor
public class ContactInfo extends WSSegment {

    @JacksonXmlProperty(localName = "address1")
    private String address1;
    @JacksonXmlProperty(localName = "address2")
    private String address2;
    @JacksonXmlProperty(localName = "city")
    private String city;
    @JacksonXmlProperty(localName = "country")
    private String country;
    @JacksonXmlProperty(localName = "email")
    private String email;
    @JacksonXmlProperty(localName = "homeTel")
    private String homeTel;
    @JacksonXmlProperty(localName = "mobileTel")
    private String mobileTel;
    @JacksonXmlProperty(localName = "state")
    private String state;
    @JacksonXmlProperty(localName = "workTel")
    private String workTel;
    @JacksonXmlProperty(localName = "zip")
    private String zip;

    public static ContactInfo fromContactInfo(org.example.domain.message.professional.record.ContactInfo entityContactInfo) {
        ContactInfo contactInfo = new ContactInfo();

        contactInfo.address1 = entityContactInfo.getAddress();
        contactInfo.address2 = entityContactInfo.getAddress2();
        contactInfo.city = entityContactInfo.getCity();
        contactInfo.country = entityContactInfo.getCountry();
        contactInfo.email = entityContactInfo.getEmail();
        contactInfo.homeTel = entityContactInfo.getHomePhone();
        contactInfo.mobileTel = entityContactInfo.getMobile();
        contactInfo.state = entityContactInfo.getState();
        contactInfo.workTel = entityContactInfo.getWorkPhone();
        contactInfo.zip = entityContactInfo.getZip();

        return contactInfo;
    }

    public String toString(int indentationLevel) {

        String contactInfo  = addIndentation(indentationLevel) + "<contactInfo>\n";

        indentationLevel ++;

        contactInfo += addIndentation(indentationLevel) + "<address1>" +  nullSafe(address1) + "</address1>\n"
                + addIndentation(indentationLevel) + "<address2>" +  nullSafe(address2) + "</address2>\n"
                + addIndentation(indentationLevel) + "<city>" +  nullSafe(city) + "</city>\n"
                + addIndentation(indentationLevel) + "<country>" +  nullSafe(country) + "</country>\n"
                + addIndentation(indentationLevel) + "<email>" +  nullSafe(email) + "</email>\n"
                + addIndentation(indentationLevel) + "<homeTel>" +  nullSafe(homeTel) + "</homeTel>\n"
                + addIndentation(indentationLevel) + "<mobileTel>" +  nullSafe(mobileTel) + "</mobileTel>\n"
                + addIndentation(indentationLevel) + "<state>" +  nullSafe(state) + "</state>\n"
                + addIndentation(indentationLevel) + "<workTel>" +  nullSafe(workTel) + "</workTel>\n"
                + addIndentation(indentationLevel) + "<zip>" +  nullSafe(zip) + "</zip>\n";

        indentationLevel --;

        contactInfo += addIndentation(indentationLevel) + "</contactInfo>";

        return contactInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactInfo that = (ContactInfo) o;

        return Objects.equals(address1, that.address1)
                && Objects.equals(address2, that.address2)
                && Objects.equals(city, that.city)
                && Objects.equals(country, that.country)
                && Objects.equals(email, that.email)
                && Objects.equals(homeTel, that.homeTel)
                && Objects.equals(mobileTel, that.mobileTel)
                && Objects.equals(state, that.state)
                && Objects.equals(workTel, that.workTel)
                && Objects.equals(zip, that.zip);
    }

}
