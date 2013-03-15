package com.n4systems.model.procedure;

import com.n4systems.model.parents.EntityWithTenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
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
