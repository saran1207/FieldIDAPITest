package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class DisableOnClick extends Behavior {

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        response.renderOnDomReadyJavaScript("jQuery('#"+component.getMarkupId()+"').click(function(){ jQuery(this).attr('disabled',true); });");
    }
}
