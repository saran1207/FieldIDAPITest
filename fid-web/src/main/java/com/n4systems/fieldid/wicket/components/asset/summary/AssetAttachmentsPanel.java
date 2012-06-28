package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.Asset;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.asset.AssetAttachment;
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

import java.util.List;

public class AssetAttachmentsPanel extends Panel {

    @SpringBean
    protected AssetService assetService;

    public AssetAttachmentsPanel(String id, IModel<Asset> model) {
        super(id, model);

        final Asset asset = model.getObject();
        List<AssetAttachment> assetAttachments = assetService.findAssetAttachments(asset);
        
        add(new ListView<AssetAttachment>("assetAttachments", assetAttachments) {
            @Override
            protected void populateItem(ListItem<AssetAttachment> item) {
                AssetAttachment attachment = item.getModelObject();
                
                String downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetAttachedFile.action?fileName="+ attachment.getFileName().replace(" ", "+") + "&uniqueID="+ asset.getId() + "&attachmentID=" + attachment.getId());

                WebComponent image;
                if(attachment.isImage()) {
                    item.add(image = new ExternalImage("attachmentImage", downloadUrl));
                    image.add(new AttributeModifier("class", "attachmentImage"));
                } else {
                    item.add(image = new Image("attachmentImage", new ContextRelativeResource("images/file-icon.png")));
                    image.add(new AttributeModifier("class", "attachmentIcon"));
                }
                ExternalLink attachmentLink;
                item.add(attachmentLink = new ExternalLink("attachmentLink", downloadUrl));
                attachmentLink.add(new Label("attachmentName", attachment.getFileName()));
                item.add(new Label("attachmentNote", attachment.getNote().getComments()));
            }
        });
        
        List<FileAttachment> typeAttachments = asset.getType().getAttachments();
        
        add(new ListView<FileAttachment>("assetTypeAttachments", typeAttachments) {
            @Override
            protected void populateItem(ListItem<FileAttachment> item) {
                FileAttachment attachment = item.getModelObject();

                String downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetTypeAttachedFile.action?fileName="+ attachment.getFileName().replace(" ", "+") + "&uniqueID="+ asset.getType().getId() + "&attachmentID=" + attachment.getId());

                WebComponent image;
                if(attachment.isImage()) {
                    item.add(image = new ExternalImage("attachmentImage", downloadUrl));
                    image.add(new AttributeModifier("class", "attachmentImage"));
                } else {
                    item.add(image = new Image("attachmentImage", new ContextRelativeResource("images/file-icon.png")));
                    image.add(new AttributeModifier("class", "attachmentIcon"));
                }
                ExternalLink attachmentLink;
                item.add(attachmentLink = new ExternalLink("attachmentLink", downloadUrl));
                attachmentLink.add(new Label("attachmentName", attachment.getFileName()));
                item.add(new Label("attachmentNote", attachment.getComments()));
            }
        });
    }

}
