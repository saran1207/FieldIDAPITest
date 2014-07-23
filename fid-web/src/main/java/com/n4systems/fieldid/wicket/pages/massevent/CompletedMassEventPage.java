package com.n4systems.fieldid.wicket.pages.massevent;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.event.massevent.MassEventService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.search.EventReportCriteria;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class CompletedMassEventPage extends FieldIDTemplatePage {

    private IModel<EventReportCriteria> criteriaModel;

    private List<ThingEvent> events;

    @SpringBean
    private MassEventService massEventService;

    @SpringBean
    private SavedReportService savedReportService;


    //Test Constructor
    @Deprecated
    public CompletedMassEventPage() {
        events = massEventService.getSelectedEventsById(Lists.newArrayList(4101826L, 4100604L, 4101516L));
    }

    public CompletedMassEventPage(List<ThingEvent> events) {
        this.criteriaModel = criteriaModel;
        this.events = events;
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new ListView<ThingEvent>("events", events) {

            @Override
            protected void populateItem(ListItem<ThingEvent> item) {
                item.add(new Label("serialNumber", new PropertyModel<String>(item.getModel(), "asset.identifier" )));
                item.add(new Label("eventType", new PropertyModel<String>(item.getModel(), "type.displayName" )));
                item.add(new Label("assetType", new PropertyModel<String>(item.getModel(), "asset.type.displayName" )));
                item.add(new Label("dueDate", new DayDisplayModel(new PropertyModel<Date>(item.getModel(), "dueDate"), true)));
                item.add(new Label("owner", new PropertyModel<String>(item.getModel(), "owner.displayName" )));
                item.add(new Label("eventStatus", new PropertyModel<String>(item.getModel(), "eventStatus.displayName" )));
            }
        });

        add(new Link<Void>("backToReport") {
            @Override
            public void onClick() {
                setResponsePage(new ReportPage(savedReportService.retrieveLastSearch()));
            }
        });

    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.completed_events"));
    }

}
