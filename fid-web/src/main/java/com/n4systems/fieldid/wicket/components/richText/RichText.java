package com.n4systems.fieldid.wicket.components.richText;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import java.io.Serializable;

/**
 * CAVEAT : have noticed a conflict with autocomplete component.  when they exist on the same page the arrow keys and space bar don't work in this editor.  not sure why as of yet....DD
 *
 * usage : see nicedit.com for documentation on javascript rich text editor.
 */
public class RichText extends Panel {

    private static final String INIT_NICEDIT_JS = "richTextFactory.create('%s',%s)";

    private Component area;
    private Boolean fullPanel;
    private String iconsPath = "../../images/nicEdit/nicEditorIcons.gif";
    private String[] buttonList = {"bold","italic","underline","left", "center", "right", "justify", "ol", "ul", "upload", "fontSize", "fontFamily", "fontFormat"};
    private Integer maxHeight;

    public RichText(String id, IModel<String> model) {
        super(id);
        add(new AttributeAppender("class", "rich-text"));
        add(area = new TextArea<String>("text", model).setOutputMarkupId(true));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/nicEdit/nicEdit.js");
        response.renderJavaScriptReference("javascript/component/richText.js");
        Gson gson = new GsonBuilder().create();
        String optionsJson = gson.toJson(getOptions());
        response.renderOnDomReadyJavaScript(String.format(INIT_NICEDIT_JS, area.getMarkupId(), optionsJson));
    }

    protected Object /*CAVEAT : Must be Json Object*/ getOptions() {
        return new NicEditOptions(buttonList,fullPanel,iconsPath,maxHeight);
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


    public class NicEditOptions implements Serializable {
        private Boolean fullPanel;
        private String iconsPath;
        private String[] buttonList;
        private Integer maxHeight;

        public NicEditOptions() {
        }

        public NicEditOptions(String[] buttonList, Boolean fullPanel, String iconsPath, Integer maxHeight) {
            this.buttonList = buttonList;
            this.fullPanel = fullPanel;
            this.iconsPath = iconsPath;
            this.maxHeight = maxHeight;
        }
    }
}
