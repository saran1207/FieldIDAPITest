package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventMapPanel;
import com.n4systems.fieldid.wicket.components.org.events.FilterPanel;
import com.n4systems.fieldid.wicket.components.org.events.table.ActionsColumn;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Event;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Iterator;
import java.util.List;

public class PlaceEventsPage extends PlacePage {

    // NOTE TO DIANA : used this as parameter.  if non-null then set filter flag to Open Only.
    public static final String OPEN_PARAM = "open";
    public static final List<WorkflowState> ALL_WORKFLOW_STATES = Lists.newArrayList(WorkflowState.OPEN, WorkflowState.COMPLETED, WorkflowState.CLOSED);

    @SpringBean
    private PlaceService placeService;

    private EventListPanel eventPanel;
    private EventMapPanel mapPanel;
    private FilterPanel filterPanel;
    private WebMarkupContainer blankSlate;

    public void setWorkflowStates(List<WorkflowState> workflowStates) {
        this.workflowStates = workflowStates;
    }

    protected List<WorkflowState> workflowStates;

    public PlaceEventsPage(PageParameters params) {
        super(params);
        if (!params.get(OPEN_PARAM).isEmpty())
            workflowStates = Lists.newArrayList(WorkflowState.OPEN);
        else
            workflowStates = ALL_WORKFLOW_STATES;
        init();
    }

    public PlaceEventsPage(IModel<BaseOrg> model) {
        super(model);

        workflowStates = ALL_WORKFLOW_STATES;
        init();
    }

    private final void init() {

        boolean hasEvents = placeService.countEventsFor(orgModel.getObject()) > 0;

        add(filterPanel = new FilterPanel("filterPanel", orgModel, workflowStates) {
            @Override
            public void onFilterSelectionChanged(AjaxRequestTarget target) {
                PlaceEventsPage.this.setWorkflowStates(getWorkflowStates());
                target.add(eventPanel);
            }
        });
        filterPanel.setOutputMarkupPlaceholderTag(true);
        filterPanel.setVisible(hasEvents);

        add(eventPanel = new EventListPanel("eventPanel", new PlaceEventDataProvider("completedDate", SortOrder.DESCENDING)) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends Event>> columns) {
                //TODO add place status
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends Event>> columns) {
                columns.add(new ActionsColumn(this));
            }
        });
        eventPanel.setOutputMarkupPlaceholderTag(true);
        add(mapPanel = new EventMapPanel("mapPanel", placeService.getEventsFor(orgModel.getObject())));
        eventPanel.setVisible(hasEvents);
        mapPanel.setOutputMarkupPlaceholderTag(true);
        mapPanel.setVisible(false);

        add(new AjaxLink<Void>("listLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                eventPanel.setVisible(true);
                mapPanel.setVisible(false);
                filterPanel.setVisible(true);
                target.add(eventPanel, mapPanel, filterPanel);
            }
        }.add(new TipsyBehavior(new FIDLabelModel("label.list"), TipsyBehavior.Gravity.NW)));

        add(new AjaxLink<Void>("mapLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                eventPanel.setVisible(false);
                mapPanel.setVisible(true);
                filterPanel.setVisible(false);
                target.add(eventPanel);
                target.add(mapPanel);
                target.add(eventPanel, mapPanel, filterPanel);
            }
        }.add(new TipsyBehavior(new FIDLabelModel("label.map"), TipsyBehavior.Gravity.NW)));

        add(blankSlate = new WebMarkupContainer("blankSlate"));
        blankSlate.setOutputMarkupPlaceholderTag(true);
        blankSlate.add(new Label("blankSlateTitle", new PropertyModel(orgModel, "name")));
        blankSlate.add(new Label("orgNameLabel", new PropertyModel(orgModel, "name")));
        blankSlate.setVisible(!hasEvents);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        //Needs to be included because the map panel is initially hidden.
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);

    }

    @Override
    public String getMainCss() {
        return "place-event";
    }

    private class PlaceEventDataProvider extends FieldIDDataProvider<Event> {

        public PlaceEventDataProvider(String order, SortOrder sortOrder) {
            setSort(order, sortOrder);
        }

        @Override
        public Iterator<? extends Event> iterator(int first, int count) {
            List<? extends Event> eventsList = placeService.getEventsFor(orgModel.getObject(), getSort().getProperty(), getSort().isAscending(), workflowStates);
            eventsList = eventsList.subList(first, first + count);
            return eventsList.iterator();
        }

        @Override
        public int size() {
            return placeService.countEventsFor(orgModel.getObject());
        }

        @Override
        public IModel<Event> model(final Event object) {
            return new AbstractReadOnlyModel<Event>() {
                @Override
                public Event getObject() {
                    return object;
                }
            };
        }
    }
}
