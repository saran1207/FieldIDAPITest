package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class InternalOrgsModel extends FieldIDSpringModel<List<InternalOrg>> {

    @SpringBean
    private OrgService orgService;

    @Override
    protected List<InternalOrg> load() {
        List<InternalOrg> internalOrgs = orgService.getInternalOrgs();

        PrimaryOrg primaryOrg = FieldIDSession.get().getSessionUser().getOwner().getPrimaryOrg();
        if (primaryOrg != null && internalOrgs.isEmpty()) {
            internalOrgs.add(primaryOrg);
        }

        return internalOrgs;
    }

}
