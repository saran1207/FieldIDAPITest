package com.n4systems.fieldid.wicket.pages.event.criteriaimage;

import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.fieldid.wicket.components.ExternalImage;
import com.n4systems.fieldid.wicket.pages.FieldIDAuthenticatedPage;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.criteriaresult.CriteriaResultImage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class CriteriaImageViewListPage extends FieldIDAuthenticatedPage {

    @SpringBean
    private S3Service s3Service;

    @SpringBean
    private PersistenceService persistenceService;

    public CriteriaImageViewListPage(PageParameters params) {
        super();
        Long criteriaResultId = params.get("uniqueID").toLong();
        final CriteriaResult criteriaResult = persistenceService.find(CriteriaResult.class, criteriaResultId);


        add(new ListView<CriteriaResultImage>("images", criteriaResult.getCriteriaImages()) {
            @Override
            protected void populateItem(ListItem<CriteriaResultImage> item) {
                final CriteriaResultImage image = item.getModelObject();
                final int index = item.getIndex();
                AjaxLink viewLink;
                item.add(viewLink = new AjaxLink<Void>("view") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        setResponsePage(new CriteriaImageViewPage(Model.of(criteriaResult), index));
                    }
                });

                viewLink.add(new ExternalImage("thumbnail", s3Service.getCriteriaResultImageOriginalURL(image).toString()));
                item.add(new Label("comments", new PropertyModel<String>(image, "comments")));
            }
        });

        
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.renderCSSReference("style/newCss/layout/layout.css");
        response.renderCSSReference("style/newCss/event/criteria_images.css");
        response.renderJavaScriptReference("javascript/jquery.ThreeDots.min.js");
        response.renderOnDomReadyJavaScript("$('.comments').ThreeDots({ allow_dangle: true })");
    }
}
