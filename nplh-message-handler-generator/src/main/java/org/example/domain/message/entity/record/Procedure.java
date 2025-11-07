package org.example.domain.message.entity.record;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.example.domain.message.Reflection;

@Data
public class Procedure extends Reflection implements Cloneable {

    Tissue tissue;
    Surgical surgical;
    Anatomic anatomic;

    public Procedure() {
        this.tissue = new Tissue();
        this.surgical = new Surgical();
        this.anatomic = new Anatomic();
    }

    public static Procedure Default()
    {
        Procedure procedure = new Procedure();

        procedure.setTissue(procedure.tissue.Default());
        procedure.setSurgical(procedure.surgical.Default());
        procedure.setAnatomic(procedure.anatomic.Default());

        return procedure;
    }

    @Override
    public Procedure clone() {
        try {
            Procedure cloned = (Procedure) super.clone();
            if (this.tissue != null) {
                cloned.setTissue(this.tissue.clone());
            } else {
                cloned.setTissue(null);
            }
            if (this.surgical != null) {
                cloned.setSurgical(this.surgical.clone());
            } else {
                cloned.setSurgical(null);
            }
            if (this.anatomic != null) {
                cloned.setAnatomic(this.anatomic.clone());
            } else {
                cloned.setAnatomic(null);
            }
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for Specimen", e);
        }
    }

    @Data
    @NoArgsConstructor
    public class Tissue extends Reflection implements Cloneable {
        String type;
        String description;
        String subtype;
        String subtypeDescription;


        @Override
        public Tissue clone() {
            try {
                return (Tissue) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning not supported for Specimen", e);
            }
        }

        public Tissue Default() {
            Tissue tissue = new Tissue();

            tissue.setType("Breast");
            tissue.setDescription("Left Breast Upper Node");

            return tissue;
        }
    }

    @Data
    @NoArgsConstructor
    public class Surgical extends Reflection implements Cloneable {
        String name;
        String description;

        @Override
        public Surgical clone() {
            try {
                return (Surgical) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning not supported for Specimen", e);
            }
        }

        public Surgical Default() {
            Surgical surgical = new Surgical();

            surgical.setName("Breast Biopsy");

            return surgical;
        }
    }

    @Data
    @NoArgsConstructor
    public class Anatomic extends Reflection implements Cloneable {
        String site;
        String description;

        public Anatomic Default() {
            Anatomic anatomic = new Anatomic();

            anatomic.setSite("Anatomic site");
            anatomic.setDescription("Anatomic description");

            return anatomic;
        }

        @Override
        public Anatomic clone() {
            try {
                return (Anatomic) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException("Cloning not supported for Anatomic", e);
            }
        }
    }
}
