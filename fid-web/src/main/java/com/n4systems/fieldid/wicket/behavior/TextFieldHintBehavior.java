package com.n4systems.fieldid.wicket.behavior;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class TextFieldHintBehavior extends Behavior {

    private IModel<String> hintModel;

    public TextFieldHintBehavior(IModel<String> hintModel) {
        this.hintModel = hintModel;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupPlaceholderTag(true);
        component.add(new AttributeAppender("title", hintModel));
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderCSSReference("style/newCss/behavior/textfieldhint.css");
        response.renderJavaScriptReference("javascript/textfieldhint.js");
        String escapedHint = StringEscapeUtils.escapeJavaScript(hintModel.getObject());
        response.renderOnDomReadyJavaScript("prepareHintListeners('" + component.getMarkupId() + "', '"
                + escapedHint + "');");
    }

}
