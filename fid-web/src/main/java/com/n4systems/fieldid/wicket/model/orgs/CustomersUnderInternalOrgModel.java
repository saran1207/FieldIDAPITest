package com.n4systems.fieldid.wicket.model.orgs;

import com.n4systems.fieldid.service.org.OrgService;
import com.n4systems.fieldid.wicket.FieldIDSession;
import com.n4systems.fieldid.wicket.model.FieldIDSpringModel;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.SecurityFilter;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

public class CustomersUnderInternalOrgModel extends FieldIDSpringModel<List<CustomerOrg>> {

    @SpringBean
    private OrgService orgService;

    private IModel<BaseOrg> internalOrgModel;

    public CustomersUnderInternalOrgModel(IModel<BaseOrg> internalOrgModel) {
        this.internalOrgModel = internalOrgModel;
    }

    @Override
    protected List<CustomerOrg> load() {
        if (internalOrgModel.getObject() == null) {
            return Collections.emptyList();
        }

        SecurityFilter securityFilter = FieldIDSession.get().getSessionUser().getSecurityFilter();

        List<CustomerOrg> customerOrgs = orgService.getCustomersUnder(internalOrgModel.getObject());

        CustomerOrg customerOrg = securityFilter.getOwner().getCustomerOrg();
        if (customerOrg != null && customerOrgs.isEmpty()) {
            customerOrgs.add(customerOrg);
        }

        return customerOrgs;
    }

}
