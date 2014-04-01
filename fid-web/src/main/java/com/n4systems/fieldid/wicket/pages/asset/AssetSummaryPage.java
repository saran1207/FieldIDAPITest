package com.n4systems.fieldid.wicket.pages.asset;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.mixpanel.MixpanelService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.BigDecimalFmtLabel;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.components.GpsFmtLabel;
import com.n4systems.fieldid.wicket.components.asset.HeaderPanel;
import com.n4systems.fieldid.wicket.components.asset.summary.*;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.model.LocalizeModel;
import com.n4systems.fieldid.wicket.model.event.UpcomingEventsListModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.fieldid.wicket.util.ZipFileUtil;
import com.n4systems.model.*;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.IOUtils;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.zip.ZipOutputStream;

import static ch.lambdaj.Lambda.on;

public class AssetSummaryPage extends AssetPage {

    private UpcomingEventsPanel upcomingEventsPanel;
    private Label upcomingEventsMessage;

    @SpringBean
    private S3Service s3Service;

    public AssetSummaryPage(Asset asset) {
        this(new PageParameters().add("uniqueID",asset.getId()).add("useContext","false"));
    }

    public AssetSummaryPage(int id) {
        this(new PageParameters().add("uniqueID",id).add("useContext","false"));
    }

    public AssetSummaryPage(PageParameters params) {
        super(params);

        mixpanelService.sendEvent(MixpanelService.VIEWED_ASSET);
           
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
            imageUrl = ContextAbsolutizer.toContextAbsoluteUrl("/file/downloadAssetTypeImage.action?uniqueID=" + asset.getType().getId());
            if(asset.getType().getImageName() != null)
                imageExists = new File(PathHandler.getAssetTypeImageFile(asset.getType()), asset.getType().getImageName()).exists();
            else
                imageExists = false;
        } else {
            imageExists = s3Service.assetProfileImageExists(asset.getId(), asset.getImageName());
            imageUrl = s3Service.getAssetProfileImageMediumURL(asset.getId(), asset.getImageName()).toString();
        }
        
        final ModalWindow modalWindow = new FIDModalWindow("assetImageModalWindow");
        modalWindow.setInitialHeight(500);
        modalWindow.setInitialWidth(540);
        add(modalWindow);

        ExternalImage assetImage;
        add(assetImage = new ExternalImage("assetImage", imageUrl));
        assetImage.setVisible(imageExists);
        assetImage.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                modalWindow.setContent(new AssetImagePanel(modalWindow.getContentId(), Model.of(imageUrl)));
                modalWindow.show(target);
            }
        });

        GpsFmtLabel latitude = new GpsFmtLabel("latitude", ProxyModel.of(asset, on(Asset.class).getGpsLocation().getLatitude()));
        GpsFmtLabel longitude = new GpsFmtLabel("longitude", ProxyModel.of(asset, on(Asset.class).getGpsLocation().getLongitude()));

        add(latitude);
        add(longitude);


        if(asset.getGpsLocation() != null) {
            add(new GoogleMap("map",asset));


        } else {
            WebMarkupContainer map;
            add(map = new WebMarkupContainer("map"));
            map.setVisible(false);
            latitude.setVisible(false);
            longitude.setVisible(false);
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
    
        add(upcomingEventsPanel = new UpcomingEventsPanel("upcomingEventsPanel", new LocalizeModel<List<ThingEvent>>(new UpcomingEventsListModel().setAsset(asset)), asset));
        upcomingEventsPanel.setOutputMarkupPlaceholderTag(true);
        add(upcomingEventsMessage = new Label("upcomingEventsMessage", new FIDLabelModel("label.empty_schedule_list").getObject()));
        upcomingEventsMessage.setOutputMarkupPlaceholderTag(true);
        if (hasUpcomingEventsOrProcedures(asset)) {
            upcomingEventsMessage.setVisible(false);
        } else {
            upcomingEventsPanel.setVisible(false);
        }

        if (hasLastEvent(asset)) {
            add(new LastEventPanel("lastEventsPanel", assetModel));
        } else {
            add(new Label("lastEventsPanel", new FIDLabelModel("label.emptyeventlist").getObject()));
        }

        DownloadLink downloadAllLink;
        String fileName = asset.getIdentifier() + "-Attachments.zip";

        List<AssetAttachment> assetAttachments = assetService.findAssetAttachments(asset);
        List<FileAttachment> typeAttachments = asset.getType().getAttachments();

        add(downloadAllLink = new DownloadLink("downloadAllLink", getAllFiles(assetAttachments, typeAttachments, fileName, asset.getType()), fileName));
        downloadAllLink.setDeleteAfterDownload(true);

        if(hasAttachments(asset)) {
            add(new AssetAttachmentsPanel("attachmentsPanel", assetModel));
        } else {
            downloadAllLink.setVisible(false);
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
    
    private boolean hasUpcomingEventsOrProcedures(Asset asset) {
        return !eventScheduleService.getAvailableSchedulesFor(asset).isEmpty() || procedureService.hasActiveProcedure(asset);
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

    private LoadableDetachableModel<File> getAllFiles(final List<AssetAttachment> assetAttachments, final List<FileAttachment> typeAttachments, final String filename, final AssetType assetType) {
        return new LoadableDetachableModel<File>() {
            @Override
            protected File load() {

                try {
                    ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(getFile(filename)));
                    for (AssetAttachment assetAttachment: assetAttachments) {
                        ZipFileUtil.addToZipFile(PathHandler.getAssetAttachmentFile(assetAttachment), zipOut);
                    }
                    for (FileAttachment fileAttachment: typeAttachments) {
                        ZipFileUtil.addToZipFile(PathHandler.getAssetTypeAttachmentFile(fileAttachment, assetType.getId()), zipOut);
                    }

                    IOUtils.closeQuietly(zipOut);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                return getFile(filename);
            }
        };
    }

    private File getFile(String filename) {
        return new File(getCurrentUser().getPrivateDir(), filename);
    }

    @Override
    protected boolean forceDefaultLanguage() {
        return false;
    }
}
