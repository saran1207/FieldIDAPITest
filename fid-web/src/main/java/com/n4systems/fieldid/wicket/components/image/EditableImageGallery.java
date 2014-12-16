package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import java.util.List;

public abstract class EditableImageGallery<T extends EditableImage> extends ImageGallery<T> {

    protected final String IMAGE_EDITOR_ENABLE_JS = "imageGallery.edit('%s',%s)";

    private final ImageAnnotatingBehavior imageAnnotatingBehavior;

    public EditableImageGallery(String id, final IModel<List<T>> images) {
        super(id, images);
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior<T>() {
            @Override protected T getEditableImage() {
                return getCurrentImage();
            }
            @Override protected ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type) {
                return EditableImageGallery.this.getImageAnnotation(id,x,y,text,type);
            }
            @Override protected ImageAnnotation getAnnotation() {
                return EditableImageGallery.this.getAnnotation();
            }
            @Override protected String getDefaultText() {
                return EditableImageGallery.this.getDefaultText();
            }
            @Override protected ImageAnnotationType getDefaultType() {
                return EditableImageGallery.this.getDefaultType();
            }
            @Override protected void processNote(T editableImage, ImageAnnotation annotation) {
                super.processNote(editableImage, annotation);
                EditableImageGallery.this.doNote(editableImage, annotation);
            }
        }.withEditing());
    }

    protected abstract ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type);

    protected ImageAnnotationType getDefaultType() {
        return ImageAnnotationType.getDefault();
    }

    protected String getDefaultText() {
        return "new label";
    }

    protected String getImageEditorJs() {
        return String.format(IMAGE_EDITOR_ENABLE_JS,gallery.getMarkupId(),jsonRenderer.render(imageAnnotatingBehavior.getImageAnnotationOptions()));
    }

    @Override
    protected T addImage(byte[] bytes, String contentType, String clientFileName) {
        T image = createImage(clientFileName);
        uploadImage(image, contentType, bytes, clientFileName);
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
        return super.createGalleryOptions(images);//new EditableImageGalleryOptions(images);
    }

    protected abstract void uploadImage(T image, String contentType, byte[] bytes, String clientFileName);

    protected abstract T createImage(String path);

    protected void doNote(T editableImage, ImageAnnotation annotation) { }

    protected abstract ImageAnnotation getAnnotation();

    // ----------------------------------------------------------------------------------------------------------------------------------------

    class GalleryEditableImageJson extends GalleryImageJson {
        private List<ImageAnnotation> annotations;

        GalleryEditableImageJson(T image) {
            super(image);
            annotations = image.getAnnotations();
        }
    }

}
