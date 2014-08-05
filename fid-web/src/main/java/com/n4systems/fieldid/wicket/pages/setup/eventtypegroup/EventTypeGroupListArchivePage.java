package com.n4systems.fieldid.wicket.pages.setup.eventtypegroup;

import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.api.Archivable;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;

import java.util.List;

/**
 * Created by rrana on 2014-08-05.
 */
public class EventTypeGroupListArchivePage extends EventTypeGroupPage {

    private EventTypeGroupPanel eventTypeGroupPanel;
    private EventTypeGroupDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;

    @Override
    protected void onInitialize() {
        super.onInitialize();

        feedbackPanel = new FIDFeedbackPanel("feedbackPanel");
        feedbackPanel.setOutputMarkupId(true);
        add(feedbackPanel);

        dataProvider = new EventTypeGroupDataProvider("name", SortOrder.ASCENDING, Archivable.EntityState.ARCHIVED);

        add(eventTypeGroupPanel = new EventTypeGroupPanel("procedureDefinitionListPanel", dataProvider) {
            @Override
            protected void addActionColumn(List<IColumn<? extends EventTypeGroup>> columns) {
                columns.add(getActionsColumn(this));
            }

            @Override
            protected FIDFeedbackPanel getErrorFeedbackPanel() {
                return feedbackPanel;
            }
        });
        eventTypeGroupPanel.setOutputMarkupPlaceholderTag(true);
    }

    protected AbstractColumn getActionsColumn(EventTypeGroupPanel eventTypeGroupPanel) {
        return new EventTypeGroupActionsColumn(eventTypeGroupPanel);
    }

}