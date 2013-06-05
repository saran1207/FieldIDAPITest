package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ch.lambdaj.Lambda.on;

public class AssetAttachmentsPanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    List<AssetAttachment> attachments = new ArrayList<AssetAttachment>();
    private WebMarkupContainer existingAttachmentsContainer;

    public AssetAttachmentsPanel(String id) {
        super(id);

        existingAttachmentsContainer = new WebMarkupContainer("existingAttachmentsContainer");
        existingAttachmentsContainer.setOutputMarkupPlaceholderTag(true);

        existingAttachmentsContainer.add(new ListView<AssetAttachment>("existingAttachments", attachments) {
            @Override
            protected void populateItem(final ListItem<AssetAttachment> item) {
                item.add(new TextArea<String>("comments", ProxyModel.of(item.getModel(), on(AssetAttachment.class).getComments())));
                item.add(new Label("fileName", ProxyModel.of(item.getModel(), on(AssetAttachment.class).getFileName())));
                item.add(new AjaxLink("removeLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        attachments.remove(item.getIndex());
                        target.add(existingAttachmentsContainer);
                    }
                });
            }
        });

        add(existingAttachmentsContainer);
        add(new UploadAttachmentForm("uploadAttachmentForm"));
    }


    class UploadAttachmentForm extends Form {
        public UploadAttachmentForm(String id) {
            super(id);

            final FileUploadField attachmentUpload = new FileUploadField("attachmentUpload");
            add(attachmentUpload);
            attachmentUpload.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    FileUpload fileUpload = attachmentUpload.getFileUpload();


                    File tempDir = PathHandler.getTempDir();
                    String fileName = UUID.randomUUID().toString();
                    File file = new File(tempDir, fileName);

                    try {
                        FileCopyUtils.copy(fileUpload.getInputStream(), new FileOutputStream(file));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    AssetAttachment attachment = new AssetAttachment();
                    attachment.setTempFileName(fileName);
                    attachment.setFileName(fileUpload.getClientFileName());

                    attachments.add(attachment);
                    target.add(existingAttachmentsContainer);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });
        }
    }

    public List<AssetAttachment> getAttachments() {
        return attachments;
    }

}
