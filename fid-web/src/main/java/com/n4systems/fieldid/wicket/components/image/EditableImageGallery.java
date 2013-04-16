package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Tenant;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

public abstract class EditableImageGallery<T extends EditableImage> extends ImageGallery<T> {

    private static final String IMAGE_EDITOR_ENABLE_JS = "imageGallery.edit('%s',%s)";

    private ImageAnnotatingBehavior imageAnnotatingBehavior;

    private final ImageAnnotation annotation;

    public EditableImageGallery(String id, List<T> images, final ImageAnnotation annotation) {
        super(id, images);
        this.annotation = annotation;
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior<T>() {
            @Override protected T getEditableImage() {
                return getCurrentImage();
            }
            @Override protected String getDefaultType() {
                return annotation.getType().getCssClass();
            }
        }.withEditing());
    }

    private String getImageEditorJs() {
        return String.format(IMAGE_EDITOR_ENABLE_JS,gallery.getMarkupId(),jsonRenderer.render(imageAnnotatingBehavior.getImageAnnotationOptions()));
    }

    @Override
    protected T addImage(S3Service.S3ImagePath path, Tenant tenant) {
        T image = createImage(path, tenant);
        image.setTenant(tenant);
        persistenceService.save(image);
        return image;
    }

    @Override
    protected void imageClicked(AjaxRequestTarget target, String action, T image) {
        super.imageClicked(target, action, image);
        target.appendJavaScript(getImageEditorJs());
    }

    @Override
    protected GalleryImageJson createImageJson(T image) {
        return new GalleryEditableImageJson(image);
    }

    @Override
    protected GalleryOptions createGalleryOptions(List<GalleryImageJson> images) {
        return super.createGalleryOptions(images);
    }

    class GalleryEditableImageJson extends GalleryImageJson {
        private List<ImageAnnotation> annotations;

        GalleryEditableImageJson(T image) {
            super(image);
            annotations = image.getAnnotations();
        }
    }

    class EditableGalleryOptions extends GalleryOptions {
        Long editedId = annotation!=null ? annotation.getId() : null;
        String type= annotation!=null ? annotation.getType().getCssClass() : ImageAnnotationType.getDefault().getCssClass();

        EditableGalleryOptions(List data) {
            super(data);
        }
    }

}
