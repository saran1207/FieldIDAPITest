package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import com.n4systems.util.json.ArrowStyleAnnotationJsonRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public abstract class ArrowStyleEditableImageGallery<T extends EditableImage> extends EditableImageGallery<T> {

    private final ArrowStyleAnnotatingBehavior imageAnnotatingBehavior;

    @SpringBean
    protected ArrowStyleAnnotationJsonRenderer jsonRenderer;

    public ArrowStyleEditableImageGallery(String id, IModel<List<T>> model) {
        super(id, model);
        add(imageAnnotatingBehavior = new ArrowStyleAnnotatingBehavior() {
            @Override protected EditableImage getEditableImage() {
                return getCurrentImage();
            }
            @Override protected ImageAnnotation getImageAnnotation(Long id, Double x, Double y, Double x2, Double y2, String text, ImageAnnotationType type) {
                return ArrowStyleEditableImageGallery.this.getImageAnnotation(id, x, y, x2, y2, text, type);
            }
            @Override protected ImageAnnotation getAnnotation() {
                return ArrowStyleEditableImageGallery.this.getAnnotation();
            }
            @Override protected String getDefaultText() {
                return ArrowStyleEditableImageGallery.this.getDefaultText();
            }
            @Override protected ImageAnnotationType getDefaultType() {
                return ArrowStyleEditableImageGallery.this.getDefaultType();
            }
        }.withEditing());
    }

    //Since we are using a new renderer, we need to override this method to make sure that the JSON for the ArrowStyle
    //stuff includes the x2 and y2 coordinates.  This Renderer couldn't be included in our default JsonRenderer, since
    //both types of annotations use the same JPA Entity.  As such, a new Renderer specific to the task needed to be
    //built.
    @Override
    protected String getImageEditorJs() {
        return String.format(IMAGE_EDITOR_ENABLE_JS,gallery.getMarkupId(), jsonRenderer.render(imageAnnotatingBehavior.getImageAnnotationOptions()));
    }

    protected abstract ImageAnnotation getImageAnnotation(Long id, Double x, Double y, Double x2, Double y2, String text, ImageAnnotationType type);

    //Don't use this one
    @Override
    protected ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type) {
        return null;
    }

}
