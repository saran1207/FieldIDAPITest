package com.n4systems.fieldid.wicket.components.richText;

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
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;

/**
 * CAVEAT : have noticed a conflict with autocomplete component.  when they exist on the same page the arrow keys and space bar don't work in this editor.  not sure why as of yet....DD
 *
 * it is very IMPORTANT (if you are planning on using the upload image feature) to override the method to specify the path for the image.
 *
 * usage : see nicedit.com for documentation on javascript rich text editor.
 */
public class RichText extends Panel {

    private static final String INIT_NICEDIT_JS = "richTextFactory.create('%s',%s)";

    private static final Logger logger= Logger.getLogger(RichText.class);
    public static final String RTF_IMAGE_PATH = "rtf/images";

    private @SpringBean SecurityContext securityContext;

    private Component area;
    private Boolean fullPanel;
    private String iconsPath = "../../images/nicEdit/nicEditorIcons.gif";
    private String[] buttonList = {"bold","italic","underline","left", "center", "right", "justify", "ol", "ul", "upload", "fontSize", "fontFamily", "fontFormat"};
    private Integer maxHeight;
    private AbstractDefaultAjaxBehavior behavior;
    private Boolean disabled;

    public RichText(String id, IModel<String> model) {
        super(id);
        add(new AttributeAppender("class", Model.of("rich-text"), " "));
        add(area = new TextArea<String>("text", model).setOutputMarkupId(true));
        setOutputMarkupId(true);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/nicEdit/nicEdit-min.js");
        response.renderJavaScriptReference("javascript/component/richText.js");
        Gson gson = new GsonBuilder().create();
        String optionsJson = gson.toJson(getOptions());
        response.renderOnDomReadyJavaScript(String.format(INIT_NICEDIT_JS, area.getMarkupId(), optionsJson));
    }

    protected Object /*CAVEAT : Must be Json Object*/ getOptions() {
        NicEditOptions options = new NicEditOptions(buttonList, fullPanel, iconsPath, maxHeight, getImageUploadUrl(),disabled);
        if (behavior!=null) {
            options.callbackUrl = behavior.getCallbackUrl().toString();
        }
        return options;
    }

    public String getTextAreaMarkupId() {
        return area.getMarkupId();
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

    public RichText withButtonList(String[] buttonList) {
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
        Integer maxHeight;
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

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }
    }
}
