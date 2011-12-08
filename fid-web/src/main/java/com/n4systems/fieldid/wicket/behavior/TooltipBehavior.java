package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.model.IModel;

public class TooltipBehavior extends AbstractBehavior {

    private IModel<String> tooltipText;
    private Component component;

    public TooltipBehavior(IModel<String> tooltipText) {
        this.tooltipText = tooltipText;
    }

    @Override
    public void bind(Component component) {
        this.component = component;
        component.setOutputMarkupId(true);
        component.add(new AttributeModifier("title", true, tooltipText));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        JavascriptPackageResource.getHeaderContribution("javascript/jquery.tools.min.js").renderHead(response);
        CSSPackageResource.getHeaderContribution("style/newCss/behavior/tooltip.css").renderHead(response);
        response.renderOnDomReadyJavascript("$('#"+component.getMarkupId()+"').tooltip();");
    }

}
