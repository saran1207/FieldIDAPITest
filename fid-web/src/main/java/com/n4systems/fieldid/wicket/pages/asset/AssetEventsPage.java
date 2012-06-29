package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.components.asset.HeaderPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventListPanel;
import com.n4systems.fieldid.wicket.components.asset.events.EventMapPanel;
import com.n4systems.model.Asset;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AssetEventsPage extends AssetPage{

    public Boolean isList;

    private Panel eventPanel;
    private Panel mapPanel;

    public AssetEventsPage(PageParameters params) {
        super(params);

        final Asset asset = assetModel.getObject();

        add(new HeaderPanel("header", assetModel, false, useContext));

        add(new AjaxLink<Void>("listLink") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                eventPanel.setVisible(true);
                mapPanel.setVisible(false);
                target.add(eventPanel);
                target.add(mapPanel);
            }
        });

        add(new AjaxLink<Void>("mapLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                eventPanel.setVisible(false);
                mapPanel.setVisible(true);

                target.add(eventPanel);
                target.add(mapPanel);
            }
        });

        add(eventPanel = new EventListPanel("eventPanel", assetModel));
        add(mapPanel = new EventMapPanel("mapPanel", assetModel));
        eventPanel.setOutputMarkupPlaceholderTag(true);
        mapPanel.setOutputMarkupPlaceholderTag(true);
        mapPanel.setVisible(false);
    }


    public static final String GOOGLE_MAPS_JS = "googleMaps";
    private static final String GOOGLE_MAP_API = "google-map-api";

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/events.css");
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
}


