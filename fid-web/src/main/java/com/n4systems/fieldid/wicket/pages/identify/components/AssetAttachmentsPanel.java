package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ConfigEntry;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ch.lambdaj.Lambda.as;
import static ch.lambdaj.Lambda.on;

public class AssetAttachmentsPanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private AssetService assetService;

    List<AssetAttachment> attachments = new ArrayList<AssetAttachment>();
    private WebMarkupContainer existingAttachmentsContainer;

    public AssetAttachmentsPanel(String id, IModel<Asset> assetModel) {
        super(id);

        if (!assetModel.getObject().isNew()) {
            attachments.addAll(assetService.findAssetAttachments(assetModel.getObject()));
        }

        existingAttachmentsContainer = new WebMarkupContainer("existingAttachmentsContainer");
        existingAttachmentsContainer.setOutputMarkupPlaceholderTag(true);

        existingAttachmentsContainer.add(new ListView<AssetAttachment>("existingAttachments", attachments) {
            @Override
            protected void populateItem(final ListItem<AssetAttachment> item) {
                TextArea<String> comments = new TextArea<String>("comments", ProxyModel.of(item.getModel(), on(AssetAttachment.class).getComments()));
                item.add(comments);
                comments.add(new AjaxFormComponentUpdatingBehavior("onblur") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) { } });
                item.add(new Label("fileName", new NameAfterLastFileSeparatorModel(ProxyModel.of(item.getModel(), on(AssetAttachment.class).getFileName()))));
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
        add(new UploadAttachmentForm("uploadAttachmentForm", assetModel));
    }


    class UploadAttachmentForm extends Form {
        private IModel<Asset> assetModel;
        FIDFeedbackPanel feedbackPanel;

        public UploadAttachmentForm(String id, IModel<Asset> assetModel_) {
            super(id);
            assetModel = assetModel_;
            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

            final FileUploadField attachmentUpload = new FileUploadField("attachmentUpload");
            attachmentUpload.setOutputMarkupId(true);
            add(attachmentUpload);
            attachmentUpload.add(new AjaxFormSubmitBehavior("onChange") {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    assetModel.getObject().ensureMobileGuidIsSet();
                    String assetUuid = assetModel.getObject().getMobileGUID();
                    String assetAttachmentUuid = UUID.randomUUID().toString();
                    String docElementId = attachmentUpload.getMarkupId();
                    String uploadJavascript = s3Service.getAssetAttachmentsUploadJavascript(assetUuid, assetAttachmentUuid, docElementId);

                    target.prependJavaScript(uploadJavascript);

                    /*FileUpload fileUpload = attachmentUpload.getFileUpload();

                    File tempDir = PathHandler.getTempDir();
                    String fileName =  fileUpload.getClientFileName();
                    File file = new File(tempDir, fileName);

                    try {
                        FileCopyUtils.copy(fileUpload.getInputStream(), new FileOutputStream(file));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }     */

                    Long uploadMaxFileSizeBytes = Long.parseLong(s3Service.getUploadMaxFileSizeBytes());

                    FileUpload fileUpload = attachmentUpload.getFileUpload();
                    if(fileUpload.getSize() < uploadMaxFileSizeBytes){
                        String fileName = fileUpload.getClientFileName();
                        String getAssetAttachmentsFolderUrl = s3Service.getAssetAttachmentsFolderUrl(assetUuid, assetAttachmentUuid);
                        AssetAttachment attachment = new AssetAttachment();
                        attachment.setMobileId(assetAttachmentUuid);
                        attachment.setFileName(getAssetAttachmentsFolderUrl + fileName);
                        attachments.add(attachment);
                    }
                    else {
                        Long humanReadableFileLimit = uploadMaxFileSizeBytes/(1024*1024);
                        error(new FIDLabelModel("error.file_size_limit", fileUpload.getClientFileName(), humanReadableFileLimit.toString()).getObject());
                    }
                    target.add(((IdentifyOrEditAssetPage)getPage()).getFeedbackPanel());

                    target.add(feedbackPanel, existingAttachmentsContainer, attachmentUpload);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                    //target.add(feedbackPanel);
                }
            });
        }
    }

    public List<AssetAttachment> getAttachments() {
        return attachments;
    }

    public class NameAfterLastFileSeparatorModel extends LoadableDetachableModel<String> {

        private IModel<String> wrappedModel;

        public NameAfterLastFileSeparatorModel(IModel<String> wrappedModel) {
            this.wrappedModel = wrappedModel;
        }

        @Override
        protected String load() {
            String name = wrappedModel.getObject();
            if (name == null) {
                return null;
            }
            return name.substring(name.lastIndexOf(File.separator)+1);
        }

        @Override
        public void detach() {
            super.detach();
            wrappedModel.detach();
        }
    }

}
