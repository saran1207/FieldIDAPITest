package com.n4systems.fieldid.wicket.pages.identify.components;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Asset;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class AssetImagePanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    String tempFileName;
    String clientFileName;
    private byte[] uploadedBytes;
    private boolean imageUpdated = false;

    public AssetImagePanel(String id, IModel<Asset> assetModel) {
        super(id);

        clientFileName = assetModel.getObject().getImageName();

        final WebMarkupContainer uploadContainer = new WebMarkupContainer("imageUploadPanel");
        final WebMarkupContainer uploadedFileDisplayPanel = new WebMarkupContainer("uploadedFileDisplayPanel");

        add(uploadContainer.setOutputMarkupPlaceholderTag(true));
        add(uploadedFileDisplayPanel.setOutputMarkupPlaceholderTag(true).setVisible(false));

        Form fileUploadForm = new Form("fileUploadForm");
        uploadContainer.add(fileUploadForm);
        uploadContainer.setVisible(clientFileName == null);

        final FileUploadField fileUploadField = new FileUploadField("assetImageFile");
        fileUploadField.add(new AjaxFormSubmitBehavior("onchange") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                FileUpload uploadedFile = fileUploadField.getFileUpload();
                uploadedBytes = uploadedFile.getBytes();
                clientFileName = uploadedFile.getClientFileName();
                uploadContainer.setVisible(false);
                uploadedFileDisplayPanel.setVisible(true);
                target.add(uploadContainer, uploadedFileDisplayPanel);
                imageUpdated = true;
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
            }
        });

        fileUploadForm.add(fileUploadField);

        uploadedFileDisplayPanel.add(new Label("fileName", new PropertyModel<String>(this, "clientFileName")));
        uploadedFileDisplayPanel.add(new AjaxLink("removeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                uploadedBytes = null;
                tempFileName = null;
                uploadContainer.setVisible(true);
                uploadedFileDisplayPanel.setVisible(false);
                target.add(uploadContainer, uploadedFileDisplayPanel);
                imageUpdated = true;
            }
        });

        uploadedFileDisplayPanel.setVisible(clientFileName != null);
    }

    public byte[] getAssetImageBytes() {
        return uploadedBytes;
    }

    public String getClientFileName() {
        return clientFileName;
    }

    public boolean isImageUpdated() {
        return imageUpdated;
    }

}
