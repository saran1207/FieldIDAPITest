package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.DivisionOrg;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class OrgCell extends Panel {
    public OrgCell(String id, final IModel<BaseOrg> rowModel) {
        super(id);
        final BaseOrg org = rowModel.getObject();
        add(new Link("org") {
            @Override public void onClick() {
                if (org instanceof DivisionOrg) {
                    setResponsePage(PlaceDescendantsPage.class, PageParametersBuilder.id(org.getId()));
                } else {
                    setResponsePage(PlaceDescendantsPage.class, PageParametersBuilder.id(org.getId()));
                }
            }
        }.add(new Label("name",org.getName())));
    }
}
