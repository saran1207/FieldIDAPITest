package com.n4systems.fieldid.wicket.components.event;

import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.Event;
import com.n4systems.model.FileAttachment;
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

import java.net.URLEncoder;
import java.util.List;

public class EventAttachmentsPanel extends Panel {

    private static final Logger logger = Logger.getLogger(EventAttachmentsPanel.class);

    public EventAttachmentsPanel(String id, IModel<? extends Event> eventModel) {
        super(id);

        final Event event = eventModel.getObject();

        List<FileAttachment> attachments = eventModel.getObject().getAttachments();

        setVisible(!attachments.isEmpty());

        String downloadAllUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAllAttachedFiles.action?uniqueID=" + event.getId());

        add(new ExternalLink("downloadAllLink", downloadAllUrl));

        add(new ListView<FileAttachment>("attachments", attachments) {

            @Override
            protected void populateItem(ListItem<FileAttachment> item) {
                FileAttachment fileAttachment = item.getModelObject();

                String fileName;
                try {
                    fileName = URLEncoder.encode(fileAttachment.getFileName(), "UTF-8");
                } catch (Exception e) {
                    logger.warn("Could not convert to UTF-8", e);
                    fileName = fileAttachment.getFileName().replace(" ", "+");
                }

                String downloadUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAttachedFile.action?fileName=" + fileName + "&uniqueID=" + event.getId() + "&attachmentID=" + fileAttachment.getId());

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
                attachmentLink.add(new Label("attachmentName", fileAttachment.getFileName()));
                item.add(new Label("attachmentNote", fileAttachment.getComments()));

            }
        });
    }
}
