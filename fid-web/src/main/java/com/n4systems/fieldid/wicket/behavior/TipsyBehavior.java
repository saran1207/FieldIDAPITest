package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class TipsyBehavior extends Behavior {

    private static final String TIPSY_JS  = "$('%s').tipsy({gravity: '%s', fade:true, delayIn:50})";
    private String selector;
    private Gravity gravity;

    public enum Gravity { S,N,E,W };

    public TipsyBehavior(String selector) {
        this(selector,Gravity.E);
    }

    public TipsyBehavior(String selector, Gravity gravity) {
        this.selector = selector;
        this.gravity = gravity;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        response.renderCSSReference("style/tipsy/tipsy.css");
        response.renderOnDomReadyJavaScript(String.format(TIPSY_JS, selector, gravity.toString().toLowerCase()));
    }
}
