package com.n4systems.fieldid.wicket.components.asset.summary;

import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.util.ZipFileUtil;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.model.user.User;
import com.n4systems.reporting.PathHandler;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipOutputStream;

public class AssetAttachmentsPanel extends Panel {

    private static final Logger logger = Logger.getLogger(AssetAttachmentsPanel.class);

    @SpringBean
    protected AssetService assetService;

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

                String fileName;
                try {
                    fileName = URLEncoder.encode(attachment.getFileName(), "UTF-8");
                } catch (Exception e) {
                    logger.warn("Could not conver to UTF-8", e);
                    fileName = attachment.getFileName().replace(" ", "+");
                }
                
                String downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetAttachedFile.action?fileName="+ fileName + "&uniqueID="+ asset.getId() + "&attachmentID=" + attachment.getId());

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
        DownloadLink downloadAllLink;
        String fileName = asset.getIdentifier() + "-Attachments.zip";
        add(downloadAllLink = new DownloadLink("downloadAllLink", getAllFiles(assetAttachments, typeAttachments, fileName, asset.getType()), fileName));
        downloadAllLink.setDeleteAfterDownload(true);
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

    private User getCurrentUser() {
        return userService.getUser(FieldIDSession.get().getSessionUser().getId());
    }

}
