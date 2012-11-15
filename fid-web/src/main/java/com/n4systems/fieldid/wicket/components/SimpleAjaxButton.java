package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class SimpleAjaxButton extends Panel {

    public SimpleAjaxButton(String id) {
        this(id, new Model<String>("Submit"));
    }

    public SimpleAjaxButton(String id, final IModel<String> buttonLabel) {
        super(id);

        Form simpleForm = new Form("simpleForm");
        simpleForm.add(new AjaxButton("button") {
            {add(new AttributeModifier("value", buttonLabel)); }
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                onClick(target);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                onClick(target);
            }
        });
        add(simpleForm);
    }

    protected void onClick(AjaxRequestTarget target) { }

}
