package com.n4systems.fieldid.wicket.pages.loto.definition;

import com.n4systems.model.procedure.ProcedureDefinition;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class PublishPanel extends Panel {

    public PublishPanel(String id, IModel<ProcedureDefinition> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(new Label("message", Model.of("are you sure you want to publish this?")));
        add(new AjaxLink("cancel") {
            @Override public void onClick(AjaxRequestTarget target) {
                doCancel(target);
            }
        });

        add(new AjaxSubmitLink("publish") {
            @Override protected void onSubmit(AjaxRequestTarget target, Form<?> form) { doPublish(target); }

            @Override protected void onError(AjaxRequestTarget target, Form<?> form) { /*???*/ }
        });
    }

    protected void doPublish(AjaxRequestTarget target) { }

    protected void doCancel(AjaxRequestTarget target) {}

}
