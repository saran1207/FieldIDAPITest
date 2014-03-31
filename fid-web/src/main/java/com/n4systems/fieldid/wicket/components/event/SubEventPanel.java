package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.reporting.SlidingCollapsibleContainer;
import com.n4systems.model.Event;
import com.n4systems.model.SubEvent;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class SubEventPanel extends Panel {

    public SubEventPanel(String id, IModel<? extends Event> model) {
        super(id);

        setVisible(!model.getObject().getSubEvents().isEmpty());

        add(new ListView<SubEvent>("subEvent", new PropertyModel<List<SubEvent>>(model, "subEvents")) {
            @Override
            protected void populateItem(ListItem<SubEvent> item) {

                String subEventAssetLabel = item.getModelObject().getName() != null ? item.getModelObject().getName() : item.getModelObject().getAsset().getIdentifier();
                String subEventLabel = item.getModelObject().getType().getDisplayName() + " - " + subEventAssetLabel;

                SlidingCollapsibleContainer collapsibleContainer;
                item.add(collapsibleContainer = new SlidingCollapsibleContainer("collapsibleContainer", Model.of(subEventLabel)));

                collapsibleContainer.addContainedPanel(new SubEventDetailsPanel("details", item.getModel()));
            }

        });


    }
}
