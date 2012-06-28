package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.asset.*;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AssetViewPage extends AssetPage {

    public AssetViewPage(PageParameters params) {
        super(params);
           
        final Asset asset = assetModel.getObject();

        add(new HeaderPanel("header", assetModel, true));
        
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
            linkedAssetPanel.setVisible(asset.getType().isLinkable());
        }else {
            add(linkedAssetPanel = new LinkedWithAssetPanel("linkedAssetPanel", assetModel));
        }

        add(new AssetDetailsPanel("assetDetailsPanel", assetModel));

        Boolean integrationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Integration);
        Boolean orderDetailsEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.OrderDetails);

        OrderDetailsPanel orderDetails;
        add(orderDetails = new OrderDetailsPanel("orderDetailsPanel", assetModel));
        orderDetails.setVisible(orderDetailsEnabled || integrationEnabled);

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

    private boolean hasLastEvent(Asset asset) {
        return assetService.findLastEvents(asset, FieldIDSession.get().getSessionUser().getSecurityFilter()) != null;
    }
    
    private boolean hasUpcomingEvents(Asset asset) {
        return !eventScheduleService.getAvailableSchedulesFor(asset).isEmpty();
    }

    private boolean hasAttachments(Asset asset) {
        return !assetService.findAssetAttachments(asset).isEmpty() || !asset.getType().getAttachments().isEmpty();
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/asset/asset.css");
    }

}
