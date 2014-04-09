package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.asset.AssetService;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.fieldid.wicket.pages.asset.AssetEventsPage;
import com.n4systems.fieldid.wicket.pages.identify.IdentifyOrEditAssetPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.Asset;
import com.n4systems.model.asset.AssetAttachment;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.ConfigEntry;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.Component;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
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

    private static final Logger logger = Logger.getLogger(AssetAttachmentsPanel.class);

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private AssetService assetService;

    List<AssetAttachment> attachments = new ArrayList<AssetAttachment>();
    //protected String wicketAjaxCall = "wicketAjaxCall";
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
                    protected void onUpdate(AjaxRequestTarget target) { }
                });
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

        /*existingAttachmentsContainer.add(new AjaxEventBehavior("onerror") {
            protected void onEvent(AjaxRequestTarget target) {
                System.out.println("ajax onerror: ");
                attachments.remove(0); //TODO fix this!
                target.add(existingAttachmentsContainer);
            }
        });*/

        existingAttachmentsContainer.add(new AbstractDefaultAjaxBehavior() {
            @Override
            public void renderHead(Component component, IHeaderResponse response) {
                super.renderHead(component, response);
                StringBuilder javascript = new StringBuilder();
                javascript.append("if(!window.containerAjaxCallbackUrlLookup){" +
                                  "    window.containerAjaxCallbackUrlLookup = {};" +
                                  "}" +
                                  "window.containerAjaxCallbackUrlLookup['" + existingAttachmentsContainer.getMarkupId() + "'] = '" + getCallbackUrl() + "';" +
                                  "getAjaxCallbackUrl = function(containerMarkupId){" +
                                  "    return window.containerAjaxCallbackUrlLookup[containerMarkupId];" +
                                  "};");
                response.renderOnDomReadyJavaScript(javascript.toString());
            }

            @Override
            protected void respond(AjaxRequestTarget target) {
                String filename = getRequest().getRequestParameters().getParameterValue("filename").toString();
                String uuid = getRequest().getRequestParameters().getParameterValue("uuid").toString();
                Integer status = getRequest().getRequestParameters().getParameterValue("status").toInteger();
                for(int index = 0; index < attachments.size(); index++){
                    if(attachments.get(index).getMobileId().equals(uuid)){
                        if(status == -1){
                            attachments.get(index).setUploadInProgress(true);
                        }
                        else {
                            attachments.get(index).setUploadInProgress(false);
                            if(status < 200 || status >= 300){
                                attachments.remove(index);
                                error(new FIDLabelModel("error.file_upload_failed", filename, status.toString()).getObject());
                                target.add(((IdentifyOrEditAssetPage)getPage()).getFeedbackPanel(), existingAttachmentsContainer);
                            }
                        }
                        break;
                    }
                }
            }
        });

        add(existingAttachmentsContainer);
        add(new UploadAttachmentForm("uploadAttachmentForm", assetModel, existingAttachmentsContainer.getMarkupId()));
    }


    class UploadAttachmentForm extends Form {
        private IModel<Asset> assetModel;
        protected String callbackContainerMarkupId;

        public UploadAttachmentForm(String id, IModel<Asset> assetModel_, String callbackContainerMarkupId_) {
            super(id);
            assetModel = assetModel_;
            callbackContainerMarkupId = callbackContainerMarkupId_;


            final FileUploadField attachmentUpload = new FileUploadField("attachmentUpload");
            attachmentUpload.setOutputMarkupId(true);
            add(attachmentUpload);
            attachmentUpload.add(new AjaxFormSubmitBehavior("onChange") {

                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    assetModel.getObject().ensureMobileGuidIsSet();
                    String assetUuid = assetModel.getObject().getMobileGUID();
                    String assetAttachmentUuid = UUID.randomUUID().toString();
                    String uploadFormMarkupId = attachmentUpload.getMarkupId();
                    String uploadJavascript = s3Service.getAssetAttachmentsUploadJavascript(assetUuid, assetAttachmentUuid, uploadFormMarkupId, callbackContainerMarkupId);

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

                    target.add(((IdentifyOrEditAssetPage)getPage()).getFeedbackPanel(), existingAttachmentsContainer, attachmentUpload);
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
