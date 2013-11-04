package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.wicket.components.GoogleMap;
import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.GpsLocation;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static ch.lambdaj.Lambda.on;

// terminology note : you might also think of this as "places" page.  we might want to refactor code to have common words at some point.
public class OrgSummaryPage extends FieldIDFrontEndPage {

    public <T extends BaseOrg> OrgSummaryPage(T org) {
        this(org.getId());
    }

    public OrgSummaryPage(Long id) {
        super();
        EntityModel model = new EntityModel(BaseOrg.class, id);
        add(new Label("header", ProxyModel.of(model, on(BaseOrg.class).getName())));
//        add(new GoogleMap("map",ProxyModel.of(new EntityModel(BaseOrg.class, id), on(BaseOrg.class).getGpsLocation())));
        add(new GoogleMap("map", Model.of(new GpsLocation(43.70263,-79.46654))));
        add(new OrgDetailsPanel("orgDetails", model));
        add(new TextArea("comments",Model.of("comments go here")));
    }

    public OrgSummaryPage(PageParameters params) {
        this(params.get("id").toLong());
    }


    @Override
    public void renderHead(IHeaderResponse response) {
        // for starters, i am basing this page on asset summary page.  later on need to either refactor common css or create my own file.
        response.renderCSSReference("style/newCss/asset/asset.css");
    }


    class OrgDetailsPanel extends Fragment {
        IModel<BaseOrg> model;

        OrgDetailsPanel(String id, IModel<BaseOrg> model) {
            super(id,"details",OrgSummaryPage.this);
            this.model = model;
        }
    }

}
