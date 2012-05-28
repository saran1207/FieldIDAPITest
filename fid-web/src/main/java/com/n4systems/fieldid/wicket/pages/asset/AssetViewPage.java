package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.NonWicketLink;
import com.n4systems.fieldid.wicket.components.asset.*;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.location.Location;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AssetViewPage extends AssetPage {

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

        add(new Label("ownerInfo", getOwnerLabel(owner, asset.getAdvancedLocation())));
        
        add(new NonWicketLink("editAssetLink", "assetEdit.action?uniqueID=" + asset.getId(), new AttributeModifier("class", "mattButton")));

        add(new NonWicketLink("startEventLink", "quickEvent.action?assetId=" + asset.getId(), new AttributeModifier("class", "mattButton blueButton")));

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

        WebMarkupContainer linkedAssetPanel;
        if(assetService.parentAsset(asset) == null) {
            add(linkedAssetPanel = new LinkedAssetPanel("linkedAssetPanel", assetModel));
            linkedAssetPanel.setOutputMarkupId(true);
        }else {
            add(linkedAssetPanel = new LinkedWithAssetPanel("linkedAssetPanel", assetModel));
        }

        add(new AssetDetailsPanel("assetDetailsPanel", assetModel));

        Boolean integrationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Integration);
        Boolean orderDetailsEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.OrderDetails);

        OrderDetailsPanel orderDetails;
        add(orderDetails = new OrderDetailsPanel("orderDetailsPanel", assetModel));
        orderDetails.setVisible(orderDetailsEnabled || integrationEnabled);

        WebMarkupContainer buttonContainer = new WebMarkupContainer("buttonContainer");

        buttonContainer.add(new Link<Void>("summaryLink") {
            @Override
            public void onClick() {
            }
        });
        
        buttonContainer.add(new NonWicketLink("schedulesLink", "eventScheduleList.action?assetId=" + asset.getId() + "&useContext=false", new AttributeModifier("class", "mattButtonMiddle")));
        
        NonWicketLink traceabilityLink;
        buttonContainer.add(traceabilityLink = new NonWicketLink("traceabilityLink", "assetTraceability.action?uniqueID=" + asset.getId() + "&useContext=false", new AttributeModifier("class", "mattButtonMiddle")));
        traceabilityLink.setVisible(assetService.hasLinkedAssets(asset) || isInVendorContext());

        buttonContainer.add(new NonWicketLink("eventHistoryLink", "assetEvents.action?uniqueID=" + asset.getId() + "&useContext=false", new AttributeModifier("class", "mattButtonRight")));

        if (traceabilityLink.isVisible()) {
           buttonContainer.add(new AttributeModifier("class", "four_button"));
        } else {
            buttonContainer.add(new AttributeModifier("class", "three_button"));
        }

        add(buttonContainer);
        
        if (hasUpcomingEvents(asset)) {
            add(new UpcomingEventsPanel("upcomingEventsPanel", assetModel));
        } else {
            add(new Label("upcomingEventsPanel", new FIDLabelModel("label.empty_schedule_list").getObject()));
        }

        if (hasLastEvent(asset)) {
            add(new LastEventPanel("lastEventsPanel", assetModel));
        } else {
            add(new Label("lastEventsPanel", new FIDLabelModel("label.emptyeventlist").getObject()));
        }
        if(hasAttachments(asset)) {
            add(new AssetAttachmentsPanel("attachmentsPanel", assetModel));
        } else {
            add(new Label("attachmentsPanel", new FIDLabelModel("label.no_attachments").getObject()));
        }

        add(new WarningsAndInstructionsPanel("warningsAndInstructionsPanel", assetModel));

    }

    private String getOwnerLabel(BaseOrg owner, Location advancedLocation) {
        StringBuffer buff = new StringBuffer();
        if(owner.isDivision()) {
            buff.append(owner.getCustomerOrg().getName()).append(" (").append(owner.getPrimaryOrg().getName()).append("), ").append(owner.getDivisionOrg().getName());
        } else if(owner.isCustomer()) {
            buff.append(owner.getCustomerOrg().getName()).append(" (").append(owner.getPrimaryOrg().getName()).append(")");
        } else {
            buff.append(owner.getPrimaryOrg().getName());
        }
        
        if(advancedLocation != null && !advancedLocation.getFullName().isEmpty()) {
                buff.append(", ").append(advancedLocation.getFullName());
        }
        return buff.toString();  
    }


    private boolean hasLastEvent(Asset asset) {
        return assetService.findLastEvents(asset, FieldIDSession.get().getSessionUser().getSecurityFilter()) != null;
    }
    
    private boolean hasUpcomingEvents(Asset asset) {
        return !eventScheduleService.getAvailableSchedulesFor(asset).isEmpty();
    }

    private boolean hasAttachments(Asset asset) {
        return !assetService.findAssetAttachments(asset).isEmpty() || !asset.getType().getAttachments().isEmpty();
    }

}
