package com.n4systems.fieldid.wicket.pages.setup.eventstatus;

import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.model.EventStatus;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Date;
import java.util.List;

public class EventStatusArchivedListPage extends EventStatusPage {

    public EventStatusArchivedListPage(PageParameters params) {
        super(params);
        
        List<EventStatus> eventStatusList = eventStatusService.getArchivedStatues();
        
        add(new ListView<EventStatus>("eventStatusList", eventStatusList) {
            @Override
            protected void populateItem(ListItem<EventStatus> item) {
                final EventStatus status = item.getModelObject();

                Label createdBy;
                Label modifiedBy;

                item.add(new Label("name", new PropertyModel<String>(status, "displayName")));

                item.add(createdBy = new Label("createdBy", new PropertyModel<Object>(status, "createdBy.userID")));
                createdBy.setVisible(status.getCreatedBy() != null);
                item.add(new DateTimeLabel("created", new PropertyModel<Date>(status, "created")));

                item.add(modifiedBy = new Label("modifiedBy", new PropertyModel<Object>(status, "modifiedBy.userID")));
                modifiedBy.setVisible(status.getModifiedBy() != null);
                item.add(new DateTimeLabel("lastModified", new PropertyModel<Date>(status, "modified")));

                item.add(new Link("unarchive") {
                    @Override
                    public void onClick() {
                        eventStatusService.unarchive(status);
                        setResponsePage(EventStatusListPage.class);
                    }
                });
            }
        }); 
        
    }
}
