package com.n4systems.fieldid.wicket.components.image;

import com.google.common.collect.Lists;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import org.apache.wicket.ajax.AjaxRequestTarget;

import java.util.List;

public class EditableImageGallery extends ImageGallery<EditableImage> {

    // this is very loose but it's the only handle i have for finding the currently displayed image in the galleria widget
    private static final String SELECTOR_FORMAT = "#%s .galleria-image:eq(%d)";

    private ImageAnnotatingBehavior imageAnnotatingBehavior;

    public EditableImageGallery(String id, List<EditableImage> images) {
        super(id, images);
        setOutputMarkupId(true);
        add(imageAnnotatingBehavior = new ImageAnnotatingBehavior() {
            @Override
            protected EditableImage getEditableImage() {
                return getCurrentImage();
            }

            @Override
            protected AnnotatedImageOptions getImageAnnotationOptions() {
                AnnotatedImageOptions options = super.getImageAnnotationOptions();
                return options;
            }

            @Override protected String getImageSelector() {
                return String.format(SELECTOR_FORMAT,getMarkupId(),currentImageIndex);
            }
        });
    }

    public EditableImageGallery(String id, EditableImage... images) {
        this(id, Lists.newArrayList(images));
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
