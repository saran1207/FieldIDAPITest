package com.n4systems.fieldid.wicket.components.eventform.details.oneclick;

import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.TooltipImage;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Button;
import com.n4systems.model.ButtonGroup;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.List;

public class ButtonGroupDisplayPanel extends Panel {

    public ButtonGroupDisplayPanel(String id, IModel<ButtonGroup> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(new ListView<Button>("buttonGroup", new PropertyModel<List<Button>>(model, "availableButtons")) {
            @Override
            protected void populateItem(ListItem<Button> item) {
                Button button = item.getModelObject();
                String buttonName = button.getButtonName();
                ContextImage image = new ContextImage("buttonImage", "images/eventButtons/"+buttonName+".png");
                item.add(image);
                item.add(new Label("buttonLabel", new PropertyModel<String>(item.getModel(), "displayText")));

                AjaxLink addEditLogicLink;

                item.add(addEditLogicLink = new AjaxLink<Void>("addEditLogicLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        onConfigureCriteriaLogic(target, button);
                    }
                });

                if (hasRule(button)) {
                    addEditLogicLink.add(new FlatLabel("addEditLabel", new FIDLabelModel("label.edit_logic")));
                } else {
                    addEditLogicLink.add(new FlatLabel("addEditLabel", new FIDLabelModel("label.add_logic")));
                }


                item.add(new TooltipImage("tooltip", new StringResourceModel("label.tooltip.criteria_logic", this, null)));
            }
        });
    }

    protected void onConfigureCriteriaLogic(AjaxRequestTarget target, Button button) {}

    protected Boolean hasRule(Button button) { return false; }
}
