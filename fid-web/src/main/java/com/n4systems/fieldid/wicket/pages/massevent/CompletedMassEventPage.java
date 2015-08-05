package com.n4systems.fieldid.wicket.pages.massevent;

import com.n4systems.fieldid.service.event.massevent.MassEventService;
import com.n4systems.fieldid.service.search.SavedAssetSearchService;
import com.n4systems.fieldid.service.search.SavedReportService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.model.DayDisplayModel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDTemplatePage;
import com.n4systems.fieldid.wicket.pages.asset.AssetSummaryPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.ReportPage;
import com.n4systems.fieldid.wicket.pages.assetsearch.SearchPage;
import com.n4systems.fieldid.wicket.pages.event.ThingEventSummaryPage;
import com.n4systems.model.ThingEvent;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Date;
import java.util.List;

public class CompletedMassEventPage extends FieldIDTemplatePage {

    private List<ThingEvent> events;

    @SpringBean
    private MassEventService massEventService;

    @SpringBean
    private SavedReportService savedReportService;

    @SpringBean
    private SavedAssetSearchService savedAssetSearchService;

    public CompletedMassEventPage(List<ThingEvent> events, MultiEventPage.MassEventOrigin massEventOrigin) {
        this.events = events;

        add(new ListView<ThingEvent>("events", events) {

            @Override
            protected void populateItem(ListItem<ThingEvent> item) {
                item.add(new BookmarkablePageLink<AssetSummaryPage>("assetSummaryLink", AssetSummaryPage.class, PageParametersBuilder.uniqueId(item.getModelObject().getAsset().getId()))
                        .add(new Label("serialNumber", new PropertyModel<String>(item.getModel(), "asset.identifier"))));
                item.add(new Label("eventType", new PropertyModel<String>(item.getModel(), "type.displayName")));
                item.add(new Label("assetType", new PropertyModel<String>(item.getModel(), "asset.type.displayName")));
                item.add(new Label("dueDate", new DayDisplayModel(new PropertyModel<Date>(item.getModel(), "dueDate"), true)));
                item.add(new Label("owner", new PropertyModel<String>(item.getModel(), "owner.displayName")));
                item.add(new Label("eventStatus", new PropertyModel<String>(item.getModel(), "eventStatus.displayName")));
                item.add(new BookmarkablePageLink<ThingEventSummaryPage>("eventSummaryLink", ThingEventSummaryPage.class, PageParametersBuilder.id(item.getModelObject().getId())));
            }
        });
        Link backToLink;
        add(backToLink = new Link<Void>("backToReport") {
            @Override
            public void onClick() {
                if (massEventOrigin.equals(MultiEventPage.MassEventOrigin.SEARCH)) {
                    setResponsePage(new SearchPage(savedAssetSearchService.retrieveLastSearch()));
                } else if (massEventOrigin.equals(MultiEventPage.MassEventOrigin.REPORTING)) {
                    setResponsePage(new ReportPage(savedReportService.retrieveLastSearch()));
                } else {
                    redirect("/startEvent.action");
                }
            }
        });

        if (massEventOrigin.equals(MultiEventPage.MassEventOrigin.SEARCH)) {
            backToLink.add(new FlatLabel("backToLabel", new FIDLabelModel("label.search_results")));
        } else if (massEventOrigin.equals(MultiEventPage.MassEventOrigin.REPORTING)) {
            backToLink.add(new FlatLabel("backToLabel", new FIDLabelModel("label.reporting_results")));
        } else {
            backToLink.add(new FlatLabel("backToLabel", new FIDLabelModel("label.startevent")));
        }
    }

    @Override
    protected Component createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("title.completed_x_events", events.size()));
    }

}
