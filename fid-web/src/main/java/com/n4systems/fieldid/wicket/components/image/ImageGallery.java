package com.n4systems.fieldid.wicket.components.image;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.common.S3Image;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
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
    private static final String GALLERY_ADD_JS = "imageGallery.add('%s',{image:'%s', id:%d});";

    protected @SpringBean JsonRenderer jsonRenderer;
    protected @SpringBean PersistenceService persistenceService;
    protected @SpringBean S3Service s3Service;

    private final List<T> images;
    private final AbstractDefaultAjaxBehavior behavior;
    protected Integer currentImageIndex;
    protected FileUploadField uploadField;
    protected final Component gallery;
    private Form form;
    // TODO : add noAjaxUpdate option.

    public ImageGallery(String id, final List<T> images) {
        super(id);
        this.images = images;
        setOutputMarkupId(true);
        add(behavior = createBehavior());

        form = new Form("uploadForm");
        form.setMultiPart(true);
        //form.setMaxSize(Bytes.megabytes(5));

        form.add(uploadField = new FileUploadField("fileUpload"));
        uploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override protected void onSubmit(AjaxRequestTarget target) {
                FileUpload fileUpload = uploadField.getFileUpload();
                if (fileUpload != null) {
                    S3Service.S3ImagePath path = s3Service.uploadImage(fileUpload.getBytes(), fileUpload.getContentType(), getFileName(fileUpload.getClientFileName()), FieldIDSession.get().getSessionUser().getTenant().getId());
                    System.out.println(path);
                    T image = saveImage(path);
                    if (image!=null) {
                        images.add(image);
                    }
                    target.appendJavaScript(String.format(GALLERY_ADD_JS,gallery.getMarkupId(),getImageUrl(image, path), image.getId()));
                }
            }

            @Override protected void onError(AjaxRequestTarget target) {
            }
        });

        add(form);

        add(new AttributeAppender("class", "image-gallery"));

        add(gallery = new WebMarkupContainer("images").setOutputMarkupId(true));
    }

    protected String getImageUrl(T image, S3Service.S3ImagePath path) {
        // TODO : make this cacheable...expiry date is months? or NEVER?
        return s3Service.generateResourceUrl(path.getMediumPath()).toString();
    }

    protected T saveImage(S3Service.S3ImagePath path) {
        return null;
    }

    private String getFileName(String fileName) {
        return "foo/bar/stuff/"+fileName;
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
                currentImageIndex = params.getParameterValue("index").toInt();
                imageClicked(target, getAction(params), getCurrentImage());
            }
        };
    }

    protected void imageClicked(AjaxRequestTarget target, String action, T image) {

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

        response.renderOnDomReadyJavaScript(String.format(GALLERY_JS,gallery.getMarkupId(),jsonRenderer.render(createGalleryOptions(getJsonDataSource()))));
    }

    protected GalleryOptions createGalleryOptions(List<GalleryImageJson> images) {
        return new GalleryOptions(images);
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
        String title = "sampe vidleo";
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


