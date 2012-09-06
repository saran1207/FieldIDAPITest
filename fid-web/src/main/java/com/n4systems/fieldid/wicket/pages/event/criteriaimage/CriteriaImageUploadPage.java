package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
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

public class CriteriaImageUploadPage extends FieldIDAuthenticatedPage {

    private FIDModalWindow modalWindow;

    public CriteriaImageUploadPage(IModel<CriteriaResult> model, FIDModalWindow actionsModalWindow) {
        super();
        this.modalWindow = actionsModalWindow;
        add(new UploadForm("uploadForm", model));
    }

    private class UploadForm extends Form<CriteriaResult> {
        private FileUploadField uploadField;
        private String comments;

        public UploadForm(String id, final IModel<CriteriaResult> model) {
            super(id, model);
            setOutputMarkupId(true);
            add(uploadField = new FileUploadField("criteriaFileUpload"));
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
                    }


                    setResponsePage(new CriteriaImageListPage(model, modalWindow));
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });

            add(new AjaxLink<Void>("cancel") {
                @Override
                public void onClick(AjaxRequestTarget target) {
                    setResponsePage(new CriteriaImageListPage(model, modalWindow));
                }
            });
        }

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/criteria_images.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }

}