package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.wicket.model.EntityModel;
import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import static ch.lambdaj.Lambda.on;

// teminology note : you might also think of this as "places" page.  we might want to refactor code to have common words at some point.
public class OrgSummaryPage extends FieldIDFrontEndPage {

    public <T extends BaseOrg> OrgSummaryPage(T org) {
        this(org.getId());
    }

    public OrgSummaryPage(Long id) {
        super();
        add(new Label("name", ProxyModel.of(new EntityModel(BaseOrg.class, id), on(BaseOrg.class).getName())));
    }

    public OrgSummaryPage(PageParameters params) {
        this(params.get("id").toLong());
    }
}
