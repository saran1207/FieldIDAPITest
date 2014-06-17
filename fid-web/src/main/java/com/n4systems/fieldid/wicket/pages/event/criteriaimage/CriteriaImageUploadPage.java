package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import java.util.List;

public class CriteriaImageUploadPage extends FieldIDAuthenticatedPage {

    @SpringBean private S3Service s3Service;

    private FIDFeedbackPanel feedbackPanel;
    
    private Page listPage;

    public CriteriaImageUploadPage(IModel<CriteriaResult> model, Page listPage) {
        super();
        this.listPage = listPage;
        add(new UploadForm("uploadForm", model));
    }

    private class UploadForm extends Form<CriteriaResult> {
        private FileUploadField uploadField;
        private String comments;

        public UploadForm(String id, final IModel<CriteriaResult> model) {
            super(id, model);
            setOutputMarkupId(true);
            add(uploadField = new FileUploadField("criteriaFileUpload"));
            uploadField.setRequired(true);
            uploadField.add( new AbstractValidator<FileUpload>() {
                public boolean validateOnNullValue(){
                    return true;
                }
                @Override
                protected void onValidate(IValidatable validatable) {
                    FileUpload fileUpload = (FileUpload) ((List) validatable.getValue()).get(0);
                    if(!fileUpload.getContentType().toLowerCase().contains("image")) {
                        ValidationError error = new ValidationError();
                        error.addMessageKey("error.file_image_type_only");
                        validatable.error(error);
                    }
                }

            });
            add(new TextArea("comments", new PropertyModel(this, "comments")));
            add(new AjaxSubmitLink("save") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    FileUpload fileUpload = uploadField.getFileUpload();

                    if (fileUpload != null) {
                        CriteriaResult criteriaResult = getModelObject();

                        byte[] imageData = fileUpload.getBytes();

                        CriteriaResultImage criteriaResultImage = new CriteriaResultImage();
                        criteriaResultImage.setCriteriaResult(criteriaResult);
                        criteriaResultImage.setFileName(fileUpload.getClientFileName());
                        criteriaResultImage.setContentType(fileUpload.getContentType());
                        criteriaResultImage.setMd5sum(DigestUtils.md5Hex(imageData));

                        criteriaResultImage.setComments(comments);

                        String tempFileName = s3Service.uploadTempCriteriaResultImage(criteriaResultImage, imageData);

                        criteriaResultImage.setTempFileName(tempFileName);

                        criteriaResult.getCriteriaImages().add(criteriaResultImage);

                        FieldIDSession.get().setPreviouslyStoredCriteriaResult(criteriaResult);

                        ((CriteriaImageListPage)listPage).showBlankSlate(target, criteriaResult.getCriteriaImages().isEmpty());
                        setResponsePage(listPage);
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    target.add(feedbackPanel);
                }
            });

            add(new AjaxLink<Void>("cancel") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setResponsePage(listPage);
                }
            });

            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        }

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/legacy/newCss/layout/layout.css");
        response.renderCSSReference("style/legacy/newCss/event/criteria_images.css");
        response.renderCSSReference("style/legacy/newCss/component/matt_buttons.css");
    }

}
