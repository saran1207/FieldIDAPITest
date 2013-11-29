package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.service.mixpanel.MixpanelService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.asset.HeaderPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventMapPanel;
import com.n4systems.fieldid.wicket.components.asset.events.table.ActionsColumn;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.data.EventByNetworkIdProvider;
import com.n4systems.fieldid.wicket.data.FieldIDDataProvider;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import com.n4systems.model.ThingEvent;
import com.n4systems.model.WorkflowState;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class AssetEventsPage extends AssetPage{

    private EventListPanel eventPanel;
    private EventMapPanel mapPanel;
    private WebMarkupContainer filters;
    private FIDFeedbackPanel feedbackPanel;

    @SpringBean
    private MixpanelService mixpanelService;

    private boolean open = true;
    private boolean completed = true;
    private boolean closed = true;

    public AssetEventsPage(PageParameters params) {
        super(params);

        final Asset asset = assetModel.getObject();
        mixpanelService.sendEvent(MixpanelService.VIEWED_ASSET_EVENTS_LIST);

        add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        add(new HeaderPanel("header", assetModel, false, useContext) {
            @Override
            protected void refreshContentPanel(AjaxRequestTarget target) {
                updateEventListPanel(target);
            }
        });
        
        AjaxLink listLink;
        AjaxLink mapLink;
        
        add(listLink = new AjaxLink<Void>("listLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                eventPanel.setVisible(true);
                mapPanel.setVisible(false);
                filters.setVisible(true);
                target.add(eventPanel);
                target.add(mapPanel);
                target.add(filters);
            }
        });

        add(mapLink = new AjaxLink<Void>("mapLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                mixpanelService.sendEvent(MixpanelService.VIEWED_ASSET_EVENTS_MAP);
                eventPanel.setVisible(false);
                mapPanel.setVisible(true);
                filters.setVisible(false);
                target.add(eventPanel);
                target.add(mapPanel);
                target.add(filters);
            }
        });
        
        if (FieldIDSession.get().getTenant().getSettings().isGpsCapture()) {
            listLink.add(new AttributeAppender("class", "mattButtonLeft").setSeparator(" "));
            mapLink.add(new AttributeAppender("class", "mattButtonRight").setSeparator(" "));
        } else {
            listLink.add(new AttributeAppender("class", "mattButton").setSeparator(" "));
            mapLink.setVisible(false);
        }

        FieldIDDataProvider<Event> dataProvider = new EventByNetworkIdProvider(asset.getNetworkId(), "completedDate", SortOrder.DESCENDING, getWorkflowStates());

        add(eventPanel = new EventListPanel("eventPanel", getWorkflowStates(), dataProvider) {
            @Override
            protected void addCustomColumns(List<IColumn<? extends Event>> columns) {
                columns.add(new PropertyColumn<ThingEvent>(new FIDLabelModel("label.assetstatus"), "assetStatus", "assetStatus.displayName"));
            }

            @Override
            protected void addActionColumn(List<IColumn<? extends Event>> columns) {
                columns.add(new ActionsColumn("id", this));
            }
        });
        add(mapPanel = new EventMapPanel("mapPanel", assetModel));
        eventPanel.setOutputMarkupPlaceholderTag(true);
        mapPanel.setOutputMarkupPlaceholderTag(true);
        mapPanel.setVisible(false);

        filters = new WebMarkupContainer("filters");
        filters.setOutputMarkupPlaceholderTag(true);
        filters.add(new AjaxCheckBox("open", new PropertyModel<Boolean>(this, "open")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                open = getModel().getObject();
                updateEventListPanel(target);
            }
        });
        filters.add(new AjaxCheckBox("completed", new PropertyModel<Boolean>(this, "completed")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                completed = getModel().getObject();
                updateEventListPanel(target);
            }
        });
        filters.add(new AjaxCheckBox("closed", new PropertyModel<Boolean>(this, "closed")) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                closed = getModel().getObject();
                updateEventListPanel(target);
            }
        });
        add(filters);

    }

	@Override
	protected Label createTitleLabel(String labelId) {
		return new Label(labelId, new FIDLabelModel("title.asset_events_page"));
	}

    private void updateEventListPanel(AjaxRequestTarget target) {
        ((EventByNetworkIdProvider) eventPanel.getDataProvider()).setStates(getWorkflowStates());
        eventPanel.getDefaultModel().detach();
        target.add(eventPanel);
        target.appendJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");

    }
    
    private List<WorkflowState> getWorkflowStates() {
        List<WorkflowState> states = new ArrayList<WorkflowState>();

        if(open)
            states.add(WorkflowState.OPEN);
        if(completed)
            states.add(WorkflowState.COMPLETED);
        if(closed)
            states.add(WorkflowState.CLOSED);

        if (states.size() == 0) {
            states.add(WorkflowState.NONE);
        }

        return states;
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/events.css");
        response.renderCSSReference("style/newCss/asset/actions-menu.css");

        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnDomReadyJavaScript("subMenu.init();");

        //Needs to be included because the map panel is initially hidden.
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GoogleMap.GOOGLE_MAP_API_ID);
        response.renderJavaScriptReference("javascript/googleMaps.js", GoogleMap.GOOGLE_MAPS_JS_ID);

    }

    public FIDFeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }


    @Override
    protected boolean forceDefaultLanguage() {
        return false;
    }
}


