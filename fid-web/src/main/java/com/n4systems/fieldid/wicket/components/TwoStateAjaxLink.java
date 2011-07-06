package com.n4systems.fieldid.wicket.components;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class TwoStateAjaxLink extends Panel {

    private boolean inInitialState = true;

    private String stateOneLabel;
    private String stateTwoLabel;

    private String currentStateLabel;

    private Label linkLabel;
    private AjaxLink link;

    public TwoStateAjaxLink(String id, String stateOneLabel, String stateTwoLabel) {
        super(id);
        this.stateOneLabel = stateOneLabel;
        this.stateTwoLabel = stateTwoLabel;
        this.currentStateLabel = stateOneLabel;
        setOutputMarkupId(true);

        add(link = new AjaxLink("link") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                inInitialState = !inInitialState;
                callCorrectCallback(target);
                setCorrectStateLabel();
                target.addComponent(TwoStateAjaxLink.this);
            }
        });
        link.add(linkLabel = new FlatLabel("linkLabel", new PropertyModel<String>(this, "currentStateLabel")));
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
