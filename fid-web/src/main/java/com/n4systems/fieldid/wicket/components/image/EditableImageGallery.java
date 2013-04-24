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
    private final IModel<ImageAnnotation> model;

    public EditableImageGallery(String id, final List<T> images, IModel<ImageAnnotation> model) {
        super(id, images);
        this.model = model;
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior<T>(model.getObject()) {
            @Override protected T getEditableImage() {
                return getCurrentImage();
            }
            @Override protected String getDefaultText() {
                return EditableImageGallery.this.getDefaultText();
            }
            @Override protected ImageAnnotation findImageAnnotation(Long id) {
                for (T image:images) {
                    for (ImageAnnotation annotation:image.getAnnotations()) {
                        if (id==null && annotation.getId()==null) {
                            return annotation;
                        } else if (annotation.getId().equals(id)) {
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

    protected String getDefaultText() {
        return "new label";
    }

    private String getImageEditorJs() {
        return String.format(IMAGE_EDITOR_ENABLE_JS,gallery.getMarkupId(),jsonRenderer.render(imageAnnotatingBehavior.getImageAnnotationOptions()));
    }

    private ImageAnnotation getAnnotation() {
        return model.getObject();
    }

    @Override
    protected T addImage(byte[] bytes, String contentType, String clientFileName) {
        T image = createImage(clientFileName);
        uploadImage(image, bytes, contentType, clientFileName);
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

    protected abstract void uploadImage(T image, byte[] bytes, String path, String clientFileName);

    protected abstract T createImage(String path);

    protected void doLabel(T editableImage, ImageAnnotation annotation) { }

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
        String type= getAnnotation()!=null ? getAnnotation().getType().getCssClass() : ImageAnnotationType.getDefault().getCssClass();

        EditableGalleryOptions(List data) {
            super(data);
        }
    }


}
