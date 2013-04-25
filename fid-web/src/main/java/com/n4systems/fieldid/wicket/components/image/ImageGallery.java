package com.n4systems.fieldid.wicket.components.image;

import com.google.common.collect.Lists;
import com.n4systems.model.common.S3Image;
import com.n4systems.util.json.JsonRenderer;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.List;

public abstract class ImageGallery<T extends S3Image> extends Panel {

    protected @SpringBean JsonRenderer jsonRenderer;

    private final String GALLERY_JS = "imageGallery.init('%s',%s);";

    private final String PLACEHOLDER_ID = "_placeholder";
    private final String IMAGE_ID_PARAMETER = "imageId";
    private final String ACTION_PARAMETER = "action";
    private final String INDEX_PARAMETER = "index";

    protected IModel<List<T>> model;
    private final AbstractDefaultAjaxBehavior behavior;
    protected Integer currentImageIndex;
    protected FileUploadField uploadField;
    protected final Component gallery;
    private Form form;
    private boolean withDone = false;
    private List<FileUpload> fileUploads = Lists.newArrayList();

    // TODO DD : add noAjaxUpdate option.
    // TODO DD : add ability to delete images (check to see if they have annotations first?)

    public ImageGallery(String id, final IModel<List<T>> model) {
        super(id);
        this.model = model;
        setOutputMarkupId(true);
        add(new AttributeAppender("class", "image-gallery"));
        add(behavior = createBehavior());

        form = new Form("uploadForm");
        form.setMultiPart(true);
        //form.setMaxSize(Bytes.megabytes(5));

        form.add(uploadField = new FileUploadField("fileUpload", new PropertyModel(this,"fileUploads")));
        uploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override protected void onSubmit(AjaxRequestTarget target) {
                FileUpload fileUpload = uploadField.getFileUpload();
                if (fileUpload != null) {
                    T image = addImage(fileUpload.getBytes(), fileUpload.getContentType(), fileUpload.getClientFileName());
                    target.add(ImageGallery.this);
                }
            }

            @Override protected void onError(AjaxRequestTarget target) { }
        });

        add(form);

        add(new WebMarkupContainer("done").setVisible(false));

        add(gallery = new WebMarkupContainer("images").setOutputMarkupId(true));
    }

    protected abstract String getImageUrl(T image);

    protected String getThumbnailImageUrl(T image) {return "";}

    protected abstract T addImage(byte[] bytes, String contentType, String clientFileName);

    protected T getCurrentImage() {
        List<T> images = model.getObject();
        if (images.size()==0 || currentImageIndex==null) {
            return null;
        }
        return images.get(currentImageIndex);
    }

    private AbstractDefaultAjaxBehavior createBehavior() {
        return new AbstractDefaultAjaxBehavior() {
            @Override protected void respond(AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                currentImageIndex = params.getParameterValue(INDEX_PARAMETER).toInt();
                T currentImage = getCurrentImage();
                if (currentImage!=null) {
                    imageClicked(target, getAction(params), currentImage);
                }
            }
        };
    }

    protected void imageClicked(AjaxRequestTarget target, String action, T image) { }

    public ImageGallery withDoneButton() {
        withDone = true;
        return this;
    }

    public ImageGallery withNoDoneButton() {
        withDone = false;
        return this;
    }

    private String getAction(IRequestParameters params) {
        StringValue p = params.getParameterValue(ACTION_PARAMETER);
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

        // TODO DD : refactor this ...don't pass jsonDataSource.
        response.renderOnLoadJavaScript(String.format(GALLERY_JS,gallery.getMarkupId(),jsonRenderer.render(createGalleryOptions(getJsonDataSource()))));
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        if (withDone) {
            replace(createDoneButton("done"));
        }
    }

    protected AbstractLink createDoneButton(String id) {
        return new AjaxLink(id) {
            @Override public void onClick(AjaxRequestTarget target) {
                doneClicked(target);
            }
        };
    }

    protected void doneClicked(AjaxRequestTarget target) { }

    protected GalleryOptions createGalleryOptions(List<GalleryImageJson> images) {
        return new GalleryOptions(images);
    }

    private List<GalleryImageJson> getJsonDataSource() {
        List data = Lists.newArrayList();
        for (T image: model.getObject()) {
            data.add(createImageJson(image));
        }
        if (model.getObject().size()==0) {
            data.add(new GalleryImageJson());
        }
        return data;
    }

    protected GalleryImageJson createImageJson(T image) {
        return new GalleryImageJson(image);
    }

    protected String getPlaceholderImageUrl() {
        // TODO : need a correct blank slate image for "no images in gallery" situation.
        return "/fieldid/images/add-photo-slate.png";
    }

    // -----------------------------------------------------------------------------------------------------------------------

    protected class GalleryImageJson {
        String image;
        String thumb;
        String id;

        public GalleryImageJson() {
            this.image = getPlaceholderImageUrl();
            this.id = PLACEHOLDER_ID;
        }

        public GalleryImageJson(T image) {
            this.id = image.getId()==null ? "_unsaved_" + image.getS3Path() : image.getId()+"";  // we'll look it up by filename if it hasn't been persisted yet.
            this.image = getImageUrl(image);
            this.thumb = getThumbnailImageUrl(image); /*optional*/
        }
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


