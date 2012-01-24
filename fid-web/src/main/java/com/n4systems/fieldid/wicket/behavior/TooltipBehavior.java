package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;

public class TooltipBehavior extends Behavior {

    private IModel<String> tooltipText;

    public TooltipBehavior(IModel<String> tooltipText) {
        this.tooltipText = tooltipText;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
        component.add(new AttributeModifier("title", tooltipText));
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/jquery.tools.min.js");
        response.renderCSSReference("style/newCss/behavior/tooltip.css");
        response.renderOnDomReadyJavaScript("$('#" + component.getMarkupId() + "').tooltip();");
    }

}
