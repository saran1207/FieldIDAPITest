package com.n4systems.fieldid.wicket.components.setup.loto;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.Panel;

public class LockoutReasonActionPanel extends Panel {

    public LockoutReasonActionPanel(String id) {
        super(id);
        add(new AjaxLink<Void>("addLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                onAddClicked(target);
            }
        });
    }

    protected void onAddClicked(AjaxRequestTarget target) {}
}
