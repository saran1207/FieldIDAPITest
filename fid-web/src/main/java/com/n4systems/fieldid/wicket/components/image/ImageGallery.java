package com.n4systems.fieldid.wicket.components.image;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.common.S3Image;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.List;

public class ImageGallery<T extends S3Image> extends Panel {

    public static final String IMAGE_ID_PARAMETER = "id";
    public static final String ACTION = "action";

    private static final String GALLERY_JS = "imageGallery.init('%s',%s);";

    private @SpringBean JsonRenderer jsonRenderer;
    private @SpringBean S3Service s3Service;

    private final List<T> images;
    private ImageEditor imageEditor;
    private final AbstractDefaultAjaxBehavior behavior;
    protected Integer currentImageIndex;
    // TODO : add noAjaxUpdate option.

    public ImageGallery(String id, List<T> images) {
        super(id);
        this.images = images;
        setOutputMarkupId(true);
        add(behavior = createBehavior());

        add(new AttributeAppender("class","image-gallery"));
        add(new WebMarkupContainer("images"));
    }

    public ImageGallery(String id, T... images) {
        this(id, Lists.newArrayList(images));
    }

    protected T getCurrentImage() {
        return currentImageIndex==null ? null : images.get(currentImageIndex);
    }


    private AbstractDefaultAjaxBehavior createBehavior() {
        return new AbstractDefaultAjaxBehavior() {
            @Override protected void respond(AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                currentImageIndex = getImageIndexById(getImageId(params));
                imageClicked(target, getAction(params), getCurrentImage());
            }
        };
    }

    protected int getImageIndexById(Long id) {
        int index = 0;
        for (T image:images) {
            if (image.getId().equals(id)) {
                return index;
            }
            index++;
        }
        throw new IllegalStateException("can't find image for id " + id);
    }

    protected void imageClicked(AjaxRequestTarget target, String action, T image) {
        System.out.println("clicked image " +  image.getId());
    }

    private String getAction(IRequestParameters params) {
        StringValue p = params.getParameterValue(ACTION);
        return p==null || p.isEmpty() ? null : p.toString();
    }

    private Long getImageId(IRequestParameters params) {
        StringValue p = params.getParameterValue(IMAGE_ID_PARAMETER);
        return p==null || p.isEmpty() ? null : p.toLong();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference("javascript/galleria/themes/classic/galleria.classic.css");
        response.renderJavaScriptReference("javascript/jquery-ui-1.8.20.no-autocomplete.min.js");
        response.renderJavaScriptReference("javascript/galleria/galleria-1.2.9.min.js");
        response.renderJavaScriptReference("javascript/imageEditor.js");
        response.renderJavaScriptReference("javascript/imageGallery.js");
        response.renderCSSReference("style/component/imageGallery.css");

        response.renderOnDomReadyJavaScript(String.format(GALLERY_JS,getMarkupId(),jsonRenderer.render(new GalleryOptions(getJsonDataSource()))));
    }

    private List<GalleryImageJson> getJsonDataSource() {
        List data = Lists.newArrayList();
        for (T image:images) {
            data.add(createImageJson(image));
        }
       // data.add(new GalleryVideoJson());
        return data;
    }

    protected GalleryImageJson createImageJson(T image) {
        return new GalleryImageJson(image);
    }

    protected class GalleryImageJson {
        String image;
        Long id;
        public GalleryImageJson(T image) {
            this.image = s3Service.generateResourceUrl(image.getS3Path()).toString();
            this.id = image.getId();
        }
    }

    protected class GalleryVideoJson {
        String video = "http://www.youtube.com/watch?v=fsPebhpQezY";
        String title = "sampe video";
        String description = "hey, it moves";

        GalleryVideoJson() { }
    }

    protected class GalleryOptions {
        Object[] dataSource;
        int width=800;
        int height=400;
        String callback = behavior!=null ? behavior.getCallbackUrl().toString() : null;

        GalleryOptions(List data) {
            this.dataSource = data.toArray(new Object[data.size()]);
        }
    }

}


