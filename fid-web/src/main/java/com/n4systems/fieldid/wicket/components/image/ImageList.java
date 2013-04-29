package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.S3Image;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;


public abstract class ImageList<T extends S3Image> extends Panel {

    private static final String INIT_JS = "fieldIdWidgets.createImageList('%s',%s);";

    private @SpringBean JsonRenderer jsonRenderer;
    private @SpringBean S3Service s3Service;

    protected IModel<List<T>> images;
    private final ListView<T> listView;

    public ImageList(String id, final IModel<List<T>> images) {
        super(id);
        this.images = images;
        setOutputMarkupId(true);
        boolean hasImages = images.getObject().size()>0;
        add(new AttributeAppender("class", "image-list"));
        listView = new ListView<T>("list", images) {
            @Override protected void populateItem(final ListItem<T> item) {
                createImage(item);
            }
        };
        add(listView.setVisible(hasImages));
        add(new WebMarkupContainer("blankSlate").setVisible(!hasImages));
    }

    protected void createImage(ListItem<T> item) {
        item.add(new WebMarkupContainer("image"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/imageList.css");
        response.renderJavaScriptReference("javascript/fieldIdWidgets.js");
        response.renderOnLoadJavaScript(String.format(INIT_JS, getMarkupId(), jsonRenderer.render(getOptions())));
    }

    protected ImageListOptions getOptions() {
        return new ImageListOptions();
    }

    class ImageListOptions implements Serializable {
        String yPosition = "middle";
        String xPosition ="left";
    }

}


