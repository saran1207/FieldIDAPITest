package com.n4systems.model.builders;

import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;

public class ProcedureDefinitionImageBuilder extends EditableImageBuilder<ProcedureDefinitionImage> {

    private ProcedureDefinition procedureDefinition;


    public static ProcedureDefinitionImageBuilder aProcedureDefinitionImage() {
        return new ProcedureDefinitionImageBuilder();
    }

    public ProcedureDefinitionImageBuilder() {
        super();
    }

    @Override
    public ProcedureDefinitionImage createObject() {
        ProcedureDefinitionImage image = new ProcedureDefinitionImage();
        return image;
    }

    @Override
    protected ProcedureDefinitionImage createObjectImpl() {
        return new ProcedureDefinitionImage();
    }
}
