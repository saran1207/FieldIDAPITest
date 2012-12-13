package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.asset.HeaderPanel;
import com.n4systems.fieldid.wicket.components.asset.summary.*;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.event.UpcomingEventsListModel;
import com.n4systems.model.Asset;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.io.File;

public class AssetSummaryPage extends AssetPage {

    private UpcomingEventsPanel upcomingEventsPanel;
    private Label upcomingEventsMessage;


    public AssetSummaryPage(Asset asset) {
        this(new PageParameters().add("uniqueID",asset.getId()).add("useContext","false"));
    }


    public AssetSummaryPage(PageParameters params) {
        super(params);
           
        final Asset asset = assetModel.getObject();

        add(new HeaderPanel("header", assetModel, true, useContext) {
            @Override
            protected void refreshContentPanel(AjaxRequestTarget target) {
                updateUpcomingEventsPanel(target);
            }
        });
        
        boolean imageExists;
        final String imageUrl;
        if(asset.getImageName() == null) {
            imageUrl = "/file/downloadAssetTypeImage.action?uniqueID=" + asset.getType().getId();
            if(asset.getType().getImageName() != null)
                imageExists = new File(PathHandler.getAssetTypeImageFile(asset.getType()), asset.getType().getImageName()).exists();
            else
                imageExists = false;
        } else {
            imageUrl = "/file/downloadAssetImage.action?uniqueID=" + assetId;
            imageExists = PathHandler.getAssetImageFile(asset).exists();
        }
        
        final ModalWindow modalWindow = new FIDModalWindow("assetImageModalWindow");
        modalWindow.setInitialHeight(500);
        modalWindow.setInitialWidth(540);
        add(modalWindow);

        ExternalImage assetImage;
        add(assetImage = new ExternalImage("assetImage", ContextAbsolutizer.toContextAbsoluteUrl(imageUrl)));
        assetImage.setVisible(imageExists);
        assetImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                modalWindow.setContent(new AssetImagePanel(modalWindow.getContentId(), Model.of(imageUrl)));
                modalWindow.show(target);
            }
        });


        if(asset.getGpsLocation() != null) {
            add(new GoogleMap("map").addLocation(asset.getGpsLocation().getLatitude().doubleValue(), asset.getGpsLocation().getLongitude().doubleValue()));
        } else {
            WebMarkupContainer map;
            add(map = new WebMarkupContainer("map"));
            map.setVisible(false);
        }

        add(new AssetAttributeDetailsPanel("assetAttributeDetailsPanel", assetModel));

        WebMarkupContainer linkedAssetPanel;
        if (assetService.parentAsset(asset) == null) {
            add(linkedAssetPanel = new LinkedAssetPanel("linkedAssetPanel", assetModel));
            linkedAssetPanel.setOutputMarkupId(true);
            linkedAssetPanel.setVisible(asset.getType().isLinkable());
        } else {
            add(linkedAssetPanel = new LinkedWithAssetPanel("linkedAssetPanel", assetModel));
        }

        add(new AssetDetailsPanel("assetDetailsPanel", assetModel));

        Boolean integrationEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.Integration);
        Boolean orderDetailsEnabled = FieldIDSession.get().getPrimaryOrg().hasExtendedFeature(ExtendedFeature.OrderDetails);

        OrderDetailsPanel orderDetails;
        add(orderDetails = new OrderDetailsPanel("orderDetailsPanel", assetModel));
        orderDetails.setVisible(orderDetailsEnabled || integrationEnabled);
    
        add(upcomingEventsPanel = new UpcomingEventsPanel("upcomingEventsPanel", new UpcomingEventsListModel().setAsset(asset), asset));
        upcomingEventsPanel.setOutputMarkupPlaceholderTag(true);
        add(upcomingEventsMessage = new Label("upcomingEventsMessage", new FIDLabelModel("label.empty_schedule_list").getObject()));
        upcomingEventsMessage.setOutputMarkupPlaceholderTag(true);
        if (hasUpcomingEvents(asset)) {
            upcomingEventsMessage.setVisible(false);
        } else {
            upcomingEventsPanel.setVisible(false);
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
        
        add(new Label("comments", new PropertyModel<Asset>(asset, "comments")));

    }

	@Override
	protected Label createTitleLabel(String labelId) {
		return new Label(labelId, new FIDLabelModel("title.asset_summary_page"));
	}

    private void updateUpcomingEventsPanel(AjaxRequestTarget target) {
        upcomingEventsPanel.getDefaultModel().detach();
        if(!upcomingEventsPanel.isVisible()) {
            upcomingEventsPanel.setVisible(true);
            upcomingEventsMessage.setVisible(false);
        }
        target.add(upcomingEventsPanel);
        target.add(upcomingEventsMessage);
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
        response.renderCSSReference("style/newCss/asset/actions-menu.css");

        response.renderJavaScriptReference("javascript/subMenu.js");
        response.renderOnDomReadyJavaScript("subMenu.init();");

    }

}
