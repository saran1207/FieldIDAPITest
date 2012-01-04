package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.ComponentTag;

public class ClickOnComponentWhenEnterKeyPressedBehavior extends Behavior {

    private Component componentToClickOn;

    public ClickOnComponentWhenEnterKeyPressedBehavior(Component componentToClickOn) {
        if (componentToClickOn == null)
            throw new RuntimeException("Must have non null component to click on enter");
        componentToClickOn.setOutputMarkupId(true);
        this.componentToClickOn = componentToClickOn;
    }

    @Override
    public void onComponentTag(Component component, ComponentTag tag) {
        tag.put("onkeypress", "if (event.keyCode == 13) { document.getElementById('"+componentToClickOn.getMarkupId()+"').click(); return false;}");
    }

}
