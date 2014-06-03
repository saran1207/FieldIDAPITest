package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.Asset;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.asset.AssetAttachment;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class AssetAttachmentsPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AssetAttachmentsPanel.class);

    @SpringBean
    protected AssetService assetService;

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private UserService userService;

    public AssetAttachmentsPanel(String id, IModel<Asset> model) {
        super(id, model);

        final Asset asset = model.getObject();
        List<AssetAttachment> assetAttachments = assetService.findAssetAttachments(asset);
        
        add(new ListView<AssetAttachment>("assetAttachments", assetAttachments) {
            @Override
            protected void populateItem(ListItem<AssetAttachment> item) {
                AssetAttachment attachment = item.getModelObject();

                String downloadUrl;
                if(attachment.isRemote()){
                    downloadUrl = s3Service.getAssetAttachmentUrl(attachment).toString();
                }
                else {
                    String fileName;
                    try {
                        fileName = URLEncoder.encode(attachment.getFileName(), "UTF-8");
                    } catch (Exception e) {
                        logger.warn("Could not convert to UTF-8", e);
                        fileName = attachment.getFileName().replace(" ", "+");
                    }
                    downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetAttachedFile.action?fileName="+ fileName + "&uniqueID="+ asset.getId() + "&attachmentID=" + attachment.getId());
                }

                WebComponent image;
                if(attachment.isImage()) {
                    item.add(image = new ExternalImage("attachmentImage", downloadUrl));
                    image.add(new AttributeModifier("class", "attachmentImage"));
                } else {
                    item.add(image = new Image("attachmentImage", new ContextRelativeResource("images/file-icon.png")));
                    image.add(new AttributeModifier("class", "attachmentIcon"));
                }
                ExternalLink attachmentLink;
                item.add(attachmentLink = new ExternalLink("attachmentLink", downloadUrl.toString()));

                String assetAttachmentFilePath = attachment.getFileName();
                String assetAttachmentFileName = assetAttachmentFilePath.substring(assetAttachmentFilePath.lastIndexOf('/') + 1);

                attachmentLink.add(new Label("attachmentName", assetAttachmentFileName));
                item.add(new Label("attachmentNote", attachment.getNote().getComments()));
            }
        });
        
        List<FileAttachment> typeAttachments = asset.getType().getAttachments();
        
        add(new ListView<FileAttachment>("assetTypeAttachments", typeAttachments) {
            @Override
            protected void populateItem(ListItem<FileAttachment> item) {
                FileAttachment attachment = item.getModelObject();

                String downloadUrl;
                if(attachment.isRemote()){
                    downloadUrl = s3Service.getFileAttachmentUrl(attachment).toString();
                }
                else {
                    String fileName;
                    try {
                        fileName = URLEncoder.encode(attachment.getFileName(), "UTF-8");
                    } catch (Exception e) {
                        logger.warn("Could not convert to UTF-8", e);
                        fileName = attachment.getFileName().replace(" ", "+");
                    }
                    downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetTypeAttachedFile.action?fileName=" + fileName + "&uniqueID=" + asset.getType().getId() + "&attachmentID=" + attachment.getId());
                }

                WebComponent image;
                if (attachment.isImage()) {
                    item.add(image = new ExternalImage("attachmentImage", downloadUrl));
                    image.add(new AttributeModifier("class", "attachmentImage"));
                } else {
                    item.add(image = new Image("attachmentImage", new ContextRelativeResource("images/file-icon.png")));
                    image.add(new AttributeModifier("class", "attachmentIcon"));
                }

                String fileAttachmentFilePath = attachment.getFileName();
                String fileAttachmentFileName = fileAttachmentFilePath.substring(fileAttachmentFilePath.lastIndexOf('/') + 1);

                ExternalLink attachmentLink;
                item.add(attachmentLink = new ExternalLink("attachmentLink", downloadUrl));
                attachmentLink.add(new Label("attachmentName", fileAttachmentFileName));
                item.add(new Label("attachmentNote", attachment.getComments()));
            }
        });
    }
}
