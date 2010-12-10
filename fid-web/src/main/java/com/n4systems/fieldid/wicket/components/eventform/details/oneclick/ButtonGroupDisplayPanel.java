package com.n4systems.fieldid.wicket.components.eventform.details.oneclick;

import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class ButtonGroupDisplayPanel extends Panel {

    public ButtonGroupDisplayPanel(String id, IModel<StateSet> model) {
        super(id, model);
        setOutputMarkupPlaceholderTag(true);

        add(new ListView<State>("buttonGroup", new PropertyModel<List<State>>(model, "states")) {
            @Override
            protected void populateItem(ListItem<State> item) {
                State state = item.getModelObject();
                String buttonName = state.getButtonName();
                ContextImage image = new ContextImage("buttonImage", "images/eventButtons/"+buttonName+".png");
                item.add(image);
                item.add(new Label("buttonLabel", new PropertyModel<String>(item.getModel(), "displayText")));
            }
        });
    }

    @Override
    public boolean isVisible() {
        return getDefaultModelObject() != null;
    }
}
