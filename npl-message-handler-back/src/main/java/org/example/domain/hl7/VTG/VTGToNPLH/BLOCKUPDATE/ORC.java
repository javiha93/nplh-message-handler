package org.example.domain.hl7.VTG.VTGToNPLH.BLOCKUPDATE;

import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;

public class ORC extends org.example.domain.hl7.common.ORC {

    public static ORC Default(String sampleID, String blockStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.Default(sampleID);

        orc.setMessageCode("SC");
        orc.setActionCode("IP");
        orc.setBlockStatus(blockStatus);

        return orc;
    }

    public static ORC FromMessage(Block block, Message message, String blockStatus) {
        ORC orc = (ORC) org.example.domain.hl7.common.ORC.FromMessage(block, message, new ORC());
        orc.setMessageCode("SC");
        orc.setActionCode("IP");
        orc.setBlockStatus(blockStatus);

        return orc;
    }

    @Override
    public String toString() {
            String value = "ORC|" +
                    nullSafe(getMessageCode()) + "||" +        // 1
                    nullSafe(getBlockId()) + "||" +      // 3
                    nullSafe(getActionCode()) + "||||||||||||||||||||" + // 5
                    nullSafe(getBlockStatus()) + "|";         // 21

            return cleanSegment(value);
    }
}
