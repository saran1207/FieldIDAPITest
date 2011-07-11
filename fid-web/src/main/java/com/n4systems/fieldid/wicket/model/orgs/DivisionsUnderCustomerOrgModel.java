package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.DivisionOrgsForCustomerOrgLoader;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.Collections;
import java.util.List;

public class DivisionsUnderCustomerOrgModel extends LoadableDetachableModel<List<DivisionOrg>> {

    private IModel<BaseOrg> customerOrgModel;

    public DivisionsUnderCustomerOrgModel(IModel<BaseOrg> customerOrgModel) {
        this.customerOrgModel = customerOrgModel;
    }

    @Override
    protected List<DivisionOrg> load() {
        if (customerOrgModel.getObject() == null) {
            return Collections.emptyList();
        }

        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();
        List<DivisionOrg> divisionOrgs = new DivisionOrgsForCustomerOrgLoader(securityFilter)
                .parent(customerOrgModel.getObject())
                .load();

        DivisionOrg divisionOrg = securityFilter.getOwner().getDivisionOrg();
        if (divisionOrg != null && divisionOrgs.isEmpty() && customerOrgModel.getObject().equals(securityFilter.getOwner().getCustomerOrg())) {
            divisionOrgs.add(divisionOrg);
        }
        
        return divisionOrgs;
    }

}
