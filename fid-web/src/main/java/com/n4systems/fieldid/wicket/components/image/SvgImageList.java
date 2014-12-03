package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;


public class SvgImageList<T> extends Panel {

    private @SpringBean S3Service s3Service;

    private static final String INIT_JS = "svgImageList.createImageList('%s');";

    protected IModel<List<T>> images;
    private final ListView<T> listView;

    public SvgImageList(String id, final IModel<List<T>> images) {
        super(id);
        this.images = images;
        setOutputMarkupId(true);
        add(new AttributeAppender("class", "image-list"));
        listView = new ListView<T>("list", images) {

            @Override
            protected void populateItem(final ListItem<T> item) {
                createImage(item);
            }

            @Override
            public boolean isVisible() {
                return images.getObject().size()>0;
            }
        };
        add(listView);
        add(new WebMarkupContainer("blankSlate") {
            @Override
            public boolean isVisible() {
                return images.getObject().size()==0;
            }
        });
    }

    protected void createImage(ListItem<T> item) {
        item.add(new WebMarkupContainer("image"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/legacy/component/imageList.css");
        response.renderJavaScriptReference("javascript/svgImageList.js");
        response.renderOnLoadJavaScript(String.format(INIT_JS, getMarkupId()));

    }
}


