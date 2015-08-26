package com.n4systems.fieldid.wicket.pages.masterevent;

import com.n4systems.fieldid.service.event.AssociatedEventTypesService;
import com.n4systems.fieldid.wicket.behavior.validation.DisableNavigationConfirmationBehavior;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.*;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class SelectSubEventPanel extends Panel {

    @SpringBean
    private AssociatedEventTypesService associatedEventTypesService;

    public SelectSubEventPanel(String id, IModel<ThingEvent> masterEventModel, Asset asset) {
        super(id, masterEventModel);

        add(new Label("headerLabel", new FIDLabelModel("label.select_event_type_for", asset.getType().getDisplayName().toUpperCase())));
        add(new ListView<AssociatedEventType>("eventType", associatedEventTypesService.getAssociatedEventTypes(asset.getType())) {
            @Override
            protected void populateItem(ListItem<AssociatedEventType> item) {
                EventType eventType = item.getModelObject().getEventType();

                item.add(new Link<Void>("selectEventTypeLink") {
                    @Override
                    public void onClick() {
                        onPerformSubEvent(masterEventModel);
                        setResponsePage(new PerformSubEventPage(masterEventModel, asset.getId(), eventType.getId()));
                    }
                }.add(new FlatLabel("name", new PropertyModel<String>(eventType, "displayName")))
                .add(new DisableNavigationConfirmationBehavior()));
            }
        });
    }

    protected void onPerformSubEvent(IModel<ThingEvent> masterEventModel) {};
}
