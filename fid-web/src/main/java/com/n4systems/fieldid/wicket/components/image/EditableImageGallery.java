package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

import java.util.List;

public abstract class EditableImageGallery<T extends EditableImage> extends ImageGallery<T> {

    private final String IMAGE_EDITOR_ENABLE_JS = "imageGallery.edit('%s',%s)";

    private final ImageAnnotatingBehavior imageAnnotatingBehavior;

    public EditableImageGallery(String id, final IModel<List<T>> images) {
        super(id, images);
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior<T>() {
            @Override protected T getEditableImage() {
                return getCurrentImage();
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
            @Override protected ImageAnnotation findImageAnnotation(Long id) {
                for (T image:images.getObject()) {
                    for (ImageAnnotation annotation:image.getAnnotations()) {
                        if (isAnnotationForId(id, annotation)) {
                            return annotation;
                        }
                    }
                }
                return null;
            }
            @Override protected void doLabel(T editableImage, ImageAnnotation annotation) {
                super.doLabel(editableImage,annotation);
                EditableImageGallery.this.doLabel(editableImage, annotation);
            }
        }.withEditing());
    }

    private boolean isAnnotationForId(Long id, ImageAnnotation annotation) {
        if (id==null) { // it's either a new one for this isolation point or an non-persisted one...
           if (annotation.getId()==null && annotation.getTempId()==null) {
               return true;
           }
        } else if (id.equals(annotation.getId())) {
            return true;
        } else if (id!=null && annotation.getId()==null && id.equals(annotation.getTempId())) {
            return true;
        }
        return false;
    }

    protected ImageAnnotationType getDefaultType() {
        return ImageAnnotationType.getDefault();
    }

    protected String getDefaultText() {
        return "new label";
    }

    private String getImageEditorJs() {
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
        return super.createGalleryOptions(images);
    }

    protected abstract void uploadImage(T image, String contentType, byte[] bytes, String clientFileName);

    protected abstract T createImage(String path);

    protected void doLabel(T editableImage, ImageAnnotation annotation) { }

    protected abstract ImageAnnotation getAnnotation();

    // ----------------------------------------------------------------------------------------------------------------------------------------

    class GalleryEditableImageJson extends GalleryImageJson {
        private List<ImageAnnotation> annotations;

        GalleryEditableImageJson(T image) {
            super(image);
            annotations = image.getAnnotations();
        }
    }

    class EditableGalleryOptions extends GalleryOptions {
        Long editedId = getAnnotation()!=null ? getAnnotation().getId() : null;

        EditableGalleryOptions(List data) {
            super(data);
        }
    }

}
