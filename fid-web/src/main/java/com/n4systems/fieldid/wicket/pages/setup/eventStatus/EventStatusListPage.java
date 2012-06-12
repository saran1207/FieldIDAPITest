package com.n4systems.fieldid.wicket.pages.setup.eventStatus;

import com.n4systems.fieldid.service.event.EventStatusService;
import com.n4systems.fieldid.wicket.components.DateTimeLabel;
import com.n4systems.fieldid.wicket.pages.EventStatusPage;
import com.n4systems.model.EventStatus;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

import static com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder.uniqueId;

public class EventStatusListPage extends EventStatusPage {

    @SpringBean
    protected EventStatusService eventStatusService;
            
    public EventStatusListPage(PageParameters params) {
        super(params);
        
        List<EventStatus> eventStatusList = eventStatusService.getActiveStatues();
        
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

                item.add(new BookmarkablePageLink("edit", EventStatusFormPage.class, uniqueId(status.getId())));
                item.add(new Link("archive") {
                    @Override
                    public void onClick() {
                        eventStatusService.archive(status);
                        setResponsePage(EventStatusListPage.class);
                    }
                });
            }
        }); 
        
    }
}
