package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.model.common.EditableImage;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.IModel;

import java.util.List;

public class EditableImageList<T extends EditableImage> extends ImageList<T> {

    private final String INIT_JS = "imageEditor.init('#%s',%s);";
    private StringBuffer initJs = new StringBuffer();

    public EditableImageList(String id, IModel<List<T>> imageModel) {
        super(id,imageModel);
    }

    @Override
    protected Component addImage(final ListItem<T> item) {
        Component image = super.addImage(item);
        ImageAnnotatingBehavior behavior;
        item.add(behavior = new ImageAnnotatingBehavior() {
            @Override
            protected EditableImage getEditableImage() {
                return item.getModelObject();
            }
        });
        initJs.append(String.format(INIT_JS, image.getMarkupId(), behavior.getJsonImageAnnotationOptions()));
        return image;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/imageList.css");
        response.renderOnLoadJavaScript(initJs.toString());
    }

}


