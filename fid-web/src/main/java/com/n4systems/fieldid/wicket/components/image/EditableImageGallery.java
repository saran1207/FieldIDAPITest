package com.n4systems.fieldid.wicket.components.image;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

public class EditableImageGallery extends ImageGallery<EditableImage> {

    // this is very loose but it's the only handle i have for finding the currently displayed image in the galleria widget
    // TODO : test this on IE.
    private static final String SELECTOR_FORMAT = "#%s .galleria-image:eq(%d)";
    private static final String IMAGE_EDITOR_ENABLE_JS = "imageGallery.enableEditor('%s',%d,%s)";

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

            @Override
            protected String getInitJs() {
                return String.format(IMAGE_EDITOR_ENABLE_JS,gallery.getMarkupId(),currentImageIndex,jsonRenderer.render(getImageAnnotationOptions()));
            }

        });
    }

    private String getImageSelector() {
        return String.format(SELECTOR_FORMAT,getMarkupId(),currentImageIndex);
    }

    public EditableImageGallery(String id, EditableImage... images) {
        this(id, Lists.newArrayList(images));
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
        target.appendJavaScript(imageAnnotatingBehavior.getInitJs());
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
