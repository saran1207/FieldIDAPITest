package com.n4systems.fieldid.wicket.pages.loto.definition;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ContentPanel extends Panel {

    public ContentPanel(String id) {
        super(id);
        setOutputMarkupPlaceholderTag(true);
        add(new AjaxLink("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });
        add(new AjaxLink("continue") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                doContinue(target);
            }
        });

    }

    protected void doCancel(AjaxRequestTarget target) { }

    protected void doContinue(AjaxRequestTarget target) { }

    public ContentPanel(String id, IModel<?> model) {
        super(id, model);
    }
}
