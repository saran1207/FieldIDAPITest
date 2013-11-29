package com.n4systems.fieldid.wicket.components.form;

import com.google.common.base.Preconditions;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;

public class LinkFieldsBehavior extends AbstractBehavior {

    private final String LINK_JS = "$('#%s').bind('change keyup', function() { $('%s').%s($('#%s').val());})";


    private final String target;
    private String method = null;

    public LinkFieldsBehavior(String target) {
        this.target = target;
    }

    public LinkFieldsBehavior forInputField() {
        method = "val";
        return this;
    }

    public LinkFieldsBehavior forTextField() {
        method = "text";
        return this;
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        Preconditions.checkState(method!=null,"you must specify whether it is for text field or input field when using " + getClass().getSimpleName());
        String id = component.getMarkupId();
        response.renderOnDomReadyJavaScript(String.format(LINK_JS, id,target, method, id));
    }

}