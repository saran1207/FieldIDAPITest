package com.n4systems.fieldid.wicket.behavior.validation;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;

//This will not work for Ajax links or buttons use AjaxCallDecorator instead
public class DisableNavigationConfirmationBehavior extends Behavior {

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        component.add(new AttributeAppender("onclick", "promptBeforeLeaving = false;").setSeparator(" "));
    }

}
