package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.service.images.ImageService;
import com.n4systems.fieldid.wicket.components.modal.FIDModalWindow;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
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

    public CriteriaImageListPage(final IModel<CriteriaResult> model, final FIDModalWindow actionsModalWindow) {
        super();
        
        add(new ListView<CriteriaResultImage>("images", model.getObject().getCriteriaImages()) {
            @Override
            protected void populateItem(ListItem<CriteriaResultImage> item) {
                final CriteriaResultImage image = item.getModelObject();
                final int index = item.getIndex();
                AjaxLink editLink;
                item.add(editLink = new AjaxLink<Void>("edit") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setResponsePage(new CriteriaImageEditPage(model, index, actionsModalWindow));
                    }
                });

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
                item.add(new Label("comments", new PropertyModel<String>(image, "comments")));
            }
        });
        
        add(new AjaxLink<Void>("upload") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new CriteriaImageUploadPage(model, actionsModalWindow));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/criteria_images.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
        response.renderJavaScriptReference("javascript/jquery.ThreeDots.min.js");
        response.renderOnDomReadyJavaScript("$('.comments').ThreeDots({ allow_dangle: true })");
    }

}
