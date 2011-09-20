package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class TextFieldWithDescription<T> extends TextField<T> implements IHeaderContributor {

    private IModel<String> descriptionText;

    public TextFieldWithDescription(String id, IModel<T> model, IModel<String> descriptionText) {
        super(id, model);
        this.descriptionText = descriptionText;
        setOutputMarkupId(true);

        add(new SimpleAttributeModifier("value", descriptionText.getObject()));
        add(new SimpleAttributeModifier("class", "description"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavascript("$('"+getMarkupId()+"').observe('focus', clearDescription)");
        response.renderOnDomReadyJavascript("$('"+getMarkupId()+"').observe('blur', replaceDescription)");
    }

}
