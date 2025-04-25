package org.example.domain.message.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.Value;
import org.example.domain.message.Message;
import org.example.domain.message.entity.list.BlocksList;
import org.example.domain.message.entity.list.SlidesList;
import org.example.domain.message.entity.list.SpecimensList;
import org.example.domain.message.entity.list.SupplementalInfoList;
import org.example.domain.message.entity.record.EntityInfo;
import org.example.domain.message.entity.record.Procedure;
import org.example.domain.message.professional.Pathologist;
import org.example.domain.message.professional.Technician;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Specimen extends EntityInfo implements Cloneable {
    private LocalDateTime collectDateTime;
    private LocalDateTime receivedDateTime;
    private Procedure procedure;
    private String facilityCode;
    private String facilityName;
    private BlocksList blocks;
    private SupplementalInfoList supplementalInfos;

    public Specimen() {
        setEntityName("SPECIMEN");
        this.procedure = new Procedure();
        this.blocks = new BlocksList();
        this.supplementalInfos = new SupplementalInfoList();
    }

    public static Specimen OneBlock(String id, String sequence) {
        Specimen specimen = new Specimen();

        specimen.setId(id);
        specimen.setSequence(sequence);
        specimen.setExternalId(id);
        specimen.setCollectDateTime("20141014");
        specimen.setFacilityCode("FC");
        specimen.setFacilityName("FName");
        specimen.setProcedure(Procedure.Default());

        Block block = Block.OneSlide( id + ";1", "1");

        specimen.setBlock(block);

        return specimen;
    }

    public void setCollectDateTime(String collectDateTime) {
        if ((collectDateTime == null) || (collectDateTime.isEmpty())){
            return;
        }
        DateTimeFormatter formatter;
        if (collectDateTime.getBytes().length == 14) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            this.collectDateTime = LocalDateTime.parse(collectDateTime, formatter);
        } else if (collectDateTime.getBytes().length == 8) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(collectDateTime, formatter);
            this.collectDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        }
    }

    public String getCollectDateTime() {
        if (collectDateTime == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTime = collectDateTime.format(formatter);
        if (dateTime.endsWith("000000")) {
            return dateTime.substring(0, 8);
        }
        return dateTime;
    }

    public void setReceivedDateTime(String receivedDateTime) {
        if ((receivedDateTime == null) || (receivedDateTime.isEmpty())){
            return;
        }
        DateTimeFormatter formatter;
        if (receivedDateTime.getBytes().length == 14) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            this.receivedDateTime = LocalDateTime.parse(receivedDateTime, formatter);
        } else if (receivedDateTime.getBytes().length == 8) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate localDate = LocalDate.parse(receivedDateTime, formatter);
            this.receivedDateTime = LocalDateTime.of(localDate, LocalTime.MIDNIGHT);
        }
    }

    public String getReceivedDateTime() {
        if (receivedDateTime == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTime = receivedDateTime.format(formatter);
        if (dateTime.endsWith("000000")) {
            return dateTime.substring(0, 8);
        }
        return dateTime;
    }

    public void addBlock(Block block) {
        this.blocks.getBlockList().add(block);
    }

    public void setBlockList(List<Block> blocks) {
        this.blocks.setBlockList(blocks);
    }

    public List<Slide> getAllSlides() {
        List<Block> blocksList = blocks.getBlockList();
        List<Slide> slidesList = new ArrayList<>();
        for (Block block : blocksList) {
            List<Slide> slidesFromBlock = block.getAllSlides();
            slidesList.addAll(slidesFromBlock);
        }
        return slidesList;
    }

    public void addSlide(Slide slide) {
        this.getBlock().addSlide(slide);
    }
    public void setSlide(Slide slide) {
        this.getBlock().setSlide(slide);
    }

    public Slide getSlide() {
        return this.getBlock().getSlide();
    }

    public List<Block> getAllBlocks() {
        return blocks.getBlockList();
    }

    public Block getBlock() {
        if (blocks.getBlockList().size() == 1) {
            return blocks.getBlockList().get(0);
        } else {
            throw new RuntimeException("Not possible to return only a block. Specimen contains " + blocks.getBlockList().size() + " blocks" );
        }
    }

    public void setBlock(Block block) {
        BlocksList blocksList = new BlocksList();
        List<Block> blocks = new ArrayList<>(List.of(block));
        blocksList.setBlockList(blocks);
        setBlocks(blocksList);
    }

    public Order getOrderParent(Message message) {
        List<Order> orders = message.getAllOrders();
        for (Order order : orders) {
            if (order.getAllSpecimen().contains(this)) {
                return order.clone();
            }
        }
        return null;
    }

    public void addSupplementalInfo(SupplementalInfo supplementalInfo) {
        supplementalInfo.setArtifact("PART");
        supplementalInfos.getSupplementalInfoList().add(supplementalInfo);
    }

    @Override
    public Specimen clone() {
        try {
            Specimen cloned = (Specimen) super.clone();
            if (this.procedure != null) {
                cloned.setProcedure(this.procedure.clone());
            } else {
                cloned.setProcedure(null);
            }
            if (this.blocks != null) {
                cloned.setBlocks(this.blocks.clone());
            } else {
                cloned.setBlocks(null);
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for Specimen", e);
        }
    }
}
