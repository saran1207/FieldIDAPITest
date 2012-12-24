package com.n4systems.fieldid.wicket.components.eventform.details.oneclick;

import com.n4systems.model.Button;
import com.n4systems.model.ButtonGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

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
            }
        });
    }

    @Override
    public boolean isVisible() {
        return true;// getDefaultModelObject() != null;
    }
}
