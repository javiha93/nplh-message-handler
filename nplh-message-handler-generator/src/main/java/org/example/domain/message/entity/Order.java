package org.example.domain.message.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import org.example.domain.message.Reflection;
import org.example.domain.message.entity.list.SpecimensList;
import org.example.domain.message.professional.Pathologist;
import org.example.domain.message.professional.Technician;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class Order extends Reflection implements Cloneable {
    private String entityName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registerDate;
    private String actionCode;
    private String status;
    private String sampleId;
    private String extSampleId;
    private String prefix;
    private String originCode;
    private String originDescription;
    private String workFlow;
    private String stat;
    private String tags;
    private Pathologist pathologist;
    private Technician technician;
    private SpecimensList specimens;

    public Order() {
        this.entityName = "CASE";
        this.specimens = new SpecimensList();
        this.technician = new Technician();
        this.pathologist = new Pathologist();
    }

    public static Order OneSpecimen(String sampleId) {
        Order order = new Order();

        order.setRegisterDate("20240518170503");
        order.setActionCode("NW");
        order.setSampleId(sampleId);

        order.setSpecimen(Specimen.OneBlock( sampleId + ";A", "A"));
        order.setPathologist(Pathologist.Default());
        order.setTechnician(Technician.Default());

        return order;
    }

    public void setRegisterDate(String registerDate) {
        if ((registerDate == null) || (registerDate.isEmpty())){
            return;
        }
        DateTimeFormatter formatter;
        if (registerDate.getBytes().length == 14) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        } else if (registerDate.getBytes().length == 8) {
            formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        } else if (registerDate.getBytes().length == 19) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        }
        else {
            return;
        }
        this.registerDate = LocalDateTime.parse(registerDate, formatter);
    }

    public void setSpecimenList(List<Specimen> specimens) {
        this.specimens.setSpecimenList(specimens);
    }

    public List<Slide> getAllSlides() {
        List<Specimen> specimenList = specimens.getSpecimenList();
        List<Slide> slidesList = new ArrayList<>();
        for (Specimen specimen : specimenList) {
            List<Slide> slidesFromSpecimen = specimen.getAllSlides();
            slidesList.addAll(slidesFromSpecimen);
        }
        return slidesList;
    }

    public Slide getSlide() {
        return getSingleSpecimen().getSlide();
    }

    public void addSlide(Slide slide) {
        getSingleSpecimen().addSlide(slide);
    }

    public void setSlide(Slide slide) {
        getSingleSpecimen().setSlide(slide);
    }

    public List<Block> getAllBlocks() {
        List<Specimen> specimenList = specimens.getSpecimenList();
        List<Block> blockList = new ArrayList<>();
        for (Specimen specimen : specimenList) {
            List<Block> blocksFromSpecimen = specimen.getAllBlocks();
            blockList.addAll(blocksFromSpecimen);
        }
        return blockList;
    }

    public Block getBlock() {
        return getSingleSpecimen().getBlock();
    }

    public void addBlock(Block block) {
        getSingleSpecimen().addBlock(block);
    }

    public void setBlock(Block block) {
        getSingleSpecimen().setBlock(block);
    }

    public List<Specimen> getAllSpecimen() {
        return specimens.getSpecimenList();
    }

    public Specimen getSingleSpecimen() {
        if (specimens.getSpecimenList().size() == 1) {
            return specimens.getSpecimenList().get(0);
        } else {
            throw new RuntimeException("Not possible to return only a specimen. Order contains " + specimens.getSpecimenList().size() + " specimens" );
        }
    }

    public Specimen getFirstSpecimen() {
        return specimens.getSpecimenList().get(0);
    }

    public void setSpecimen(Specimen specimen) {
        SpecimensList specimenList = new SpecimensList();
        List<Specimen> specimens = new ArrayList<>(List.of(specimen));
        specimenList.setSpecimenList(specimens);
        setSpecimens(specimenList);
    }

    public void addSpecimen(Specimen specimen) {
        this.specimens.getSpecimenList().add(specimen);
    }

    @Override
    public Order clone() {
        try {
            Order cloned = (Order) super.clone();
            if (this.pathologist != null) {
                cloned.setPathologist(this.pathologist.clone());
            } else {
                cloned.setPathologist(null);
            }
            if (this.technician != null) {
                cloned.setTechnician(this.technician.clone());
            } else {
                cloned.setTechnician(null);
            }
            if (this.specimens != null) {
                cloned.setSpecimens(this.specimens.clone());
            } else {
                cloned.setSpecimens(null);
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for Order", e);
        }
    }

}

