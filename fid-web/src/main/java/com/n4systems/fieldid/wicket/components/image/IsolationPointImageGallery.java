package com.n4systems.fieldid.wicket.components.image;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URL;

public class IsolationPointImageGallery extends EditableImageGallery<ProcedureDefinitionImage> {

    private @SpringBean S3Service s3Service;

    private final ProcedureDefinition procedureDefinition;
    private final IModel<IsolationPoint> model;

    public IsolationPointImageGallery(String id, ProcedureDefinition procedureDefinition, IModel<IsolationPoint> model) {
        super(id, new PropertyModel(procedureDefinition,"images"));
        this.model = model;
        this.procedureDefinition = procedureDefinition;
        withDoneButton();
    }

    @Override
    protected void uploadImage(ProcedureDefinitionImage image, String contentType, byte[] bytes, String clientFileName) {
        s3Service.uploadTempProcedureDefImage(image, contentType, bytes);
        image.setFileName(clientFileName);
    }

    @Override
    protected ProcedureDefinitionImage createImage(String path) {
        ProcedureDefinitionImage image = new ProcedureDefinitionImage();
        image.setTenant(procedureDefinition.getTenant());
        image.setFileName(path);
        image.setProcedureDefinition(procedureDefinition);
        // TODO DD : when/how can i remove these images? if they have no annotations then they shouldn't exist.
        procedureDefinition.addImage(image);
        return image;
    }

    @Override
    protected void doLabel(ProcedureDefinitionImage image, ImageAnnotation annotation) {
        super.doLabel(image, annotation);
        Preconditions.checkState(annotation.getImage().equals(image));
        model.getObject().setAnnotation(annotation);
    }

    @Override
    protected ImageAnnotation getAnnotation() {
        return model.getObject().getAnnotation();
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

    @Override
    protected ImageAnnotationType getDefaultType() {
        return ImageAnnotationType.fromIsolationPointSourceType(model.getObject().getSourceType());
    }

    @Override
    protected String getDefaultText() {
        return model.getObject().getIdentifier();
    }
}
