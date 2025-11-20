package org.example.domain.message.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StainingInfo {
    public String hostID;
    public String hostVersion;
    public String slidePosition;
    public String shortName;
    public String runNumber;
    public String runTime;
    public String runEstimatedTime;
    public String stainerSerialNumber;
    public String stainerEffectiveType;
    public String stainingRunStartTime;
    public String stainingRunCompletedTime;
    public String stainerFriendlyName;
    public String stainingBatchID;

    public static StainingInfo fullStainingInfo() {
        StainingInfo stainingInfo = new StainingInfo();

        stainingInfo.hostID = "6";
        stainingInfo.hostVersion = "2.5.1";
        stainingInfo.slidePosition = "5";
        stainingInfo.shortName = "shortName";
        stainingInfo.runNumber = "1001";
        stainingInfo.runTime = "45";
        stainingInfo.runEstimatedTime = "50";
        stainingInfo.stainerSerialNumber = "SN9000-A";
        stainingInfo.stainerEffectiveType = "TipoDeStainerX";
        stainingInfo.stainingRunStartTime = "2025-11-14T10:00:00Z";
        stainingInfo.stainingRunCompletedTime = "2025-11-14T10:45:00Z";
        stainingInfo.stainerFriendlyName = "Stainer de Laboratorio 1";
        stainingInfo.stainingBatchID = "4";

        return stainingInfo;
    }
}
