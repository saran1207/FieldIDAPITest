package com.n4systems.model.procedure;

import com.n4systems.model.common.EditableImage;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name="procedure_definition_images")
@Inheritance(strategy= InheritanceType.JOINED)
@Cacheable
@org.hibernate.annotations.Cache(region = "ProcedureCache", usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProcedureDefinitionImage extends EditableImage {

    @ManyToOne
    @JoinColumn(name="procedure_definition_id")
    private ProcedureDefinition procedureDefinition;

    public ProcedureDefinition getProcedureDefinition() {
        return procedureDefinition;
    }

    public void setProcedureDefinition(ProcedureDefinition procedureDefinition) {
        this.procedureDefinition = procedureDefinition;
    }

}
