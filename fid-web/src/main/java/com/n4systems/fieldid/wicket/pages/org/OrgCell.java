package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.DivisionOrg;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class OrgCell extends Panel {
    public OrgCell(String id, final IModel<BaseOrg> rowModel) {
        super(id);
        final BaseOrg org = rowModel.getObject();

        if (org instanceof DivisionOrg) {
            add(new BookmarkablePageLink<PlaceSummaryPage>("org", PlaceSummaryPage.class, PageParametersBuilder.id(org.getId()))
                    .add(new Label("name", org.getName())));
        } else {
            add(new BookmarkablePageLink<PlaceDescendantsPage>("org", PlaceDescendantsPage.class, PageParametersBuilder.id(org.getId()))
                    .add(new Label("name", org.getName())));

        }

    }
}
