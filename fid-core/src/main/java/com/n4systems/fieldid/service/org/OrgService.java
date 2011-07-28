package com.n4systems.fieldid.service.org;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class OrgService extends FieldIdPersistenceService {

    @Transactional(readOnly = true)
    public List<InternalOrg> getInternalOrgs() {
        QueryBuilder<InternalOrg> query = createUserSecurityBuilder(InternalOrg.class);
        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    @Transactional(readOnly = true)
    public List<DivisionOrg> getDivisionsUnder(BaseOrg org) {
        QueryBuilder<DivisionOrg> query = createUserSecurityBuilder(DivisionOrg.class);
        query.addSimpleWhere("parent", org);
        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    @Transactional(readOnly = true)
    public List<CustomerOrg> getCustomersUnder(BaseOrg org) {
        QueryBuilder<CustomerOrg> query = createUserSecurityBuilder(CustomerOrg.class);
        query.addSimpleWhere("parent", org);
        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    @Transactional(readOnly = true)
    public PrimaryOrg getPrimaryOrgForTenant(Long tenantId) {
        QueryBuilder<PrimaryOrg> query = createUserSecurityBuilder(PrimaryOrg.class);
        query.addSimpleWhere("tenant.id", tenantId);

        return persistenceService.find(query);
    }

}
