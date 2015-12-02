package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.model.IModel;

/**
 * This Behaviour creates a JS Alert using the "msg" value provided and cancels the onclick event that generated this
 * alert.  Great for when users can't use functionality for some reason and you want to tell them why... and stop them
 * from using that functionality.
 *
 * Created by Jordan Heath on 2015-12-02.
 */
public class AlertBehaviour extends AttributeModifier {

    public AlertBehaviour(IModel<String> msg) {
        super("onclick", msg);
    }

    @Override
    protected String newValue(final String currentValue, final String replacementValue) {
                        //Pretty simple... display an alert, then return false so the "click" action is cancelled.
        String prefix = "alert('" + replacementValue + "'); " +
                        "return false;";

        return (currentValue == null ? prefix : prefix + currentValue);
    }
}
