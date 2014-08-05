package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-08-05.
 */
public class EventTypeGroupCell extends Panel {

    public EventTypeGroupCell(String id, IModel<? extends EventTypeGroup> eventTypeGroupModel) {
        super(id);

        EventTypeGroup eventTypeGroup = eventTypeGroupModel.getObject();

        /*
        Link link = new Link("eventTypeGroupLink") {
            @Override
            public void onClick() {
                setResponsePage(ReassignEventTypeGroupPage.class, PageParametersBuilder.uniqueId(eventTypeGroup.getId()));
            }
        };
        link.add(new Label("name", eventTypeGroupModel.getObject().getDisplayName()));
        link.setVisible(eventTypeGroup.isActive());
        add(link);
        */

        NonWicketLink link;
        link = new NonWicketLink("eventTypeGroupLink", "eventTypeGroup.action?uniqueID=" + eventTypeGroupModel.getObject().getId());
        link.add(new Label("name", eventTypeGroupModel.getObject().getDisplayName()));
        link.setVisible(eventTypeGroup.isActive());
        add(link);


        Label label;
        label = new Label("nameArchived", eventTypeGroupModel.getObject().getDisplayName());
        label.setVisible(eventTypeGroup.isArchived());
        add(label);

    }
}