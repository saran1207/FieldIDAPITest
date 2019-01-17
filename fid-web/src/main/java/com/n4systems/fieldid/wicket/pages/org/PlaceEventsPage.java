package com.n4systems.fieldid.wicket.pages.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.org.PlaceService;
import com.n4systems.fieldid.wicket.behavior.TipsyBehavior;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventMapPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.components.org.events.FilterPanel;
import com.n4systems.fieldid.wicket.components.org.events.table.ActionsColumn;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.navigation.NavigationItem;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
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

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class PlaceEventsPage extends PlacePage {

    public static final String OPEN_PARAM = "open";
    public static final List<WorkflowState> ALL_WORKFLOW_STATES = Lists.newArrayList(WorkflowState.OPEN, WorkflowState.COMPLETED, WorkflowState.CLOSED);
    private enum Display { LIST, MAP };

    @SpringBean
    private PlaceService placeService;

    private EventListPanel eventPanel;
    private EventMapPanel mapPanel;
    private FilterPanel filterPanel;
    private WebMarkupContainer blankSlate;
    private PlaceEventDataProvider dataProvider;
    private FIDFeedbackPanel feedbackPanel;
    private Display display = Display.LIST;

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

    @Override
    protected void refreshContent(AjaxRequestTarget target) {
        setVisibilty();
        target.add(eventPanel, filterPanel, mapPanel, blankSlate);
    }

    private final void init() {

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(filterPanel = new FilterPanel("filterPanel", orgModel, workflowStates) {
            @Override
            public void onFilterSelectionChanged(AjaxRequestTarget target) {
                workflowStates = getWorkflowStates();
                PlaceEventsPage.this.dataProvider.setWorkflowStates(getWorkflowStates());
                target.add(eventPanel);
            }
        });
        filterPanel.setOutputMarkupPlaceholderTag(true);

        dataProvider = new PlaceEventDataProvider("completedDate", SortOrder.DESCENDING, workflowStates);
        add(eventPanel = new EventListPanel("eventPanel", dataProvider) {
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
        mapPanel.setOutputMarkupPlaceholderTag(true);
        mapPanel.setVisible(false);

        add(new AjaxLink<Void>("listLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                eventPanel.setVisible(true);
                mapPanel.setVisible(false);
                filterPanel.setVisible(true);
                display = Display.LIST;
                target.add(eventPanel, mapPanel, filterPanel);
            }
        }.add(new TipsyBehavior(new FIDLabelModel("label.list"), TipsyBehavior.Gravity.NW)));

        add(new AjaxLink<Void>("mapLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                eventPanel.setVisible(false);
                mapPanel.setVisible(true);
                filterPanel.setVisible(false);
                display = Display.MAP;
                target.add(eventPanel, mapPanel, filterPanel);
            }
        }.add(new TipsyBehavior(new FIDLabelModel("label.map"), TipsyBehavior.Gravity.NW)));

        add(blankSlate = new WebMarkupContainer("blankSlate"));
        blankSlate.setOutputMarkupPlaceholderTag(true);
        blankSlate.add(new Label("blankSlateTitle", new PropertyModel(orgModel, "name")));
        blankSlate.add(new Label("orgNameLabel", new PropertyModel(orgModel, "name")));
        setVisibilty();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        //Needs to be included because the map panel is initially hidden.
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false&key=AIzaSyBcMtP_Yxr_RrU8TnYeFrGqJylMmDlFlHI", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);
    }

    @Override
    public String getMainCss() {
        return "place-event";
    }

    @Override
    protected List<NavigationItem> createBreadCrumbs(BaseOrg org) {
        List<NavigationItem> navItems = super.createBreadCrumbs(org);
        navItems.add(aNavItem().label(new FIDLabelModel("label.events")).page(getClass()).params(PageParametersBuilder.id(org.getId())).build());
        return navItems;
    }

    public FIDFeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }

    private void setVisibilty() {
        boolean hasEvents = placeService.countEventsFor(orgModel.getObject(), null) > 0;
        if(display.equals(Display.LIST)) {
            eventPanel.setVisible(hasEvents);
            filterPanel.setVisible(hasEvents);
            mapPanel.setVisible(false);
        } else {
            eventPanel.setVisible(false);
            filterPanel.setVisible(false);
            mapPanel.setVisible(hasEvents);
        }
        blankSlate.setVisible(!hasEvents);
    }

    private class PlaceEventDataProvider extends FieldIDDataProvider<Event> {

        private List<WorkflowState> states;

        public PlaceEventDataProvider(String order, SortOrder sortOrder, List<WorkflowState> workflowStates) {
            setSort(order, sortOrder);
            this.states = workflowStates;
        }

        @Override
        public Iterator<? extends Event> iterator(int first, int count) {
            List<? extends Event> eventsList = placeService.getEventsFor(orgModel.getObject(), getSort().getProperty(), getSort().isAscending(), states, first,count);
            return eventsList.iterator();
        }

        @Override
        public int size() {
            return placeService.countEventsFor(orgModel.getObject(), states);
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

        public void setWorkflowStates(List<WorkflowState> workflowStates) {
            this.states = workflowStates;
        }
    }
}
