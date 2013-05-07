package com.n4systems.fieldid.wicket.components.image;

import com.google.common.collect.Lists;
import com.n4systems.model.common.EditableImage;
import com.n4systems.model.common.ImageAnnotation;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

import java.io.Serializable;
import java.util.List;

public class EditableImageList<T extends EditableImage> extends ImageList<T> {

    public EditableImageList(String id, IModel<List<T>> imageModel) {
        super(id,new FilteredImageListModel<T>(imageModel));
    }

    @Override
    protected ImageListOptions getOptions() {
        return new EditableImageOptions();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/component/annotated-image.css");
        response.renderJavaScriptReference("javascript/jquery.annotate.js");

    }


    class EditableImageOptions extends ImageListOptions {

        List<List<AnnotationOptions>> annotationOptions = Lists.newArrayList();

        EditableImageOptions() {
            super();
            if(images.getObject() != null) {
                for (EditableImage image : images.getObject()) {
                    List<AnnotationOptions> options = Lists.newArrayList();
                    for (ImageAnnotation annotation:image.getAnnotations()) {
                        options.add(new AnnotationOptions(annotation));
                    }
                    annotationOptions.add(options);
                }
            }
        }
    }


    class AnnotationOptions implements Serializable {
        double x;
        double y;
        String text;
        String cssStyle;

        public AnnotationOptions(ImageAnnotation annotation) {
            this.x = annotation.getX();
            this.y = annotation.getY();
            this.text = annotation.getText();
            this.cssStyle = annotation.getType().getCssClass();
        }
    }


}


