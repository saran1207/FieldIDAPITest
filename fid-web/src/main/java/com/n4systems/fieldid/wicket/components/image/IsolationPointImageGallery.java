package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URL;

public class IsolationPointImageGallery extends EditableImageGallery<ProcedureDefinitionImage> {

    private @SpringBean S3Service s3Service;

    private final ProcedureDefinition procedureDefinition;

    public IsolationPointImageGallery(String id, ProcedureDefinition procedureDefinition, ImageAnnotation annotation) {
        super(id, procedureDefinition.getImages(), annotation);
        withDoneButton();
        this.procedureDefinition = procedureDefinition;
    }

    @Override
    protected void uploadImage(ProcedureDefinitionImage image, byte[] bytes, String contentType, String clientFileName) {
        s3Service.uploadProcedureDefImage(image, bytes, contentType, clientFileName);
    }

    @Override
    protected ProcedureDefinitionImage createImage(String path) {
        ProcedureDefinitionImage image = new ProcedureDefinitionImage();
        image.setTenant(procedureDefinition.getTenant());
        image.setFileName(path);
        image.setProcedureDefinition(procedureDefinition);
        return image;
    }

    @Override
    protected String getImageUrl(ProcedureDefinitionImage image) {
        URL url = s3Service.getProcedureDefinitionImageMediumURL(image);
        return url.toString();
    }

    @Override
    protected String getThumbnailImageUrl(ProcedureDefinitionImage image) {
        URL url = s3Service.getProcedureDefinitionImageThumbnailURL(image);
        return url.toString();
    }
}
