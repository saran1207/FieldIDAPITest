package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class TwoStateAjaxButton extends Panel {

    private boolean inInitialState = true;

    private String stateOneLabel;
    private String stateTwoLabel;

    private String currentStateLabel;

    private AjaxButton button;
    private Label buttonLabel;

    public TwoStateAjaxButton(String id, String stateOneLabel, String stateTwoLabel) {
        super(id);
        this.stateOneLabel = stateOneLabel;
        this.stateTwoLabel = stateTwoLabel;
        this.currentStateLabel = stateOneLabel;
        setOutputMarkupId(true);

        Form form = new Form("theForm");
        add(form);
        form.add(button = new AjaxButton("button") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                inInitialState = !inInitialState;
                callCorrectCallback(target);
                setCorrectStateLabel();
                target.addComponent(TwoStateAjaxButton.this);
            }
        });
        button.add(buttonLabel = new Label("buttonLabel", new PropertyModel<String>(this, "currentStateLabel")));
    }

    private void setCorrectStateLabel() {
        currentStateLabel = inInitialState ? stateOneLabel : stateTwoLabel;
    }

    private void callCorrectCallback(AjaxRequestTarget target) {
        if (inInitialState) {
            onEnterInitialState(target);
        } else {
            onEnterSecondaryState(target);
        }
    }

    protected void onEnterInitialState(AjaxRequestTarget target) { }

    protected void onEnterSecondaryState(AjaxRequestTarget target) { }

}
