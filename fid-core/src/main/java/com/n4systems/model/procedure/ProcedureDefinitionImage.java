package com.n4systems.model.procedure;

import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.*;

@Entity
@Table(name="procedure_definition_images")
public class ProcedureDefinitionImage extends EntityWithTenant {

    @ManyToOne
    @JoinColumn(name="procedure_definition_id")
    private ProcedureDefinition procedureDefinition;

    @Column(name="file_name")
    private String fileName;

    public ProcedureDefinition getProcedureDefinition() {
        return procedureDefinition;
    }

    public void setProcedureDefinition(ProcedureDefinition procedureDefinition) {
        this.procedureDefinition = procedureDefinition;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
