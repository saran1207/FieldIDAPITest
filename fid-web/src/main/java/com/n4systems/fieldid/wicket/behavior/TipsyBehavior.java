package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class TipsyBehavior extends Behavior {

    private static final String TIPSY_JS  = "$('%s').tipsy({gravity: '%s', fade:true, delayIn:250, live:true})";
    private boolean hideTooltipsOnLinkClick = false;
    private String selector = null;
    private Gravity gravity;
    private IModel<String> title = null;

    public enum Gravity { S,N,E,W,SW,NW,SE,NE }

    public TipsyBehavior(Gravity gravity) {
        this(null, gravity);
    }

    public TipsyBehavior(String title) {
        this(Model.of(title));
    }

    public TipsyBehavior(IModel<String> title) {
        this(title, Gravity.E);
    }

    public TipsyBehavior(IModel<String> title, Gravity gravity) {
        this.gravity = gravity;
        this.title = title;
    }

    public TipsyBehavior withSelector(String selector) {
        this.selector = selector;
        return this;
    }

    @Override
    public void bind(Component component) {
        if (title != null) {
            component.add(new AttributeModifier("title", title));
        }
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        response.renderCSSReference("style/legacy/tipsy/tipsy.css");
        response.renderOnDomReadyJavaScript(String.format(TIPSY_JS, getSelector(component), gravity.toString().toLowerCase()));
        if (hideTooltipsOnLinkClick) {
            response.renderOnDomReadyJavaScript("$('a.tipsy-tooltip').click(function() { $('.tipsy').remove(); })");
        }
    }

    private String getSelector(Component component) {
        return selector==null ? "#"+component.getMarkupId() : selector;
    }

    public TipsyBehavior hideTooltipsOnLinkClick() {
        hideTooltipsOnLinkClick = true;
        return this;
    }
}
