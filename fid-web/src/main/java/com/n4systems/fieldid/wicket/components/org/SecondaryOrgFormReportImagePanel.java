package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.service.user.UserGroupService;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.orgs.SecondaryOrg;
import com.n4systems.reporting.PathHandler;
import com.n4systems.util.persistence.image.UploadedImage;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondaryOrgFormReportImagePanel extends Panel {

    private static final Logger logger = Logger.getLogger(SecondaryOrgFormReportImagePanel.class);

    private UploadedImage uploadedImage;

    public SecondaryOrgFormReportImagePanel(String id, IModel<SecondaryOrg> secondaryOrg, UploadedImage reportImage) {
        super(id, secondaryOrg);
        this.uploadedImage = reportImage;

        UploadForm uploadForm;
        add(uploadForm = new UploadForm("uploadForm"));
        uploadForm.setMultiPart(true);
        uploadForm.setVisible(!secondaryOrg.getObject().isNew());
    }

    class UploadForm extends Form<FileAttachment> {
        FileUploadField uploadField;
        Label fileName;

        public UploadForm(String id) {
            super(id);
            setOutputMarkupId(true);

            final WebMarkupContainer fileDisplay = new WebMarkupContainer("fileDisplay");

            add(uploadField = new FileUploadField("digitalSignature"));
            uploadField.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    FileUpload fileUpload = uploadField.getFileUpload();
                    fileName.setDefaultModelObject(fileUpload.getClientFileName());
                    saveFileAttachment(fileUpload);
                    fileDisplay.setVisible(true);
                    uploadField.setVisible(false);
                    target.add(UploadForm.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });
            if(uploadedImage.isExistingImage())
                fileDisplay.add(fileName = new Label("fileName", Model.of(uploadedImage.getImage().getName())));
            else
                fileDisplay.add(fileName = new Label("fileName", Model.of(new String())));
            fileDisplay.add(new AjaxLink<Void>("removeLink") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    fileDisplay.setVisible(false);
                    uploadField.setVisible(true);
                    target.add(UploadForm.this);
                    uploadedImage.setRemoveImage(true);
                }
            });
            fileDisplay.setOutputMarkupId(true);
            fileDisplay.setVisible(uploadedImage.isExistingImage());
            uploadField.setVisible(!uploadedImage.isExistingImage());
            add(fileDisplay);
        }
    }

    public void saveFileAttachment(FileUpload fileUpload) {
        if (fileUpload != null) {
            File tempFilePath = PathHandler.getTempFile(fileUpload.getClientFileName());
            String uploadedFilePath = new File( tempFilePath.getParent() ).getName() + '/' + tempFilePath.getName();

            UploadedImage newImage = new UploadedImage();
            try {
                IOUtils.copy(fileUpload.getInputStream(), new FileOutputStream(tempFilePath));
                newImage.setUploadDirectory(uploadedFilePath);
                newImage.setImage(new File(uploadedFilePath));
                newImage.setNewImage(true);
                uploadedImage = newImage;
            } catch (IOException e) {
                logger.error("error with attaching file", e);
            }
        }
    }

    public UploadedImage getUploadedImage() {
        return uploadedImage;
    }

}
