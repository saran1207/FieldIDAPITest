package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.ImageAnnotation;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class ImageAnnotationLabel extends Panel {
    public ImageAnnotationLabel(String id, ImageAnnotation imageAnnotation) {
        super(id, new PropertyModel(imageAnnotation,"text"));
    }
}
