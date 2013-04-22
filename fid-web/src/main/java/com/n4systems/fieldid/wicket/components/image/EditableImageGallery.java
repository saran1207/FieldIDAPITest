package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

public abstract class EditableImageGallery<T extends EditableImage> extends ImageGallery<T> {

    private static final String IMAGE_EDITOR_ENABLE_JS = "imageGallery.edit('%s',%s)";

    private ImageAnnotatingBehavior imageAnnotatingBehavior;

    private final ImageAnnotation annotation;

    public EditableImageGallery(String id, final List<T> images, final ImageAnnotation annotation) {
        super(id, images);
        this.annotation = annotation;
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior<T>(annotation) {
            @Override protected T getEditableImage() {
                return getCurrentImage();
            }
            @Override protected String getDefaultText() {
                return EditableImageGallery.this.getDefaultText();
            }
            @Override protected ImageAnnotation findImageAnnotation(Long id) {
                for (T image:images) {
                    for (ImageAnnotation annotation:image.getAnnotations()) {
                        if (id.equals(annotation.getId())) {
                            return annotation;
                        }
                    }
                }
                return null;
            }
        }.withEditing());
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
        uploadImage(image, bytes, contentType, clientFileName);
        persistenceService.save(image);
        return image;
    }

    protected abstract void uploadImage(T image, byte[] bytes, String path, String clientFileName);

    protected abstract T createImage(String path);

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
