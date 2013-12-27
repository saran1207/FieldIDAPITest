package com.n4systems.fieldid.wicket.components.org;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.PlaceSummaryPage;
import com.n4systems.model.orgs.BaseOrg;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

public class BackToPlaceSubHeader extends Panel {

    public BackToPlaceSubHeader(String id, IModel<BaseOrg> orgModel) {
        super(id, orgModel);

        add(new BookmarkablePageLink<PlaceSummaryPage>("backToPlaceLink", PlaceSummaryPage.class, PageParametersBuilder.id(orgModel.getObject().getId()))
                .add(new Label("backToLabel", new PropertyModel<String>(orgModel, "name"))));

    }
}
