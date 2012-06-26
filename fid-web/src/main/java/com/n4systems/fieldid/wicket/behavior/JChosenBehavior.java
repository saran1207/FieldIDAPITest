package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.Model;

public class JChosenBehavior extends Behavior {

    public JChosenBehavior() {
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        component.add(new AttributeAppender("class", Model.of("chzn-select"), " "));
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/chosen/chosen.jquery.js");
        response.renderCSSReference("style/chosen/chosen.css");
        response.renderOnDomReadyJavaScript("$('#"+component.getMarkupId()+"').chosen();");
    }
}
