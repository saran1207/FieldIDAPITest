package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;

public class ChangeThenClickComponentWhenEnterPressed extends ClickOnComponentWhenEnterKeyPressedBehavior {

    private Component component;

    public ChangeThenClickComponentWhenEnterPressed(Component componentToClickOn) {
        super(componentToClickOn);
    }

    @Override
    public void bind(Component component) {
        super.bind(component);
        this.component = component;
        component.setOutputMarkupId(true);
    }

    @Override
    protected String getBeforeClickJs() {
        return "$('#"+component.getMarkupId()+"').trigger('change');";
    }
}
