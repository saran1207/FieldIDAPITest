package com.n4systems.fieldid.wicket.components.image;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.S3Image;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class ImageList<T extends S3Image> extends Panel {

    private static final String INIT_JS = "$('#%s').css('overflow-x','auto')";
    private @SpringBean S3Service s3Service;

    private IModel<List<T>> images;

    public ImageList(String id, final IModel<List<T>> images) {
        super(id);
        this.images = images;
        setOutputMarkupId(true);
        add(new AttributeAppender("class", "image-list"));
        ListView<T> listView = new ListView<T>("list", images) {
            @Override protected void populateItem(final ListItem<T> item) {
                addImage(item);
                item.setRenderBodyOnly(true);
            }
        };
        add(listView);
    }

    protected Component addImage(ListItem<T> item) {
        Component image;
        item.add(image = new S3ImageContainer("image", item.getModel().getObject().getS3Path()).setOutputMarkupId(true));
        return image;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("style/component/imageList.css");
        response.renderOnDomReadyJavaScript(String.format(INIT_JS,getMarkupId()));
    }


    public class S3ImageContainer extends WebComponent {

        private final String imageUrl;

        public S3ImageContainer(String id, String path) {
            super(id);
            this.imageUrl = s3Service.generateResourceUrl(path).toString();
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            add(new AttributeAppender("style", Model.of("background-image:url("+imageUrl+")"),";"));
        }

        protected void onComponentTag(ComponentTag tag) {
            super.onComponentTag(tag);
            checkComponentTag(tag, "div");
        }

    }


}


