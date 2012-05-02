package com.n4systems.fieldid.service.org;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.orgs.*;
import com.n4systems.util.persistence.*;
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
    public PrimaryOrg getPrimaryOrgForTenant(Long tenantId, Boolean useUserSecurity) {
        QueryBuilder<PrimaryOrg> query;

        query = createTenantSecurityBuilder(PrimaryOrg.class);

        query.addSimpleWhere("tenant.id", tenantId);

        return persistenceService.find(query);
    }
    
    public PrimaryOrg getPrimaryOrgForTenant(Long tenantId) {
        return getPrimaryOrgForTenant(tenantId, true);
    }
    
    public OrgList getAllOrgsLike(String value) {
        int THRESHOLD = 20;  // XXX : make this configurable.
        return getAllOrgsLike(value, THRESHOLD);
    }
    
    public OrgList getAllOrgsLike(String value, int threshold) {
        OrgQuery orgQuery = new OrgQuery(value);
        QueryBuilder<? extends BaseOrg> query = createUserSecurityBuilder(orgQuery.getSearchClass());
        
        query.addWhere(orgQuery.getWhere());
        query.setLimit(threshold*5/4);  // allow for a bit more than threshold, so we definitely know we have lots more matches.
        List<? extends BaseOrg> result = persistenceService.findAll(query);
        
        return new OrgList(result, orgQuery, threshold);
    }


}










