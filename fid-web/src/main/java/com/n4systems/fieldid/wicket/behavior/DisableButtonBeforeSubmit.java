package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;

public class DisableButtonBeforeSubmit extends Behavior {

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        component.add(new AttributeModifier("onclick", "jQuery(this).attr('disabled', true);this.form.submit();"));
    }

}
