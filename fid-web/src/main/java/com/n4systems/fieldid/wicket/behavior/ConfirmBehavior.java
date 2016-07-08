package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

//Note: For AjaxLink use ConfirmAjaxCallDecorator
public class ConfirmBehavior extends AttributeModifier {

    public ConfirmBehavior(IModel<String> msg) {
        super("onclick", msg);
    }

    public ConfirmBehavior(String event, IModel<String> msg) {
        super(event, msg);
    }


    protected String newValue(final String currentValue, final String replacementValue) {
        String prefix = "if (confirmationRequired) { " +
                "var conf = confirm('" + replacementValue + "');" +
                " if (!conf) return false;}";
        String result = prefix;
        if (currentValue != null) {
            result = prefix + currentValue;
        }
        return result;
    }

    //To change confirmationRequired, use renderOnLoadJavaScript to set confirmationRequired = false
    // on the page the behavior is used
    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderJavaScript("var confirmationRequired = true;", "confirmBehavior");
    }


}

