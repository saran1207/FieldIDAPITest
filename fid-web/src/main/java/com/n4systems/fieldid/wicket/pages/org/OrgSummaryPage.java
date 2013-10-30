package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.wicket.pages.FieldIDFrontEndPage;
import com.n4systems.fieldid.wicket.util.ProxyModel;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.markup.html.basic.Label;

import static ch.lambdaj.Lambda.on;

// you might also think of this as "places" page.
public class OrgSummaryPage extends FieldIDFrontEndPage {

    public <T extends BaseOrg> OrgSummaryPage(T org) {
        add(new Label("name", ProxyModel.of(org,on(BaseOrg.class).getName())));
    }

}
