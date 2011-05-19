package com.n4systems.fieldid.wicket.behavior.validation;

import org.apache.wicket.Component;

public class ValidationBehavior {

    public static void addValidationBehaviorToComponent(Component component) {
        component.add(new HighlightFieldOnValidationError());
        component.add(new DisplayValidationErrorOnInputTitle(component));
    }

}
