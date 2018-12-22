package com.n4systems.fieldid.wicket.components.org.columns;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.model.navigation.PageParametersBuilder;
import com.n4systems.fieldid.wicket.pages.org.ArchivedOrgConfirmPage;
import com.n4systems.fieldid.wicket.pages.org.EditOrgPage;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class OrgListCell extends Panel {

    @SpringBean
    OrgService orgService;

    public OrgListCell(String componentId, IModel<SecondaryOrg> rowModel) {
        super(componentId, rowModel);

        final InternalOrg internalOrg = rowModel.getObject();

        if(internalOrg.isSecondary()) {
            add(new BookmarkablePageLink<EditOrgPage>("edit", EditOrgPage.class, PageParametersBuilder.uniqueId(internalOrg.getId())));
        }  else {
            add(new BookmarkablePageLink<EditOrgPage>("edit", EditOrgPage.class, PageParametersBuilder.uniqueId(internalOrg.getId())));
        }

        add(new BookmarkablePageLink<EditOrgPage>("archive", ArchivedOrgConfirmPage.class, PageParametersBuilder.uniqueId(internalOrg.getId())));



    }
}
