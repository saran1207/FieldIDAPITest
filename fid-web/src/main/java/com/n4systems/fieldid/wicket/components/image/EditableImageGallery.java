package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

public class EditableImageGallery extends ImageGallery<EditableImage> {

    private static final String IMAGE_EDITOR_ENABLE_JS = "imageGallery.edit('%s',%s)";

    private ImageAnnotatingBehavior imageAnnotatingBehavior;

    public EditableImageGallery(String id, List<EditableImage> images) {
        super(id, images);
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior() {
            @Override protected EditableImage getEditableImage() {
                return getCurrentImage();
            }

            @Override protected AnnotatedImageOptions getImageAnnotationOptions() {
                AnnotatedImageOptions options = super.getImageAnnotationOptions();
                return options;
            }

        });
    }

    private String getImageEditorJs() {
        return String.format(IMAGE_EDITOR_ENABLE_JS,gallery.getMarkupId(),jsonRenderer.render(imageAnnotatingBehavior.getImageAnnotationOptions()));
    }


    @Override
    protected EditableImage saveImage(S3Service.S3ImagePath path) {
        EditableImage image = new EditableImage(path.getMediumPath());
        persistenceService.save(image);
        return image;
    }

    @Override
    protected void imageClicked(AjaxRequestTarget target, String action, EditableImage image) {
        super.imageClicked(target, action, image);
        target.appendJavaScript(getImageEditorJs());
    }

    @Override
    protected GalleryImageJson createImageJson(EditableImage image) {
        return new GalleryEditableImageJson(image);
    }


    class GalleryEditableImageJson extends GalleryImageJson {
        private List<ImageAnnotation> annotations;

        GalleryEditableImageJson(EditableImage image) {
            super(image);
            annotations = image.getAnnotations();
        }
    }

}
