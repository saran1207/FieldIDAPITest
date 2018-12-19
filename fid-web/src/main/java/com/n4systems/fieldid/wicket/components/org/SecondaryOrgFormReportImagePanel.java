package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.ExternalImage;
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
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondaryOrgFormReportImagePanel extends Panel {

    @SpringBean
    private S3Service s3Service;

    private static final Logger logger = Logger.getLogger(SecondaryOrgFormReportImagePanel.class);

    private UploadedImage uploadedImage;
    private SecondaryOrg  secondaryOrg;

    public SecondaryOrgFormReportImagePanel(String id, IModel<SecondaryOrg> secondaryOrg, UploadedImage reportImage) {
        super(id, secondaryOrg);
        this.uploadedImage = reportImage;
        if (secondaryOrg != null) this.secondaryOrg = secondaryOrg.getObject();

        UploadForm uploadForm;
        add(uploadForm = new UploadForm("uploadForm"));
        uploadForm.setMultiPart(true);
        if (secondaryOrg != null) uploadForm.setVisible(!secondaryOrg.getObject().isNew());
    }

    public SecondaryOrg getSecondaryOrg() {return secondaryOrg;}

    class UploadForm extends Form<FileAttachment> {
        FileUploadField uploadField;

        public UploadForm(String id) {
            super(id);
            setOutputMarkupId(true);

            final WebMarkupContainer fileDisplay = new WebMarkupContainer("fileDisplay");

            add(uploadField = new FileUploadField("digitalSignature"));
            uploadField.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    FileUpload fileUpload = uploadField.getFileUpload();
                    saveFileAttachment(fileUpload);
                    fileDisplay.setVisible(true);
                    uploadField.setVisible(false);
                    target.add(UploadForm.this);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });
            if(uploadedImage.isExistingImage()) {
                fileDisplay.add(new ExternalImage("logoImage", s3Service.getSecondaryOrgCertificateLogoURL(getSecondaryOrg().getId())));
            }
            else {
                fileDisplay.add(new ExternalImage("logoImage", "/fieldid/images/attachment-icon.png"));
            }
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
