package com.n4systems.fieldid.wicket.components.richText;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.n4systems.services.SecurityContext;
import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

/**
 * CAVEAT : have noticed a conflict with autocomplete component.  when they exist on the same page the arrow keys and space bar don't work in this editor.  not sure why as of yet....DD
 *
 * it is very IMPORTANT (if you are planning on using the upload image feature) to override the method to specify the path for the image.
 *
 * usage : see nicedit.com for documentation on javascript rich text editor.
 *
 * NOTE : IE doesn't support HTML 5 file uploads a separate (and worse) editor plugin has been written.
 * it should only be used for IE!
 */
public class RichText extends Panel {

    private static final String INIT_NICEDIT_JS = "richTextFactory.create('%s',%s)";

    private static final Logger logger= Logger.getLogger(RichText.class);
    public static final String RTF_IMAGE_PATH = "rtf/images";

    private @SpringBean SecurityContext securityContext;

    private Component area;
    private Boolean fullPanel;
    private String iconsPath = "../../images/nicEdit/nicEditorIcons.gif";
    private List<String> buttonList = Lists.newArrayList("bold","italic","underline","left", "center", "right", "justify", "ol", "ul", "fontSize", "fontFamily", "fontFormat");
    private Integer maxHeight;
    private AbstractDefaultAjaxBehavior behavior;
    private Boolean disabled;
    private boolean isIE;

    public RichText(String id, IModel<String> model) {
        super(id);
        add(new AttributeAppender("class", Model.of("rich-text"), " "));
        add(area = new TextArea<String>("text", model).setOutputMarkupId(true));
        setOutputMarkupId(true);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/nicEdit/nicEdit-min.js");

        // these are optionally loaded if IE
        if (isIE()) {
            response.renderJavaScriptReference("javascript/form/form.js");
            response.renderJavaScriptReference("javascript/nicEdit/uploadImage/uploadImage.js");
        }
        response.renderJavaScriptReference("javascript/component/richText.js");
        Gson gson = new GsonBuilder().create();
        String optionsJson = gson.toJson(getOptions());
        response.renderOnDomReadyJavaScript(String.format(INIT_NICEDIT_JS, area.getMarkupId(), optionsJson));
    }

    private boolean isIE() {
        WebClientInfo clientInfo = WebSession.get().getClientInfo();
        return clientInfo.getProperties().isBrowserInternetExplorer();
    }

    protected Object /*CAVEAT : Must be Json Object*/ getOptions() {
        buttonList.add(isIE() ? "uploadImage" : "upload");  // first button is IE specific non-HTML 5 custom upload plugin. 2nd one is sexier out of the box nicEdit uploader.
        NicEditOptions options = new NicEditOptions(buttonList.toArray(new String[buttonList.size()]), fullPanel, iconsPath, maxHeight, getImageUploadUrl(),disabled);
        if (behavior!=null) {
            options.callbackUrl = behavior.getCallbackUrl().toString();
        }
        return options;
    }

    public String getTextAreaMarkupId() {
        return area.getMarkupId();
    }

    public RichText withWidth(String width) {
        Preconditions.checkArgument(width!=null,"must specify a non null width");
        String value = width;
        if (!width.endsWith(";")) {
            value = value+";";
        }
        area.add(new AttributeAppender("style",Model.of("width:"+value), " "));
        return this;
    }

    public RichText withRows(Integer rows) {
        Preconditions.checkArgument(rows!=null,"must specify a non null rows");
        area.add(new AttributeAppender("rows",Model.of(rows), " "));
        return this;
    }

    public RichText withAutoUpdate() {
        // use this if you want your model updated via ajax after blur event
        area.add(behavior = new AjaxFormComponentUpdatingBehavior("onblur") {
            @Override protected void onUpdate(AjaxRequestTarget target) {
                ; // nop...just here to inherit the model binding feature.
            }
        });
        return this;
    }

    /**
     * @see com.n4systems.fieldid.servlets.ImageUploadHandler
     * @see com.n4systems.fieldid.servlets.ImageUploadServlet
     * specifies where the nicEdit text editor will go with uploaded images.
     * the default value assumes that fieldId's imageUpload servlet is configured properly.  (see web.xml)
     */
    protected String getImageUploadUrl() {
        return String.format("../../imageUpload/rtf?path=%s&tenantId=%s", getImagePath(), getTenantId());
    }

    protected String getImagePath() {
        logger.warn("using default path '" + RTF_IMAGE_PATH + "'for RTF editor images...override to specify a custom path");
        return RTF_IMAGE_PATH;
    }

    protected Long getTenantId() {
        return securityContext.getTenantSecurityFilter().getTenantId();
    }

    public RichText withMaxHeight(Integer maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public RichText withIconsPath(String path) {
        this.iconsPath = path;
        return this;
    }

    public RichText withButtonList(List<String> buttonList) {
        this.buttonList = buttonList;
        return this;
    }

    public RichText withFullPanel(Boolean fullPanel) {
        this.fullPanel = fullPanel;
        return this;
    }

    public RichText disabled() {
        this.disabled = true;
        return this;
    }


    // java object converted to json & used by nicEdit javascript widget.

    public class NicEditOptions implements Serializable {
        Boolean fullPanel;
        String iconsPath;
        String[] buttonList;
        Integer maxHeight = 200;
        String uploadURI;
        String callbackUrl;
        Boolean disabled;

        public NicEditOptions(String[] buttonList, Boolean fullPanel, String iconsPath, Integer maxHeight, String uploadURI, Boolean disabled) {
            this.buttonList = buttonList;
            this.fullPanel = fullPanel;
            this.iconsPath = iconsPath;
            this.maxHeight = maxHeight;
            this.uploadURI = uploadURI;
            this.disabled = disabled;
        }

    }
}
