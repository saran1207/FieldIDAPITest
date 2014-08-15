package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.service.event.EventTypeGroupService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.user.User;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created by rrana on 2014-08-05.
 */
public class EventTypeGroupActionsCell extends Panel {

    @SpringBean
    private EventTypeGroupService eventTypeGroupService;

    private Long eventTypeGroupId;

    public EventTypeGroupActionsCell(String id, final IModel<EventTypeGroup> eventTypeGroupModel, final EventTypeGroupPanel procedureListPanel) {
        super(id);

        final EventTypeGroup eventTypeGroup = eventTypeGroupModel.getObject();
        eventTypeGroupId = eventTypeGroupModel.getObject().getID();

        Link editLink;

        if(eventTypeGroup.isActive()) {
            editLink = new BookmarkablePageLink("editLink", EventTypeGroupEditPage.class, PageParametersBuilder.uniqueId(eventTypeGroupId));
            editLink.add(new Label("name", "Edit"));
        } else {
            editLink = new Link("editLink") {
                @Override
                public void onClick() {
                    eventTypeGroup.setState(Archivable.EntityState.ACTIVE);
                    User user = eventTypeGroupService.getUser(FieldIDSession.get().getSessionUser().getId());
                    eventTypeGroupService.update(eventTypeGroup, user);
                    setResponsePage(new EventTypeGroupListPage());
                }
            };
            editLink.add(new Label("name", "Unarchive"));
        }

        add(editLink);

        WebMarkupContainer optionsContainer = new WebMarkupContainer("optionsContainer");

        Link archiveLink = new Link("archiveLink") {
            @Override
            public void onClick() {
                setResponsePage(ReassignEventTypeGroupPage.class, PageParametersBuilder.uniqueId(eventTypeGroup.getId()));
            }
        };

        optionsContainer.add(archiveLink);

        add(optionsContainer);

        optionsContainer.setVisible(eventTypeGroup.isActive());
    }

}
