package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CriteriaImageViewPage extends FieldIDAuthenticatedPage {

    @SpringBean
    S3Service s3Service;

    public CriteriaImageViewPage(final IModel<CriteriaResult> model, int imageIndex) {
        
        final CriteriaResultImage image = model.getObject().getCriteriaImages().get(imageIndex);
        add(new ExternalImage("image", s3Service.getCriteriaResultImageOriginalURL(image).toString()));
        add(new Label("comments", new PropertyModel<String>(image, "comments")));
        
        add(new Link<Void>("back") {
            @Override
            public void onClick() {
                setResponsePage(new CriteriaImageViewListPage(PageParametersBuilder.uniqueId(model.getObject().getId())));
            }
        });
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/event/criteria_images.css");
        response.renderCSSReference("style/newCss/component/matt_buttons.css");
    }
    
}
