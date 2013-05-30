package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.FileAttachment;
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

import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class AssetAttachmentsPanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    List<FileAttachment> attachments = new ArrayList<FileAttachment>();
    private WebMarkupContainer existingAttachmentsContainer;

    public AssetAttachmentsPanel(String id) {
        super(id);

        existingAttachmentsContainer = new WebMarkupContainer("existingAttachmentsContainer");
        existingAttachmentsContainer.setOutputMarkupPlaceholderTag(true);

        existingAttachmentsContainer.add(new ListView<FileAttachment>("existingAttachments", attachments) {
            @Override
            protected void populateItem(final ListItem<FileAttachment> item) {
                item.add(new TextArea<String>("comments", ProxyModel.of(item.getModel(), on(FileAttachment.class).getComments())));
                item.add(new Label("fileName", ProxyModel.of(item.getModel(), on(FileAttachment.class).getFileName())));
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

                    String tempFileName = s3Service.uploadTempAssetFile(fileUpload.getContentType(), fileUpload.getBytes());

                    FileAttachment attachment = new FileAttachment();
                    attachment.setTempFileName(tempFileName);
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

}
