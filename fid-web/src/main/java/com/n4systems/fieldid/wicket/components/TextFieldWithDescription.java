package com.n4systems.fieldid.wicket.components;

import com.n4systems.fieldid.wicket.behavior.validation.ConsiderAbsentIfEqualToValidator;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;

public class TextFieldWithDescription extends TextField<String> {

    private IModel<String> descriptionText;

    public TextFieldWithDescription(String id, IModel<String> model, IModel<String> descriptionText) {
        super(id, model);
        this.descriptionText = descriptionText;
        setOutputMarkupId(true);

        add(new SimpleAttributeModifier("value", descriptionText.getObject()));
        add(new SimpleAttributeModifier("class", "description"));

        // If we submit with the same value as the description, the user didn't change the text,
        // so we consider it missing, and add the same error key as a Required validation.
        add(new ConsiderAbsentIfEqualToValidator(descriptionText));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderOnDomReadyJavaScript("$('#"+getMarkupId()+"').bind('focus', clearDescription)");
        response.renderOnDomReadyJavaScript("$('#"+getMarkupId()+"').bind('blur', replaceDescription)");
    }

}
