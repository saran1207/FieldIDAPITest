package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CriteriaImageListPage extends FieldIDAuthenticatedPage {

    @SpringBean
    ImageService imageService;

    @SpringBean
    S3Service s3Service;

    public CriteriaImageListPage(final IModel<CriteriaResult> model) {
        super();
        ContextImage addPhotoSlate;
        add(addPhotoSlate = new ContextImage("addPhotoSlate", "images/add-photo-slate.png"));
        addPhotoSlate.setVisible(model.getObject().getCriteriaImages().isEmpty());
        add(new ListView<CriteriaResultImage>("images", model.getObject().getCriteriaImages()) {
            @Override
            protected void populateItem(ListItem<CriteriaResultImage> item) {
                final CriteriaResultImage image = item.getModelObject();
                final int index = item.getIndex();
                AjaxLink editLink;
                item.add(editLink = new AjaxLink<Void>("edit") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setResponsePage(new CriteriaImageEditPage(model, index));
                    }
                });

                if(image.getImageData() == null) {
                    editLink.add(new ExternalImage("thumbnail", s3Service.getCriteriaResultImageOriginalURL(image).toString()));
                } else {
                    editLink.add(new NonCachingImage("thumbnail", new AbstractReadOnlyModel<DynamicImageResource>() {
                        @Override 
                        public DynamicImageResource getObject() {
                            DynamicImageResource imageResource = new DynamicImageResource() {
                                @Override
                                protected byte[] getImageData(Attributes attributes) {
                                    return imageService.scaleImage(image.getImageData(), 150, 150);
                                }
                            };
                            imageResource.setFormat(image.getContentType());
                            return imageResource;
                        }
                    }));
                }
                item.add(new Label("comments", new PropertyModel<String>(image, "comments")));
            }
        });
        
        add(new AjaxLink<Void>("upload") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new CriteriaImageUploadPage(model));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/criteria_images.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderJavaScriptReference("javascript/jquery.ThreeDots.min.js");
        response.renderOnDomReadyJavaScript("$('.comments').ThreeDots({ whole_word:false })");
    }

}
