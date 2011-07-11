package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.InternalOrgsLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

public class InternalOrgsModel extends LoadableDetachableModel<List<InternalOrg>> {

    @Override
    protected List<InternalOrg> load() {
        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();
        List<InternalOrg> internalOrgs = new InternalOrgsLoader(securityFilter).load();

        PrimaryOrg primaryOrg = securityFilter.getOwner().getPrimaryOrg();
        if (primaryOrg != null && internalOrgs.isEmpty()) {
            internalOrgs.add(primaryOrg);
        }

        return internalOrgs;
    }

}
