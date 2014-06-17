package com.n4systems.fieldid.wicket.components.assettype;

import com.google.common.io.Files;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.behavior.Watermark;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.AssetType;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.resource.BlobImageResource;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.resource.ContextRelativeResource;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import static ch.lambdaj.Lambda.on;

public class AssetTypeAttachmentsPanel extends Panel {

    @SpringBean
    protected S3Service s3Service;

    List<FileAttachment> attachments = new ArrayList<FileAttachment>();
    private WebMarkupContainer existingAttachmentsContainer;

    public AssetTypeAttachmentsPanel(String id, final IModel<AssetType> assetTypeModel) {
        super(id);

        if (!assetTypeModel.getObject().isNew()) {
            attachments.addAll(assetTypeModel.getObject().getAttachments());

        }

        existingAttachmentsContainer = new WebMarkupContainer("existingAttachmentsContainer");
        existingAttachmentsContainer.setOutputMarkupPlaceholderTag(true);

        existingAttachmentsContainer.add(new ListView<FileAttachment>("existingAttachments", attachments) {
            @Override
            protected void populateItem(final ListItem<FileAttachment> item) {

                TextArea<String> comments = new TextArea<String>("comments", ProxyModel.of(item.getModel(), on(AssetAttachment.class).getComments()));
                item.add(comments);
                comments.add(new AjaxFormComponentUpdatingBehavior("onblur") {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) { } });
                comments.add(new Watermark(new FIDLabelModel("label.asset_type.form.files").getObject()));

                WebComponent image, imageFilename;
                if (item.getModelObject().isImage()) {
                    if(item.getModelObject().isNew()) {
                        item.add(image = new Image("attachmentImage",new DynamicImageResource() {
                            @Override
                            protected byte[] getImageData(Attributes attributes) {
                                try {
                                    return Files.toByteArray(PathHandler.getTempFileInRoot(item.getModelObject().getFileName()));
                                } catch (IOException e) {
                                    return new byte[0];
                                }
                            }
                        }));
                    }else {
                        String imageUrl;
                        if(item.getModelObject().isRemote()){
                            imageUrl = s3Service.getFileAttachmentUrl(item.getModelObject()).toString();
                        }
                        else {
                            imageUrl = ContextAbsolutizer.toContextAbsoluteUrl("file/downloadAssetTypeAttachedFile.action?fileName=" + item.getModelObject().getFileName().replace(" ", "+") + "&uniqueID=" + assetTypeModel.getObject().getId() + "&attachmentID=" + item.getModelObject().getId());
                        }
                        item.add(image = new ExternalImage("attachmentImage", imageUrl));
                    }
                    image.add(new AttributeModifier("class", "attachmentImage"));
                } else {
                    item.add(image = new Image("attachmentImage", new ContextRelativeResource("images/file-icon.png")));
                    image.add(new AttributeModifier("class", "attachmentIcon"));
                    //TODO arezafar why is there no link for non-image attachments?
                }

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
        add(new UploadAttachmentForm("uploadAttachmentForm"));
    }

    class UploadAttachmentForm extends Form {
        public UploadAttachmentForm(String id) {
            super(id);

            final FileUploadField attachmentUpload = new FileUploadField("attachmentUpload");
            attachmentUpload.setOutputMarkupId(true);
            add(attachmentUpload);
            attachmentUpload.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    FileUpload fileUpload = attachmentUpload.getFileUpload();

                    File tempDir = PathHandler.getTempDir();
                    String fileName = fileUpload.getClientFileName();
                    File file = new File(tempDir, fileName);

                    try {
                        FileCopyUtils.copy(fileUpload.getInputStream(), new FileOutputStream(file));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    FileAttachment attachment = new FileAttachment();
                    attachment.ensureMobileIdIsSet();
                    attachment.setFileName(tempDir.getName() + File.separator + fileName);

                    attachments.add(attachment);
                    target.add(existingAttachmentsContainer, attachmentUpload);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });
        }
    }

    public List<FileAttachment> getAttachments() {
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
