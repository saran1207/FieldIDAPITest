package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Tenant;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;

public class IsolationPointImageGallery extends EditableImageGallery<ProcedureDefinitionImage> {

    private final String IMAGE_PATH = "/tenants/%d/loto/%d/images/";
    private final ProcedureDefinition procedureDefinition;

    public IsolationPointImageGallery(String id, ProcedureDefinition procedureDefinition, ImageAnnotation annotation) {
        super(id, procedureDefinition.getImages(), annotation);
        withDoneButton();
        this.procedureDefinition = procedureDefinition;
    }

    @Override protected ProcedureDefinitionImage createImage(S3Service.S3ImagePath path, Tenant tenant) {
        ProcedureDefinitionImage image = new ProcedureDefinitionImage();
        image.setTenant(tenant);
        image.setFileName(path.getOrigPath());
        image.setProcedureDefinition(procedureDefinition);
        return image;
    }

    @Override protected String getFileName(String fileName) {
        return String.format(IMAGE_PATH,procedureDefinition.getTenant().getId(),procedureDefinition.getId());
    }
}
