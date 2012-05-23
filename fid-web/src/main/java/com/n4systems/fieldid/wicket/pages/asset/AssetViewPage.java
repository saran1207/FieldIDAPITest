package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.asset.*;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AssetViewPage extends AssetPage {

    private LinkedAssetPanel linkedAssetPanel;

    public AssetViewPage(PageParameters params) {
        super(params);
           
        final Asset asset = assetModel.getObject();
        
        add(new Label("assetType", asset.getType().getName()));
        add(new Label("assetIdentifier", asset.getIdentifier()));
        Label assetStatus;
        if(asset.getAssetStatus() != null) {
            add(assetStatus = new Label("assetStatus", asset.getAssetStatus().getDisplayName()));
        } else {
            add(assetStatus = new Label("assetStatus"));
            assetStatus.setVisible(false);
        }

        BaseOrg owner = asset.getOwner();
        
        add(new Label("ownerInfo", owner.getDisplayName() + ", " + owner.getAddressInfo().getDisplay().replaceAll("\n", " ")));
        
        add(new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID=" + asset.getId(), new AttributeModifier("class", "mattButton")));

        add(new NonWicketLink("startEventLink", "quickEvent.action?assetId=" + asset.getId(), new AttributeModifier("class", "mattButton")));

        String imageUrl;
        if(asset.getImageName() == null) {
            imageUrl = "/file/downloadAssetTypeImage.action?uniqueID=" + asset.getType().getId();
        } else {
            imageUrl = "/file/downloadAssetImage.action?uniqueID=" + assetId;
        }
        add(new ExternalImage("assetImage", ContextAbsolutizer.toContextAbsoluteUrl(imageUrl)));

        if(asset.getGpsLocation() != null) {
            add(new GoogleMap("map").addLocation(asset.getGpsLocation().getLatitude().doubleValue(), asset.getGpsLocation().getLongitude().doubleValue()));
        } else {
            WebMarkupContainer map;
            add(map = new WebMarkupContainer("map"));
            map.setVisible(false);
        }

        add(new AssetAttributeDetailsPanel("assetAttributeDetailsPanel", assetModel));
        
        add(linkedAssetPanel = new LinkedAssetPanel("linkedAssetPanel", assetModel));
        linkedAssetPanel.setOutputMarkupId(true);

        add(new AssetDetailsPanel("assetDetailsPanel", assetModel));

        Boolean integrationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Integration);
        Boolean orderDetailsEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.OrderDetails);

        OrderDetailsPanel orderDetails;
        add(orderDetails = new OrderDetailsPanel("orderDetailsPanel", assetModel));
        orderDetails.setVisible(orderDetailsEnabled || integrationEnabled);

        if (hasUpcomingEvents(asset)) {
            add(new UpcomingEventsPanel("upcomingEventsPanel", assetModel));
        } else {
            add(new WebComponent("upcomingEventsPanel"));
        }

        if (hasLastEvent(asset)) {
            add(new LastEventPanel("lastEventsPanel", assetModel));
        } else {
            add(new WebComponent("lastEventsPanel"));
        }
        
        add(new AssetAttachmentsPanel("attachmentsPanel", assetModel));
        
        add(new WarningsAndInstructionsPanel("warningsAndInstructionsPanel", assetModel));

    }
    
    private boolean hasLastEvent(Asset asset) {
        return assetService.findLastEvents(asset, FieldIDSession.get().getSessionUser().getSecurityFilter()) != null;
    }
    
    private boolean hasUpcomingEvents(Asset asset) {
        return eventScheduleService.getAvailableSchedulesFor(asset).size() > 0;
    }

}
