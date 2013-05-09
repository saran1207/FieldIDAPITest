package com.n4systems.fieldid.wicket.components.image;

import com.google.common.base.Preconditions;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.uuid.AtomicLongService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.model.procedure.IsolationPoint;
import com.n4systems.model.procedure.ProcedureDefinition;
import com.n4systems.model.procedure.ProcedureDefinitionImage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URL;

public class IsolationPointImageGallery extends EditableImageGallery<ProcedureDefinitionImage> {

    private @SpringBean S3Service s3Service;
    private @SpringBean AtomicLongService atomicLongService;

    private final ProcedureDefinition procedureDefinition;
    private final IModel<IsolationPoint> model;

    public IsolationPointImageGallery(String id, ProcedureDefinition procedureDefinition, IModel<IsolationPoint> model) {
        super(id, new PropertyModel(procedureDefinition,"images"));
        this.model = model;
        this.procedureDefinition = procedureDefinition;
        withDoneButton();
    }

    @Override
    protected Component createInstructions(String id) {
        return new Label(id,new FIDLabelModel("label.annotate_instructions"));
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
        procedureDefinition.addImage(image);
        return image;
    }

    @Override
    protected void doNote(ProcedureDefinitionImage image, ImageAnnotation annotation) {
        super.doNote(image, annotation);
        Preconditions.checkState(annotation.getImage().equals(image));
        getIsolationPoint().setAnnotation(annotation);
        getIsolationPoint().setIdentifier(annotation.getText());
    }

    private IsolationPoint getIsolationPoint() {
        return model.getObject();
    }

    @Override
    protected ImageAnnotation getAnnotation() {
        return getIsolationPoint().getAnnotation();
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
        return ImageAnnotationType.fromIsolationPointSourceType(getIsolationPoint().getSourceType());
    }

    @Override
    protected String getDefaultText() {
        return getIsolationPoint().getIdentifier();
    }

    @Override
    protected String getPlaceholderImageUrl() {
        return "/fieldid/images/loto/upload-lightbox-blank-slate.png";
    }

    @Override
    protected Long getIntialImageIndex() {
        Long suggestedIndex = super.getIntialImageIndex();
        if (suggestedIndex==null) {
            ImageAnnotation annotation = getIsolationPoint().getAnnotation();
            return annotation!=null ? getIndexOfImage(annotation.getImage()) : null;
        } else {
            return suggestedIndex;
        }
    }

    @Override
    protected ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type) {
        ImageAnnotation annotation = getIsolationPoint().getAnnotation();
        if (annotation==null && id==null) {   // create new one.
            annotation = new ImageAnnotation(x,y,text,type);
            annotation.setTempId(atomicLongService.getNext());  // use temporary # for non-persisted annotations.  not globally unique, but jvm unique.
        }
        Preconditions.checkState(annotation!=null, "couldn't find annotation with id " + id);

        annotation.setX(x);
        annotation.setY(y);
        annotation.setText(text);
        annotation.setType(type);
        return annotation;
    }

}
