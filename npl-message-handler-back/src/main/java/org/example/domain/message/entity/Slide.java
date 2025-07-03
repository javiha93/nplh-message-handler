package org.example.domain.message.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.example.domain.message.Message;
import org.example.domain.message.Reflection;
import org.example.domain.message.entity.list.SlidesList;
import org.example.domain.message.entity.list.SupplementalInfoList;
import org.example.domain.message.entity.record.EntityInfo;
import org.example.domain.message.entity.supplementalInfo.SpecialInstruction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Slide extends EntityInfo implements Cloneable {
    private String actionCode;
    private StainProtocol stainProtocol;
    private Control control;
    private String comment;
    private String labelPrinted;
    private Boolean isRescanned;
    private String rescanReason;
    private String rescanComment;
    private List<Reagent> reagents = new ArrayList<>();
    private SupplementalInfoList supplementalInfos;


    public String getIsRescanned() {
        if (isRescanned == null) {
            return null;
        }
        return isRescanned.toString();
    }

    public void setIsRescanned(String isRescanned) {
        this.isRescanned = Boolean.parseBoolean(isRescanned);
    }

    public void setIsRescanned(Boolean isRescanned) {
        this.isRescanned = isRescanned;
    }

    public Slide() {
        setEntityName("SLIDE");
        this.stainProtocol = new StainProtocol();
        this.control = new Control();
        this.supplementalInfos = new SupplementalInfoList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Slide slide = (Slide) o;
        return getId() != null ? getId().equals(slide.getId()) : slide.getId() == null;
    }

    public static Slide Default(String id, String sequence, String actionCode) {
        Slide slide = new Slide();

        slide.setId(id);
        slide.setSequence(sequence);
        slide.setExternalId(id);

        slide.setActionCode(actionCode);
        slide.setStainProtocol(StainProtocol.Default());
        slide.setComment("Comment");
        slide.setIsRescanned(false);

        return slide;
    }

    @Data
    @NoArgsConstructor
    public class Control extends Reflection implements Cloneable {
        String name;
        String description;
        String scoring;
        String clone;
        String vendor;

        @Override
        public Control clone() {
            try {
                return (Control) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning not supported for Technician", e);
            }
        }

        public Control Default() {
            Control control = new Control();

            control.setName("controlName");
            control.setDescription("controlDescription");
            control.setScoring("scoring");
            control.setClone("clone");
            control.setVendor("vendor");

            return control;
        }
    }

    public Block getBlockParent(Message message) {
        List<Block> blocks = message.getAllBlocks();
        for (Block block : blocks) {
            if (block.getAllSlides().contains(this)) {
                return block.clone();
            }
        }
        return null;
    }

    public Order getOrderSlide(Message message) {
        Block block = getBlockParent(message);
        Specimen specimen = block.getSpecimenParent(message);
        Order order = specimen.getOrderParent(message);

        block.setSlideList(List.of(this));
        specimen.setBlockList(List.of(block));
        order.setSpecimenList(List.of(specimen));

        return order;
    }

    public void addSupplementalInfo(SupplementalInfo supplementalInfo) {
        supplementalInfo.setArtifact("SLIDE");
        supplementalInfos.getSupplementalInfoList().add(supplementalInfo);
    }

    public void addReagent(Reagent reagent) {
        reagents.add(reagent);
    }

    @Override
    public Slide clone() {
        try {
            Slide cloned = (Slide) super.clone();
            if (this.stainProtocol != null) {
                cloned.setStainProtocol(this.stainProtocol.clone());
            } else {
                cloned.setStainProtocol(null);
            }
            if (this.control != null) {
                cloned.setControl(this.control.clone());
            } else {
                cloned.setControl(null);
            }
            if (this.supplementalInfos != null) {
                cloned.setSupplementalInfos(this.supplementalInfos.clone());
            } else {
                cloned.setSupplementalInfos(null);
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for Specimen", e);
        }
    }
}
