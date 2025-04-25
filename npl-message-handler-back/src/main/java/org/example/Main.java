
package org.example;

import org.example.domain.hl7.LIS.LISToNPLH.ADTA08.ADTA08;
import org.example.domain.hl7.LIS.LISToNPLH.ADTA28.ADTA28;
import org.example.domain.hl7.LIS.LISToNPLH.CASEUPDATE.CASEUPDATE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETECASE.DELETECASE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETESLIDE.DELETESLIDE;
import org.example.domain.hl7.LIS.LISToNPLH.DELETESPECIMEN.DELETESPECIMEN;
import org.example.domain.hl7.LIS.LISToNPLH.OML21.dto.OML21;
import org.example.domain.hl7.VTG.VTGToNPLH.BLOCKUPDATE.BlockUpdate;
import org.example.domain.message.Message;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;
import org.example.domain.message.entity.supplementalInfo.SpecialInstruction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

        // Create a test message
        Message message = Message.Default("test");
        message.getSpecimen("test;A")
                .addSupplementalInfo(
                        new SpecialInstruction("SpecialInstructionValue"));

        // Test all new FromMessage methods
        OML21 oml21 = OML21.FromMessage(message);
        ADTA08 adta08 = ADTA08.FromMessage(message);
        ADTA28 adta28 = ADTA28.FromMessage(message);
        CASEUPDATE caseupdate = CASEUPDATE.FromMessage(message, "IN_PROGRESS");
        DELETECASE deletecase = DELETECASE.FromMessage(message);
        
        Slide slide = message.getAllSlides().get(0);
        DELETESLIDE deleteslide = DELETESLIDE.FromMessage(message, slide);
        
        Specimen specimen = message.getAllSpecimens().get(0);
        DELETESPECIMEN deletespecimen = DELETESPECIMEN.FromMessage(message, specimen);

        Block block = message.getAllBlocks().get(0);
        BlockUpdate blockUpdate = BlockUpdate.FromMessage(message, block,"STAINING");
        // oewf = OEWF.FromMessage(message);

        // Convert all to strings
        String oml21String = oml21.toString();
        String adta08String = adta08.toString();
        String adta28String = adta28.toString();
        String caseUpdateString = caseupdate.toString();
        String deleteCaseString = deletecase.toString();
        String deleteSlideString = deleteslide.toString();
        String deleteSpecimenString = deletespecimen.toString();
        String blockUpdateString = blockUpdate.toString();
        //String oewfString = oewf.toString();
    }
}
