package com.n4systems.fieldid.wicket.components.asset.events.table;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.event.ThingEventSummaryPage;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class SafetyNetworkActionsCell extends Panel {

    public SafetyNetworkActionsCell(String id, IModel<ThingEvent> model) {
        super(id, model);

        if (model.getObject().getWorkflowState() != WorkflowState.COMPLETED) {
            setVisible(false);
        }

        Long eventId = model.getObject().getId();

        add(new BookmarkablePageLink<ThingEventSummaryPage>("viewLink", ThingEventSummaryPage.class, PageParametersBuilder.id(eventId)));
    }

}
