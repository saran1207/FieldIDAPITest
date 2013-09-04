package com.n4systems.fieldid.wicket.components.assettype;

import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.ContextAbsolutizer;
import com.n4systems.model.AssetType;
import com.n4systems.reporting.PathHandler;
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

import java.io.File;

public class AssetTypeImagePanel extends Panel {

    String tempFileName;
    String clientFileName;
    private byte[] uploadedBytes;
    private boolean imageUpdated = false;

    public AssetTypeImagePanel(String id, IModel<AssetType> assetTypeModel) {
        super(id);

        clientFileName = assetTypeModel.getObject().getImageName();

        final WebMarkupContainer uploadContainer = new WebMarkupContainer("imageUploadPanel");
        final WebMarkupContainer uploadedFileDisplayPanel = new WebMarkupContainer("uploadedFileDisplayPanel");

        add(uploadContainer.setOutputMarkupPlaceholderTag(true));
        add(uploadedFileDisplayPanel.setOutputMarkupPlaceholderTag(true).setVisible(false));

        Form fileUploadForm = new Form("fileUploadForm");
        uploadContainer.add(fileUploadForm);
        uploadContainer.setVisible(clientFileName == null);

        final FileUploadField fileUploadField = new FileUploadField("assetTypeImageFile");
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


        boolean imageExists;
        String imageUrl = "";
        if(assetTypeModel.getObject().getImageName() != null) {
            imageUrl = ContextAbsolutizer.toContextAbsoluteUrl("/file/downloadAssetTypeImage.action?uniqueID=" + assetTypeModel.getObject().getId());
            if(assetTypeModel.getObject().getImageName() != null)
                imageExists = new File(PathHandler.getAssetTypeImageFile(assetTypeModel.getObject()), assetTypeModel.getObject().getImageName()).exists();
            else
                imageExists = false;
        } else
            imageExists = false;

        ExternalImage assetImage;
        uploadedFileDisplayPanel.add(assetImage = new ExternalImage("image", imageUrl));
        assetImage.setVisible(imageExists);

        uploadedFileDisplayPanel.add(new Label("fileName", new PropertyModel<String>(this, "clientFileName")));
        uploadedFileDisplayPanel.add(new AjaxLink("removeLink") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                uploadedBytes = null;
                tempFileName = null;
                clientFileName = null;
                uploadContainer.setVisible(true);
                uploadedFileDisplayPanel.setVisible(false);
                target.add(uploadContainer, uploadedFileDisplayPanel);
                imageUpdated = true;
            }
        });

        uploadedFileDisplayPanel.setVisible(clientFileName != null);
    }

    public byte[] getAssetTypeImageBytes() {
        return uploadedBytes;
    }

    public String getClientFileName() {
        return clientFileName;
    }

    public boolean isImageUpdated() {
        return imageUpdated;
    }

}
