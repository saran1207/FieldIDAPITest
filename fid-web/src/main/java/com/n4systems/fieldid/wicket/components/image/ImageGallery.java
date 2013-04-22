package com.n4systems.fieldid.wicket.components.image;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
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
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValue;

import java.util.List;

public abstract class ImageGallery<T extends S3Image> extends Panel {

    public final String IMAGE_ID_PARAMETER = "imageId";
    public final String ACTION_PARAMETER = "action";
    public final String INDEX_PARAMETER = "index";

    private static final String GALLERY_JS = "imageGallery.init('%s',%s);";
    private static final String GALLERY_ADD_JS = "imageGallery.add('%s',{image:'%s', id:%d});";

    protected @SpringBean JsonRenderer jsonRenderer;
    protected @SpringBean PersistenceService persistenceService;
    protected @SpringBean S3Service s3Service;

    protected final List<T> images;
    private final AbstractDefaultAjaxBehavior behavior;
    protected Integer currentImageIndex;
    protected FileUploadField uploadField;
    protected final Component gallery;
    private Form form;
    private boolean withDone = false;
    // TODO DD : add noAjaxUpdate option.
    // TODO DD : add ability to delete images (check to see if they have annotations first?)

    public ImageGallery(String id, final List<T> images) {
        super(id);
        this.images = images;
        setOutputMarkupId(true);
        add(new AttributeAppender("class", "image-gallery"));
        add(behavior = createBehavior());

        form = new Form("uploadForm");
        form.setMultiPart(true);
        //form.setMaxSize(Bytes.megabytes(5));

        form.add(uploadField = new FileUploadField("fileUpload"));
        uploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override protected void onSubmit(AjaxRequestTarget target) {
                FileUpload fileUpload = uploadField.getFileUpload();
                if (fileUpload != null) {
                    T image = addImage(fileUpload.getBytes(), fileUpload.getContentType(), fileUpload.getClientFileName());
                    if (image != null) {
                        images.add(image);
                    }
                    target.add(ImageGallery.this);
                    target.appendJavaScript(String.format(GALLERY_ADD_JS, gallery.getMarkupId(), getImageUrl(image), image.getId()));
                }
            }

            @Override protected void onError(AjaxRequestTarget target) {
            }
        });

        add(form);

        add(new WebMarkupContainer("done").setVisible(false));

        add(gallery = new WebMarkupContainer("images").setOutputMarkupId(true));
    }

    protected abstract String getImageUrl(T image);

    protected String getThumbnailImageUrl(T image) {return "";}

    protected abstract T addImage(byte[] bytes, String contentType, String clientFileName);

    protected T getCurrentImage() {
        return currentImageIndex==null ? null : images.get(currentImageIndex);
    }

    private AbstractDefaultAjaxBehavior createBehavior() {
        return new AbstractDefaultAjaxBehavior() {
            @Override protected void respond(AjaxRequestTarget target) {
                IRequestParameters params = RequestCycle.get().getRequest().getRequestParameters();
                currentImageIndex = params.getParameterValue(INDEX_PARAMETER).toInt();
                Long id = params.getParameterValue(IMAGE_ID_PARAMETER).toLong();
                Preconditions.checkState(images.get(currentImageIndex).getId().equals(id), "selected image id '" + id + "' doesn't match['" + currentImageIndex+"]=" + images.get(currentImageIndex).getId());
                imageClicked(target, getAction(params), getCurrentImage());
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
        for (T image:images) {
            data.add(createImageJson(image));
        }
        return data;
    }

    protected GalleryImageJson createImageJson(T image) {
        return new GalleryImageJson(image);
    }

    protected class GalleryImageJson {
        String image;
        String thumb;
        Long id;
        public GalleryImageJson(T image) {
            this.image = getImageUrl(image).toString();
            this.thumb = getThumbnailImageUrl(image).toString(); /*optional*/
            this.id = image.getId();
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


