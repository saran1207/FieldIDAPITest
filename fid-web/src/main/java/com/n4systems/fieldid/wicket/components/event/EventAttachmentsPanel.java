package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.*;
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

import java.net.URLEncoder;
import java.util.List;

public class EventAttachmentsPanel extends Panel {

    private static final Logger logger = Logger.getLogger(EventAttachmentsPanel.class);

    @SpringBean
    private S3Service s3Service;
    private String downloadAction;
    private String downloadAllAction;

    public EventAttachmentsPanel(String id, IModel<? extends AbstractEvent> eventModel) {
        super(id);

        final AbstractEvent event = eventModel.getObject();

        if (event instanceof Event) {
            downloadAction = "downloadAttachedFile";
        } else {
            downloadAction = "downloadSubAttachedFile";
        }

        List<FileAttachment> attachments = eventModel.getObject().getAttachments();

        setVisible(!attachments.isEmpty());

        if (event instanceof PlaceEvent) {
            downloadAllAction = "downloadAllPlaceAttachedFiles";
        } else if (event instanceof SubEvent){
            downloadAllAction = "downloadAllSubAttachedFiles";
        } else {
            downloadAllAction = "downloadAllAttachedFiles";
        }

        String downloadAllUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/" + downloadAllAction + ".action?uniqueID=" + event.getId());

        add(new ExternalLink("downloadAllLink", downloadAllUrl));

        add(new ListView<FileAttachment>("attachments", attachments) {

            @Override
            protected void populateItem(ListItem<FileAttachment> item) {
                FileAttachment fileAttachment = item.getModelObject();

                String downloadUrl;
                if(fileAttachment.isRemote()){
                    downloadUrl = s3Service.getFileAttachmentUrl(fileAttachment).toString();
                }
                else {
                    String fileName;
                    try {
                        fileName = URLEncoder.encode(fileAttachment.getFileName(), "UTF-8");
                    } catch (Exception e) {
                        logger.warn("Could not convert to UTF-8", e);
                        fileName = fileAttachment.getFileName().replace(" ", "+");
                    }
                    downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/" + downloadAction + ".action?fileName=" + fileName + "&uniqueID=" + event.getId() + "&attachmentID=" + fileAttachment.getId());
                }

                WebComponent image;
                if(fileAttachment.isImage()) {
                    item.add(image = new ExternalImage("attachmentImage", downloadUrl));
                    image.add(new AttributeModifier("class", "attachment-image"));
                } else {
                    item.add(image = new Image("attachmentImage", new ContextRelativeResource("images/file-icon.png")));
                    image.add(new AttributeModifier("class", "attachment-icon"));
                }

                ExternalLink attachmentLink;
                item.add(attachmentLink = new ExternalLink("attachmentLink", downloadUrl));
                String attachmentFilename = fileAttachment.getFileName().substring(fileAttachment.getFileName().lastIndexOf('/') + 1);
                attachmentLink.add(new Label("attachmentName", attachmentFilename));
                item.add(new Label("attachmentNote", fileAttachment.getComments()));

            }
        });
    }
}
