package org.example.domain.ws.VSS.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.ws.WSSegment;


@Data
@NoArgsConstructor
public class Observation extends WSSegment {

    private String keyValueType;
    private StainProtocol stainProtocol;
    private String key;
    private String value;

    public static Observation fromStainingInfo(String key, String value, org.example.domain.message.entity.StainProtocol stainProtocol) {
        Observation observation = new Observation();

        observation.key = switch (key) {
            case "hostID" -> "StainingHostID";
            case "hostVersion" -> "StainingHostVersion";
            case "stainerSerialNumber" -> "StainerSerialNumber";
            case "stainerEffectiveType" -> "StainerType";
            case "runNumber" -> "StainingRunNumber";
            case "stainingRunStartTime" -> "StainingRunStartTime";
            case "runEstimatedTime" -> "StainingRunEstimatedMins";
            case "slidePosition" -> "StainingRunSlidePosition";
            case "stainingRunCompletedTime" -> "StainingRunCompletedTime";
            case "runTime" -> "StainingRunMins";
            case "shortName" -> "ShortName";
            case "stainerFriendlyName" -> "StainerFriendlyName";
            case "stainingBatchID" -> "StainingBatchID";
            default -> throw new IllegalArgumentException("Not supported field for observation: " + key);
        };
        observation.value = value;
        observation.keyValueType = "DT";
        observation.stainProtocol = StainProtocol.FromStainProtocol(stainProtocol);

        return observation;
    }

    public String toString(int indentationLevel) {
        return   addIndentation(indentationLevel) + "<Observation>\n"
                + addIndentation(indentationLevel + 1) + "<KeyValueType>" + nullSafe(keyValueType) + "</KeyValueType>\n"
                + nullSafe(stainProtocol, StainProtocol::new).toString(indentationLevel + 1, "Identifier") + "\n"
                + addIndentation(indentationLevel + 1) + "<Key>" + nullSafe(key) + "</Key>\n"
                + addIndentation(indentationLevel + 1) + "<Value>" + nullSafe(value) + "</Value>\n"
                + addIndentation(indentationLevel) + "</Observation>";
    }
}
