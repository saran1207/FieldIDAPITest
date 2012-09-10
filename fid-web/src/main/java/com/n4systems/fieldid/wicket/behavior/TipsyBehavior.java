package com.n4systems.fieldid.wicket.behavior;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class TipsyBehavior extends Behavior {

    private static final String TIPSY_JS  = "$('%s').tipsy({gravity: '%s', fade:true, delayIn:100, live:true})";
    private String selector = null;
    private Gravity gravity;
    private String title = null;

    public enum Gravity { S,N,E,W };

    public TipsyBehavior(Gravity gravity) {
        this(null, gravity);
    }

    public TipsyBehavior(String title) {
        this(title, Gravity.E);
    }

    public TipsyBehavior(String title, Gravity gravity) {
        this.gravity = gravity;
        this.title = title;
    }

    public TipsyBehavior withSelector(String selector) {
        this.selector = selector;
        return this;
    }

    @Override
    public void bind(Component component) {
        if (title!=null) {
            component.add(new AttributeModifier("title", title));
        }
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        response.renderCSSReference("style/tipsy/tipsy.css");
        if (title!=null) {
            response.renderOnDomReadyJavaScript(String.format(TIPSY_JS, getSelector(component), gravity.toString().toLowerCase()));
        }
    }

    private String getSelector(Component component) {
        return selector==null ? "#"+component.getMarkupId() : selector;
    }
}
