package com.n4systems.fieldid.wicket.pages.setup.eventstatus;

import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.model.EventStatus;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Date;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EventStatusListPage extends EventStatusPage {

    public EventStatusListPage(PageParameters params) {
        super(params);
        
        final List<EventStatus> eventStatusList = eventStatusService.getActiveStatuses();
        
        add(new ListView<EventStatus>("eventStatusList", eventStatusList) {
            @Override
            protected void populateItem(ListItem<EventStatus> item) {
                final EventStatus status = item.getModelObject();

                item.add(new Label("name", new PropertyModel<String>(status, "displayName")));

                item.add(new Label("createdBy", new PropertyModel<Object>(status, "createdBy.fullName")));

                item.add(new DateTimeLabel("created", new PropertyModel<Date>(status, "created")));

                item.add(new Label("modifiedBy", new PropertyModel<Object>(status, "modifiedBy.fullName")));

                item.add(new DateTimeLabel("lastModified", new PropertyModel<Date>(status, "modified")));

                Link editLink = new BookmarkablePageLink("edit", EventStatusFormPage.class, uniqueId(status.getId()));
                item.add(editLink);

                WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

                Link archiveLink =new Link("archive") {
                    @Override
                    public void onClick() {

                        List<EventStatus> evntStatusList = eventStatusService.getActiveStatuses();
                        if (evntStatusList.size() > 1) {
                            eventStatusService.archive(status);
                            setResponsePage(EventStatusListPage.class);
                        }
                    }

                    @Override public boolean isVisible() {
                        return eventStatusList.size() > 1;
                    }
                };

                optionsContainer.add(archiveLink);

                item.add(optionsContainer);
            }
        }); 
        
    }
}
