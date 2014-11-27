package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import com.n4systems.model.common.ImageAnnotationType;
import org.apache.wicket.model.IModel;

import java.util.List;

public abstract class ArrowStyleEditableImageGallery<T extends EditableImage> extends EditableImageGallery<T> {

    private final ArrowStyleAnnotatingBehavior imageAnnotatingBehavior;

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

    protected abstract ImageAnnotation getImageAnnotation(Long id, Double x, Double y, Double x2, Double y2, String text, ImageAnnotationType type);

    //Don't use this one
    @Override
    protected ImageAnnotation getImageAnnotation(Long id, Double x, Double y, String text, ImageAnnotationType type) {
        return null;
    }

}
