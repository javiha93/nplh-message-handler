package org.example.domain.message.entity.list;

import lombok.Data;
import org.example.domain.message.Reflection;
import org.example.domain.message.entity.Block;
import org.example.domain.message.entity.Order;
import org.example.domain.message.entity.Slide;
import org.example.domain.message.entity.Specimen;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpecimensList extends Reflection implements Cloneable {
    private List<Specimen> specimenList;

    public SpecimensList() {
        Specimen specimen = new Specimen();
        List<Specimen> specimens = new ArrayList<>();
        specimens.add(specimen);

        this.specimenList = specimens;
    }

    public void mergeSpecimen(Specimen newSpecimen) {
        boolean isNewSpecimen = true;
        if ((specimenList == null) || (specimenList.isEmpty())) {
            specimenList = new ArrayList<>();
            specimenList.add(newSpecimen);
        }
        for(Specimen specimen : specimenList) {
            if (areSpecimenWithoutId(newSpecimen, specimen) || specimen.getId().equals(newSpecimen.getId())) {
                isNewSpecimen = false;
                List<Block> blockList = newSpecimen.getAllBlocks();
                for (Block block : blockList) {
                    specimen.getBlocks().mergeBlock(block);
                }
            }
        }
        if (isNewSpecimen) {
            specimenList.add(newSpecimen);
        }
    }

    private boolean areSpecimenWithoutId(Specimen existingSpecimen ,Specimen newSpecimen) {
        return (existingSpecimen.getId() == null) && (newSpecimen.getId() == null)  && (specimenList.size() == 1);
    }

    @Override
    public SpecimensList clone() {
        try {
            SpecimensList cloned = (SpecimensList) super.clone();
            if (specimenList == null) {
                return cloned;
            }
            List<Specimen> clonedSpecimenList = new ArrayList<>();
            for (Specimen specimen : this.specimenList) {
                clonedSpecimenList.add(specimen.clone());
            }
            cloned.setSpecimenList(clonedSpecimenList);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for SpecimensList", e);
        }
    }
}
