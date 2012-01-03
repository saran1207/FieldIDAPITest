package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.PackageResourceReference;

public class TooltipBehavior extends Behavior implements IHeaderContributor {

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
        response.renderJavaScriptReference(new PackageResourceReference("javascript/jquery.tools.min.js"));
        response.renderCSSReference(new PackageResourceReference("style/newCss/behavior/tooltip.css"));
        response.renderOnLoadJavaScript("$('#" + component.getMarkupId() + "').tooltip();");
    }

}
