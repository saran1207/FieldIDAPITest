package com.n4systems.fieldid.service.org;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class OrgService extends FieldIdPersistenceService {

    public List<InternalOrg> getInternalOrgs() {
        QueryBuilder<InternalOrg> query = new QueryBuilder<InternalOrg>(InternalOrg.class, userSecurityFilter);
        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    public List<DivisionOrg> getDivisionsUnder(BaseOrg org) {
        QueryBuilder<DivisionOrg> query = new QueryBuilder<DivisionOrg>(DivisionOrg.class, userSecurityFilter);
        query.addSimpleWhere("parent", org);
        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    public List<CustomerOrg> getCustomersUnder(BaseOrg org) {
        QueryBuilder<CustomerOrg> query = new QueryBuilder<CustomerOrg>(CustomerOrg.class, userSecurityFilter);
        query.addSimpleWhere("parent", org);
        query.addOrder("name");

        return persistenceService.findAll(query);
    }

}
