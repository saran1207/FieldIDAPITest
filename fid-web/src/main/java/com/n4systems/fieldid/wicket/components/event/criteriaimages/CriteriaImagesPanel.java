package com.n4systems.fieldid.wicket.components.event.criteriaimages;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CriteriaImagesPanel extends Panel {

    private @SpringBean S3Service s3Service;

	public CriteriaImagesPanel(String id, final IModel<CriteriaResult> model) {
		super(id, model);
		add(new UploadForm("uploadForm", model));
	}

	private class UploadForm extends Form<CriteriaResult> {
		private FileUploadField uploadField;

		public UploadForm(String id, final IModel<CriteriaResult> model) {
			super(id, model);
			setOutputMarkupId(true);
			add(uploadField = new FileUploadField("criteriaFileUpload"));
			uploadField.add(new AjaxFormSubmitBehavior("onchange") {
                @Override
                protected void onSubmit(AjaxRequestTarget target) {
                    FileUpload fileUpload = uploadField.getFileUpload();

                    if (fileUpload != null) {
                        CriteriaResult criteriaResult = getModelObject();

                        CriteriaResultImage criteriaResultImage = new CriteriaResultImage();
                        criteriaResultImage.setCriteriaResult(criteriaResult);
                        criteriaResultImage.setFileName(fileUpload.getClientFileName());
                        criteriaResultImage.setContentType(fileUpload.getContentType());

                        byte[] imageData = fileUpload.getBytes();

                        criteriaResultImage.setMd5sum(DigestUtils.md5Hex(imageData));

                        String tempFileName = s3Service.uploadTempCriteriaResultImage(criteriaResultImage, imageData);
                        criteriaResultImage.setTempFileName(tempFileName);

                        criteriaResult.getCriteriaImages().add(criteriaResultImage);
                    }

                    onClose(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                }
            });
		}

	}

	protected void onClose(AjaxRequestTarget target) {}

}
