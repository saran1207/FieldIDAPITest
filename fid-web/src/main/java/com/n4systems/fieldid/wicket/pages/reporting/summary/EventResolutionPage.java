package com.n4systems.fieldid.wicket.pages.reporting.summary;

import com.n4systems.fieldid.service.search.EventResolutionService;
import com.n4systems.fieldid.wicket.components.FlatLabel;
import com.n4systems.fieldid.wicket.components.navigation.MattBar;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.pages.reporting.ReportingResultsPage;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventType;
import com.n4systems.model.Status;
import com.n4systems.model.search.EventReportCriteriaModel;
import com.n4systems.model.search.EventStatus;
import com.n4systems.model.summary.EventResolutionSummary;
import com.n4systems.model.summary.EventSetSummary;
import com.n4systems.util.chart.RangeType;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventResolutionPage extends FieldIDFrontEndPage {

    @SpringBean
    private EventResolutionService eventResolutionService;

    private EventResolutionSummary eventResolutionSummary;

    private WebMarkupContainer breakdownContainer;
    private ListView<EventSetSummary> summaryList;
    private EventReportCriteriaModel criteria;

    public EventResolutionPage(IModel<EventReportCriteriaModel> criteriaModel) {
        criteria = criteriaModel.getObject();
        eventResolutionSummary = eventResolutionService.getEventResolutionSummary(criteria);

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
                } else if (state.equals(3)) {
                    summaryList.setModel(createDailyBreakdownSummaryModel());
                }
                target.add(breakdownContainer);
            }
        };

        mattBar.setCurrentState(1);
        mattBar.addLink(new Model<String>("Event Types"), 1);
        mattBar.addLink(new Model<String>("Asset Types"), 2);
        mattBar.addLink(new Model<String>("Daily Breakdown"), 3);

        add(mattBar);

        breakdownContainer = new WebMarkupContainer("breakdownContainer");
        breakdownContainer.setOutputMarkupPlaceholderTag(true);

        breakdownContainer.add(summaryList = new ListView<EventSetSummary>("summaryList", createEventBreakdownSummaryModel()) {
            @Override
            protected void populateItem(ListItem<EventSetSummary> item) {
                Link itemCriteriaLink = createItemCriteriaLink("itemCriteriaLink", item.getModel());
                itemCriteriaLink.add(new FlatLabel("name", new PropertyModel<String>(item.getModel(), "name")));
                item.add(itemCriteriaLink);

                item.add(new FlatLabel("eventsDue", new PropertyModel<String>(item.getModel(), "eventsDue")));
                item.add(new FlatLabel("eventsCompleted", new PropertyModel<String>(item.getModel(), "eventsCompleted")));

                Link outstandingLink = createOutstandingItemCriteriaLink("outstandingCriteriaLink", item.getModel());
                outstandingLink.add(new FlatLabel("eventsOutstanding", new PropertyModel<String>(item.getModel(), "eventsOutstanding")));
                item.add(outstandingLink);

                item.add(new FlatLabel("eventsPassed", new PropertyModel<String>(item.getModel(), "eventsPassed")));

                Link failedLink = createFailedItemCriteriaLink("failedCriteriaLink", item.getModel());
                failedLink.add(new FlatLabel("eventsFailed", new PropertyModel<String>(item.getModel(), "eventsFailed")));
                item.add(failedLink);

                if (item.getIndex() % 2 == 1) {
                    item.add(new AttributeAppender("class", "odd"));
                }
            }
        });

        add(breakdownContainer);
    }

    private Link createFailedItemCriteriaLink(String linkId, final IModel<EventSetSummary> model) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                EventReportCriteriaModel criteria = copyOfCriteria();
                populateCriteriaInto(model.getObject().getItem(), criteria);
                populateFailedInto(criteria);
                setResponsePage(new ReportingResultsPage(criteria));
            }
        };
    }

    private Link createOutstandingItemCriteriaLink(String linkId, final IModel<EventSetSummary> model) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                EventReportCriteriaModel criteria = copyOfCriteria();
                populateCriteriaInto(model.getObject().getItem(), criteria);
                populateOutstandingInto(criteria);
                setResponsePage(new ReportingResultsPage(criteria));
            }
        };
    }

    private Link createItemCriteriaLink(String linkId, final IModel<EventSetSummary> model) {
        return new Link(linkId) {
            @Override
            public void onClick() {
                EventReportCriteriaModel criteria = copyOfCriteria();
                populateCriteriaInto(model.getObject().getItem(), criteria);
                setResponsePage(new ReportingResultsPage(criteria));
            }
        };
    }

    private void populateCriteriaInto(Object item, EventReportCriteriaModel criteria) {
        if (item instanceof Date) {
            criteria.getDateRange().setRangeType(RangeType.CUSTOM);
            criteria.getDateRange().setFromDate((Date) item);
            criteria.getDateRange().setToDate((Date) item);
        } else if (item instanceof AssetType) {
            criteria.setAssetType((AssetType) item);
        } else if (item instanceof EventType) {
            criteria.setEventType((EventType) item);
        }
    }

    private void populateOutstandingInto(EventReportCriteriaModel criteriaModel) {
        criteriaModel.setEventStatus(EventStatus.INCOMPLETE);
    }

    private void populateFailedInto(EventReportCriteriaModel criteriaModel) {
        criteriaModel.setResult(Status.FAIL);
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

    private IModel<List<EventSetSummary>> createDailyBreakdownSummaryModel() {
        return new LoadableDetachableModel<List<EventSetSummary>>() {
            @Override
            protected List<EventSetSummary> load() {
                return new ArrayList<EventSetSummary>(eventResolutionSummary.getDateEventSummaries().values());
            }
        };
    }

    @Override
    protected Label createTitleLabel(String labelId) {
        return new Label(labelId, new EventResolutionTitleModel(new PropertyModel<EventReportCriteriaModel>(this,"criteria")));
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event_resolution/event_resolution.css");
    }

    private EventReportCriteriaModel copyOfCriteria() {
        try {
            return (EventReportCriteriaModel) criteria.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }


}
