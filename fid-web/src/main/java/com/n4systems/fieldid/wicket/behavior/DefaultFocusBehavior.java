package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponent;

public class DefaultFocusBehavior extends Behavior {

    private static final String FOCUS_JS = "$('#" + "').focus();";

    @Override
    public void bind(Component component) {
        if (!(component instanceof FormComponent)) {
            throw new IllegalArgumentException("DefaultFocusBehavior: component must be instanceof FormComponent");
        }
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderOnLoadJavaScript(String.format(FOCUS_JS, component.getOutputMarkupId()));
    }
}