
package org.example.domain.message.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import org.example.domain.message.Message;
import org.example.domain.message.entity.list.SlidesList;
import org.example.domain.message.entity.list.SupplementalInfoList;
import org.example.domain.message.entity.record.EntityInfo;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Block extends EntityInfo implements Cloneable {
    private String parentId;
    private SlidesList slides;
    private SupplementalInfoList supplementalInfos;

    public Block() {
        setEntityName("BLOCK");
        this.slides = new SlidesList();
        // Do not initialize supplementalInfos by default
    }

    public static Block OneSlide(String id, String sequence) {
        Block block = new Block();

        block.setId(id);
        block.setSequence(sequence);

        Slide slide = Slide.Default( id + ";1", "1", "NW");

        block.setSlide(slide);

        return block;
    }

    public static Block MultipleSlides(String id, String sequence, int slideNumber) {
        Block block = new Block();

        block.setId(id);
        block.setSequence(sequence);
        block.setExternalId(id);

        for (int i = 1; i <= slideNumber; i++) {
            Slide slide = Slide.Default(id + i, String.valueOf(i), "NW");
            block.addSlide(slide);
        }

        return block;
    }

    public List<Slide> getAllSlides() {
        return this.slides.getSlideList();
    }

    public void addSlide(Slide slide) {
        slides.getSlideList().add(slide);
    }

    public void setSlideList(List<Slide> slides) {
        this.slides.setSlideList(slides);
    }

    public Specimen getSpecimenParent(Message message) {
        List<Specimen> specimens = message.getAllSpecimens();
        for (Specimen specimen : specimens) {
            if (specimen.getAllBlocks().contains(this)) {
                return specimen.clone();
            }
        }
        return null;
    }

    public Slide getSlide() {
        if (slides.getSlideList().size() == 1) {
            return slides.getSlideList().get(0);
        } else {
            throw new RuntimeException("Not possible to return only a order. Block contains " + slides.getSlideList().size() + " slides" );
        }
    }

    public void setSlide(Slide slide) {
        SlidesList slidesList = new SlidesList();
        List<Slide> slides = new ArrayList<>(List.of(slide));
        slidesList.setSlideList(slides);
        setSlides(slidesList);
    }

    public boolean hasSlide() {
        return !slides.getSlideList().isEmpty();
    }

    public void addSupplementalInfo(SupplementalInfo supplementalInfo) {
        if (this.supplementalInfos == null) {
            this.supplementalInfos = new SupplementalInfoList();
        }
        supplementalInfo.setArtifact("BLOCK");
        supplementalInfos.getSupplementalInfoList().add(supplementalInfo);
    }

    @Override
    public Block clone() {
        try {
            Block cloned = (Block) super.clone();
            if (this.slides != null) {
                cloned.setSlides(this.slides.clone());
            } else {
                cloned.setSlides(null);
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
