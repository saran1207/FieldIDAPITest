package com.n4systems.fieldid.wicket.behavior;


import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * This Wicket Behavior will set the focus to the component it is attached to upon page load.
 */
public class SetFocusOnLoadBehavior extends Behavior {

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderOnLoadJavaScript("document.getElementById('" + component.getMarkupId() + "').focus();");
    }

    @Override
    public boolean isTemporary(Component component)
    {
        return false;
    }
}
