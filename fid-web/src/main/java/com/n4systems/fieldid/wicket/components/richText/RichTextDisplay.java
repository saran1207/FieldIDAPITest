package com.n4systems.fieldid.wicket.components.richText;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class RichTextDisplay extends Panel {

    Label text;

    public RichTextDisplay(String id, IModel<String> textModel) {
        super(id);
        setOutputMarkupPlaceholderTag(true);

        text = new Label("text", textModel);
        add(text.setEscapeModelStrings(false));
        add(new AttributeAppender("class", Model.of("rich-text"), " "));
        add(new AttributeAppender("style", "font-size: 10px"));
    }

    public RichTextDisplay setText(IModel<String> textModel) {
        text.setDefaultModel(textModel);
        return this;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/nicEdit/putSpinnersOverImages.js");
    }
}
