package org.example.domain.message.entity.list;

import lombok.Data;
import org.example.domain.message.Reflection;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Slide;

import java.util.ArrayList;
import java.util.List;

@Data
public class SlidesList extends Reflection implements Cloneable{
    private List<Slide> slideList;

    public SlidesList() {
        Slide slide = new Slide();
        List<Slide> slides = new ArrayList<>();
        slides.add(slide);

        this.slideList = new ArrayList<>(slides);
    }

    public void mergeSlide(Slide newSlide) {
        if ((slideList == null) || (slideList.isEmpty())) {
            slideList = new ArrayList<>();
            slideList.add(newSlide);
        }
        boolean isNewSlide = true;
        for( Slide slide : slideList) {
            if (slide.getId().equals(newSlide.getId())) {
                isNewSlide = false;
            }
        }
        if (isNewSlide) {
            slideList.add(newSlide);
        }
    }

    @Override
    public SlidesList clone() {
        try {
            SlidesList cloned = (SlidesList) super.clone();
            if (slideList == null) {
                return cloned;
            }
            List<Slide> clonedSlideList = new ArrayList<>();
            for (Slide slide : this.slideList) {
                clonedSlideList.add(slide.clone());
            }
            cloned.setSlideList(clonedSlideList);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for SpecimensList", e);
        }
    }
}
