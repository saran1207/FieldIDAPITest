package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class PublishPanel extends Panel {

    public PublishPanel(String id, IModel<ProcedureDefinition> model, Form form) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);
        add(new AttributeAppender("class",Model.of("publish")));

        add(new Label("message", Model.of("are you sure you want to publish this?")));
        add(new AjaxLink("cancel") {
            @Override public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });

        add(new SubmitLink("publish", form) {
            @Override
            public void onSubmit() {
                doPublish();
            }

            @Override
            public void onError() {
                doCancel(null);
            }

        });
    }

    protected void doPublish() { }

    protected void doCancel(AjaxRequestTarget target) {}

}
