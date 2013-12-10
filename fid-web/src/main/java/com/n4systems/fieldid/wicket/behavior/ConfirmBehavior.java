package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;

public class ConfirmBehavior extends AttributeModifier {

    public ConfirmBehavior(IModel<String> msg) {
        super("onclick", msg);
    }

    public ConfirmBehavior(String event, IModel<String> msg) {
        super(event, msg);
    }

    protected String newValue(final String currentValue, final String replacementValue) {
        String prefix = "var conf = confirm('" + replacementValue + "'); " +
                "if (!conf) return false;";
        String result = prefix;
        if (currentValue != null) {
            result = prefix + currentValue;
        }
        return result;
    }

}

