package com.n4systems.fieldid.wicket.pages.org;

import com.n4systems.fieldid.service.org.OrgListFilterCriteria;
import com.n4systems.fieldid.wicket.components.navigation.NavigationBar;
import com.n4systems.fieldid.wicket.components.org.InternalOrgFormDetailsPanel;
import com.n4systems.fieldid.wicket.model.FIDLabelModel;
import com.n4systems.model.orgs.SecondaryOrg;
import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

import static com.n4systems.fieldid.wicket.model.navigation.NavigationItemBuilder.aNavItem;

public class AddSecondaryOrgPage extends OrgPage {

    @Override
    protected void doSave() {
        create();
        setResponsePage(OrgsListPage.class);
    }

    @Override
    protected Component createDetailsPanel(String id) {
        return new InternalOrgFormDetailsPanel(id, internalOrg);
    }

    @Override
    protected void addNavBar(String navBarId) {
        OrgListFilterCriteria criteria = new OrgListFilterCriteria(false);
        Long activeOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly(false));
        Long archivedOrgCount = orgService.countSecondaryOrgs(criteria.withArchivedOnly());
        add(new NavigationBar(navBarId,
                aNavItem().label(new FIDLabelModel("nav.view_all.count", activeOrgCount)).page(OrgsListPage.class).build(),
                aNavItem().label(new FIDLabelModel("nav.view_all_archived.count", archivedOrgCount)).page(ArchivedOrgsListPage.class).build(),
                aNavItem().label("nav.add").page(EditOrgPage.class).onRight().build()
        ));
    }

}
