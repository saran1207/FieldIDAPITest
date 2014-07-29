package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.fieldid.service.event.massevent.MassEventService;
import com.n4systems.fieldid.service.event.massevent.SelectedEventTypeCount;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.fieldid.wicket.pages.event.PerformMultiEventPage;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class SelectMassEventPage extends FieldIDTemplatePage {

    @SpringBean
    private MassEventService massEventService;

    private final IModel<EventReportCriteria> criteriaModel;

    public SelectMassEventPage(IModel<EventReportCriteria> criteriaModel) {
        this.criteriaModel = criteriaModel;
        final List<Long> eventIds = criteriaModel.getObject().getSelection().getSelectedIds();

        List<SelectedEventTypeCount> eventTypeCounts = massEventService.countSelectedEventTypes(eventIds);
        final IModel<EventReportCriteria> criteriaModelToSend = criteriaModel;

        if(eventTypeCounts.size() == 1) {
            //TODO redirect to perform mass events page
            List<ThingEvent> openEvents = massEventService.getSelectedEventsByEventType(eventIds);
            throw new RestartResponseException(new PerformMultiEventPage(openEvents, criteriaModelToSend, false));
        } else {
            add(new ListView<SelectedEventTypeCount>("eventType", eventTypeCounts) {

                @Override
                protected void populateItem(final ListItem<SelectedEventTypeCount> item) {
                    item.add(new Label("name", new PropertyModel<String>(item.getModel(), "type.displayName")));
                    item.add(new Label("count", new PropertyModel<Long>(item.getModel(), "count")));
                    item.add(new Link("performEvents") {
                        @Override
                        public void onClick() {
                            //TODO setresponse page to perform mass events page
                            List<ThingEvent> openEvents = massEventService.getSelectedEventsByEventType(eventIds, item.getModelObject().type);
                            throw new RestartResponseException(new PerformMultiEventPage(openEvents, criteriaModelToSend, true));
                        }
                    });
                }
            });
        }
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.select_event_type_to_perform"));
    }

    @Override
    protected Component createBackToLink(String linkId, String linkLabelId) {
        return new Link(linkId) {
            @Override
            public void onClick() {
               setResponsePage(new ReportPage(criteriaModel.getObject()));
            }
        }.add(new Label(linkLabelId, new FIDLabelModel("label.back_to_x", new FIDLabelModel("label.reporting_results").getObject())));
    }
}
