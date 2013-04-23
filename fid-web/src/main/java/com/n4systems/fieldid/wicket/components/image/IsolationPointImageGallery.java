package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URL;

import static ch.lambdaj.Lambda.on;

public class IsolationPointImageGallery extends EditableImageGallery<ProcedureDefinitionImage> {

    private @SpringBean S3Service s3Service;

    private final ProcedureDefinition procedureDefinition;
    private final IModel<IsolationPoint> model;

    public IsolationPointImageGallery(String id, ProcedureDefinition procedureDefinition, IModel<IsolationPoint> model) {
        super(id, procedureDefinition.getImages(), ProxyModel.of(model, on(IsolationPoint.class).getAnnotation()));
        this.model = model;
        this.procedureDefinition = procedureDefinition;
        withDoneButton();
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
        // TODO DD : when/how can i remove these? if they have no annotations then they shouldn't exist.
        procedureDefinition.getImages().add(image);
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
