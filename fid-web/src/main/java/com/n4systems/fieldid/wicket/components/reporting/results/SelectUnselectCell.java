package com.n4systems.fieldid.wicket.components.reporting.results;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class SelectUnselectCell extends Panel {

    public SelectUnselectCell(String id, final IModel<Boolean> selectedModel) {
        super(id);

        add(new AjaxCheckBox("checkbox", selectedModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onSelectUnselect(target);
            }
        });
    }

    protected void onSelectUnselect(AjaxRequestTarget target) { }

}
