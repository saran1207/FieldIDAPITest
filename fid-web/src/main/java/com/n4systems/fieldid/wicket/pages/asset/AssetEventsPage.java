package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.asset.HeaderPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventMapPanel;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.model.Asset;
import com.n4systems.model.Event;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.List;

public class AssetEventsPage extends AssetPage{

    private EventListPanel eventPanel;
    private EventMapPanel mapPanel;
    private WebMarkupContainer filters;
    private FIDFeedbackPanel feedbackPanel;

    private boolean open = true;
    private boolean completed = true;
    private boolean closed = true;

    public AssetEventsPage(PageParameters params) {
        super(params);

        final Asset asset = assetModel.getObject();

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

        add(eventPanel = new EventListPanel("eventPanel", assetModel, getEventStates()));
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

    private void updateEventListPanel(AjaxRequestTarget target) {
        eventPanel.getDataProvider().setStates(getEventStates());
        eventPanel.getDefaultModel().detach();
        target.add(eventPanel);
        target.appendJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");

    }
    
    private List<Event.EventState> getEventStates() {
        List<Event.EventState> states = new ArrayList<Event.EventState>();
        if(open)
            states.add(Event.EventState.OPEN);
        if(completed)
            states.add(Event.EventState.COMPLETED);
        if(closed)
            states.add(Event.EventState.CLOSED);
        return states;
    }

    public static final String GOOGLE_MAPS_JS = "googleMaps";
    private static final String GOOGLE_MAP_API = "google-map-api";

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/events.css");
        response.renderCSSReference("style/newCss/asset/actions-menu.css");
        response.renderCSSReference("style/tipsy/tipsy.css");

        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnDomReadyJavaScript("subMenu.init();");

        response.renderJavaScriptReference("javascript/tipsy/jquery.tipsy.js");
        // CAVEAT : https://github.com/jaz303/tipsy/issues/19
        // after ajax call, tipsy tooltips will remain around so need to remove them explicitly.
        response.renderOnDomReadyJavaScript("$('.tipsy').remove(); $('.tipsy-tooltip').tipsy({gravity: 'nw', fade:true, delayIn:150})");

        //Needs to be included because the map panel is initially hidden.
        response.renderJavaScriptReference("https://maps.googleapis.com/maps/api/js?sensor=false", GOOGLE_MAP_API);
        response.renderJavaScriptReference("javascript/googleMaps.js", GOOGLE_MAPS_JS);

    }

    public FIDFeedbackPanel getFeedbackPanel() {
        return feedbackPanel;
    }
}


