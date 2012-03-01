package com.n4systems.fieldid.wicket.pages.reporting.summary;

import com.n4systems.fieldid.service.search.EventResolutionService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.summary.EventResolutionSummary;
import com.n4systems.model.summary.EventSetSummary;
import com.sun.org.omg.CORBA.AttributeMode;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class EventResolutionPage extends FieldIDFrontEndPage {

    @SpringBean
    private EventResolutionService eventResolutionService;

    private EventResolutionSummary eventResolutionSummary;

    private WebMarkupContainer breakdownContainer;
    private ListView<EventSetSummary> summaryList;

    public EventResolutionPage(IModel<EventReportCriteriaModel> criteriaModel) {
        eventResolutionSummary = eventResolutionService.getEventResolutionSummary(criteriaModel.getObject());

        add(new ContextImage("printIcon", "images/print-icon.png"));

        add(new Label("eventsDue", new PropertyModel<String>(eventResolutionSummary.getBaseSummary(), "eventsDue")));
        add(new Label("eventsCompleted", new PropertyModel<String>(eventResolutionSummary.getBaseSummary(), "eventsCompleted")));
        add(new Label("eventsOutstanding", new PropertyModel<String>(eventResolutionSummary.getBaseSummary(), "eventsOutstanding")));
        add(new Label("eventsPassed", new PropertyModel<String>(eventResolutionSummary.getBaseSummary(), "eventsPassed")));
        add(new Label("eventsFailed", new PropertyModel<String>(eventResolutionSummary.getBaseSummary(), "eventsFailed")));

        add(new Label("passedPercentage", new PropertyModel<String>(eventResolutionSummary.getBaseSummary(), "passedPercentage")));
        add(new Label("failedPercentage", new PropertyModel<String>(eventResolutionSummary.getBaseSummary(), "failedPercentage")));

        MattBar mattBar = new MattBar("navigationBar") {
            @Override
            protected void onEnterState(AjaxRequestTarget target, Object state) {
                if (state.equals(1)) {
                    summaryList.setModel(createEventBreakdownSummaryModel());
                } else if (state.equals(2)) {
                    summaryList.setModel(createAssetBreakdownSummaryModel());
                }
                target.add(breakdownContainer);
            }
        };

        mattBar.setCurrentState(1);
        mattBar.addLink(new Model<String>("Event Types"), 1);
        mattBar.addLink(new Model<String>("Asset Types"), 2);
//        mattBar.addLink(new Model<String>("Daily Breakdown"), 3);

        add(mattBar);

        breakdownContainer = new WebMarkupContainer("breakdownContainer");
        breakdownContainer.setOutputMarkupPlaceholderTag(true);

        breakdownContainer.add(summaryList = new ListView<EventSetSummary>("summaryList", createEventBreakdownSummaryModel()) {
            @Override
            protected void populateItem(ListItem<EventSetSummary> item) {
                item.add(new FlatLabel("name", new PropertyModel<String>(item.getModel(), "name")));
                item.add(new FlatLabel("eventsDue", new PropertyModel<String>(item.getModel(), "eventsDue")));
                item.add(new FlatLabel("eventsCompleted", new PropertyModel<String>(item.getModel(), "eventsCompleted")));
                item.add(new FlatLabel("eventsOutstanding", new PropertyModel<String>(item.getModel(), "eventsOutstanding")));
                item.add(new FlatLabel("eventsPassed", new PropertyModel<String>(item.getModel(), "eventsPassed")));
                item.add(new FlatLabel("eventsFailed", new PropertyModel<String>(item.getModel(), "eventsFailed")));
                if (item.getIndex() % 2 == 1) {
                    item.add(new AttributeAppender("class", "odd"));
                }
            }
        });

        add(breakdownContainer);
    }

    private IModel<List<EventSetSummary>> createEventBreakdownSummaryModel() {
        return new LoadableDetachableModel<List<EventSetSummary>>() {
            @Override
            protected List<EventSetSummary> load() {
                return new ArrayList<EventSetSummary>(eventResolutionSummary.getEventTypeEventSummaries().values());
            }
        };
    }

    private IModel<List<EventSetSummary>> createAssetBreakdownSummaryModel() {
        return new LoadableDetachableModel<List<EventSetSummary>>() {
            @Override
            protected List<EventSetSummary> load() {
                return new ArrayList<EventSetSummary>(eventResolutionSummary.getAssetTypeEventSummaries().values());
            }
        };
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new FIDLabelModel("label.event_resolution"));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event_resolution/event_resolution.css");
    }

}
