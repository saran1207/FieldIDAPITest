package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CriteriaImageEditPage extends FieldIDAuthenticatedPage {
    
    @SpringBean
    ImageService imageService;
    
    @SpringBean
    S3Service s3Service;
    
    public CriteriaImageEditPage(final IModel<CriteriaResult> model, int imageIndex) {
       super();
       add(new EditForm("editForm", model, imageIndex));
    }

    private class EditForm extends Form<CriteriaResult> {

        public EditForm(String id, final IModel<CriteriaResult> model, final int imageIndex) {
            super(id, model);
            setOutputMarkupId(true);

            final CriteriaResultImage image = model.getObject().getCriteriaImages().get(imageIndex);
            
            if(image.getImageData() == null) {
                add(new ExternalImage("image", s3Service.getCriteriaResultImageOriginalURL(image).toString()));
            }else {
                add(new NonCachingImage("image", new AbstractReadOnlyModel<DynamicImageResource>() {
                    @Override
                    public DynamicImageResource getObject() {
                        DynamicImageResource imageResource = new DynamicImageResource() {
                            @Override
                            protected byte[] getImageData(Attributes attributes) {
                                return imageService.scaleImage(image.getImageData(), 510, 510);
                            }
                        };
                        imageResource.setFormat(image.getContentType());
                        return imageResource;
                    }
                }));
            }
            add(new TextArea("comments", new PropertyModel(image, "comments")));

            add(new AjaxSubmitLink("save") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    FieldIDSession.get().setPreviouslyStoredCriteriaResult(model.getObject());
                    setResponsePage(new CriteriaImageListPage(model));
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            });

            add(new AjaxSubmitLink("delete") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    CriteriaResult criteriaResult = getModelObject();
                    criteriaResult.getCriteriaImages().remove(imageIndex);
                    setResponsePage(new CriteriaImageListPage(model));
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
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


