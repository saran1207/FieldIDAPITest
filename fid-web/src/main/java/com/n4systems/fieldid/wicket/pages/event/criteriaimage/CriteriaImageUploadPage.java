package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.feedback.FIDFeedbackPanel;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
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
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

import java.util.List;

public class CriteriaImageUploadPage extends FieldIDAuthenticatedPage {

    private FIDFeedbackPanel feedbackPanel;

    public CriteriaImageUploadPage(IModel<CriteriaResult> model) {
        super();
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

                        CriteriaResultImage criteriaResultImage = new CriteriaResultImage();
                        criteriaResultImage.setCriteriaResult(criteriaResult);
                        criteriaResultImage.setFileName(fileUpload.getClientFileName());
                        criteriaResultImage.setImageData(fileUpload.getBytes());
                        criteriaResultImage.setContentType(fileUpload.getContentType());

                        criteriaResultImage.setComments(comments);

                        criteriaResult.getCriteriaImages().add(criteriaResultImage);

                        FieldIDSession.get().setPreviouslyStoredCriteriaResult(criteriaResult);

                        setResponsePage(new CriteriaImageListPage(model));
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
                    setResponsePage(new CriteriaImageListPage(model));
                }
            });

            add(feedbackPanel = new FIDFeedbackPanel("feedbackPanel"));

        }

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/layout/layout.css");
        response.renderCSSReference("style/newCss/event/criteria_images.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

}