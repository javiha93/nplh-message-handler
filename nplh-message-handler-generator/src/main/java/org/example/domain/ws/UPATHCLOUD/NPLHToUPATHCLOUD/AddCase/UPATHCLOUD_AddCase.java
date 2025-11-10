package org.example.domain.ws.UPATHCLOUD.NPLHToUPATHCLOUD.AddCase;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.message.Message;
import org.example.domain.ws.UPATHCLOUD.common.CaseData;
import org.example.domain.ws.WSMessage;
import org.example.domain.ws.WSSegment;

import java.util.Objects;

@Data
@NoArgsConstructor

public class UPATHCLOUD_AddCase extends WSSegment implements WSMessage {
    @JacksonXmlProperty(localName = "arg0")
    private CaseData caseObject;

    public static UPATHCLOUD_AddCase fromMessage(Message message) {
        UPATHCLOUD_AddCase addCase = new UPATHCLOUD_AddCase();

        addCase.setCaseObject(CaseData.fromMessage(message));

        return addCase;
    }

    @Override
    public String toString() {
        int indentationLevel = 1;

        return  "<arg>\n" +
                caseObject.toString(indentationLevel) +
                "</arg>";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UPATHCLOUD_AddCase addCase = (UPATHCLOUD_AddCase) o;
        return Objects.equals(caseObject, addCase.caseObject);
    }

    public static UPATHCLOUD_AddCase fromXml(String xml) {
        UPATHCLOUD_AddCase addCase = new UPATHCLOUD_AddCase();

        addCase.caseObject = CaseData.fromXml(xml);

        return addCase;
    }
}
