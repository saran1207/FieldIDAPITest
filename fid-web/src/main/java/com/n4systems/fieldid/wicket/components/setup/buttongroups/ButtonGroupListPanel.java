package com.n4systems.fieldid.wicket.components.setup.buttongroups;

import com.n4systems.model.ButtonGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * This is the Panel that holds an individual ButtonGroup.  It does not hold the List portion of the panel, that's
 * injected inside of this.  This portion mainly holds the model that will be displayed, along with the name field for
 * the button group as a whole.
 *
 * Created by Jordan Heath on 2016-03-14.
 */
public class ButtonGroupListPanel extends Panel {
    private IModel<ButtonGroup> buttonGroupModel;

    public ButtonGroupListPanel(String id, IModel<ButtonGroup> model) {
        super(id, model);
        this.buttonGroupModel = model;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        TextField<String> buttonGroupNameField;

        add(buttonGroupNameField = new TextField<>("buttonGroupNameField", new PropertyModel<>(buttonGroupModel.getObject(), "name")));

        buttonGroupNameField.add(new OnChangeAjaxBehavior() {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                buttonGroupModel.getObject().setChanged(true);
            }
        });

        add(new ButtonPanel("buttonListPanel", new PropertyModel<>(buttonGroupModel.getObject(), "buttons")) {
            @Override
            protected void didUpdateButtons() {
                didUpdateModel();
            }
        });
    }

    private void didUpdateModel() {
        buttonGroupModel.getObject().setChanged(true);
    }
}
