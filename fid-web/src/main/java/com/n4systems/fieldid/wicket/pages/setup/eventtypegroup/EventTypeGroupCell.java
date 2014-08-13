package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.EventTypeGroup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Created by rrana on 2014-08-05.
 */
public class EventTypeGroupCell extends Panel {

    public EventTypeGroupCell(String id, IModel<? extends EventTypeGroup> eventTypeGroupModel) {
        super(id);

        final EventTypeGroup eventTypeGroup = eventTypeGroupModel.getObject();

        Link link = new BookmarkablePageLink("eventTypeGroupLink", EventTypeGroupViewPage.class, PageParametersBuilder.uniqueId(eventTypeGroup.getID()));
        link.add(new Label("name", eventTypeGroupModel.getObject().getDisplayName()));
        link.setVisible(eventTypeGroup.isActive());
        add(link);

        Label label;
        label = new Label("nameArchived", eventTypeGroupModel.getObject().getDisplayName());
        label.setVisible(eventTypeGroup.isArchived());
        add(label);

    }
}