package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Tenant;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

public abstract class EditableImageGallery<T extends EditableImage> extends ImageGallery<T> {

    private static final String IMAGE_EDITOR_ENABLE_JS = "imageGallery.edit('%s',%s)";

    private ImageAnnotatingBehavior imageAnnotatingBehavior;

    public EditableImageGallery(String id, List<T> images) {
        super(id, images);
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior() {
            @Override
            protected EditableImage getEditableImage() {
                return getCurrentImage();
            }
        }.withEditing());
    }

    private String getImageEditorJs() {
        return String.format(IMAGE_EDITOR_ENABLE_JS,gallery.getMarkupId(),jsonRenderer.render(imageAnnotatingBehavior.getImageAnnotationOptions()));
    }

    @Override
    protected T saveImage(S3Service.S3ImagePath path, Tenant tenant) {
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


    class GalleryEditableImageJson extends GalleryImageJson {
        private List<ImageAnnotation> annotations;

        GalleryEditableImageJson(T image) {
            super(image);
            annotations = image.getAnnotations();
        }
    }

}
