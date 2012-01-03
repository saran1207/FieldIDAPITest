package com.n4systems.fieldid.wicket.components.search.results;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class SelectUnselectCell extends Panel {

    protected AjaxCheckBox selectCheckbox;

    public SelectUnselectCell(String id, final IModel<Boolean> selectedModel) {
        super(id);

        selectCheckbox = new AjaxCheckBox("checkbox", selectedModel) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onSelectUnselect(target);
            }
        };
        add(selectCheckbox);
    }

    protected void onSelectUnselect(AjaxRequestTarget target) { }

}
