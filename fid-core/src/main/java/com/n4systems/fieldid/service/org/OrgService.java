package com.n4systems.fieldid.service.org;

import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.orgs.*;
import com.n4systems.util.collections.OrgList;
import com.n4systems.util.persistence.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    
    public OrgList getAllOrgsLike(String value, int threshold) {
        OrgQueryParser orgQueryParser = new OrgQueryParser(value);

        if (StringUtils.isBlank(value)) {  // if user doesn't know what to type, we'll just return top of hierarchy.
            // note : currently doesn't work because wiquery/js doesn't escape blank string.
            //  so if you presses space in autocomplete box it just hangs 'cause it creates malformed url.
            QueryBuilder<PrimaryOrg> query = createUserSecurityBuilder(PrimaryOrg.class);
            List<PrimaryOrg> primaryOrgs = persistenceService.findAll(query);
            QueryBuilder<SecondaryOrg> query2 = createUserSecurityBuilder(SecondaryOrg.class);
            List<PrimaryOrg> secondaryOrgs = persistenceService.findAll(query);
            List<BaseOrg> result = new ArrayList<BaseOrg>();
            result.addAll(secondaryOrgs);
            result.addAll(primaryOrgs);
            return new OrgList(result, orgQueryParser, threshold );
        }

        QueryBuilder<BaseOrg> query = createUserSecurityBuilder(BaseOrg.class);
        List<? extends BaseOrg> parents = findParentsLike(orgQueryParser);
        if(parents.isEmpty()) {
            // if we were intending to look for parents but found none, then abort search right now & return empty.
            if (orgQueryParser.getParentTerms().size()>0) {
                return new OrgList(new ArrayList<BaseOrg>(), orgQueryParser, threshold);
            }
        } else {
            WhereParameterGroup group = new WhereParameterGroup("isParentCustomer");
            group.setChainOperator(WhereClause.ChainOp.OR);
            List<PrimaryOrg> primaryParentOrgs = getPrimaryParentOrgs(parents);
            if (!primaryParentOrgs.isEmpty()) {
                group.addClause(new WhereParameter(WhereParameter.Comparator.IN, "primaryOrg", "primaryOrg", primaryParentOrgs, null, false, WhereClause.ChainOp.OR));
            }
            List<CustomerOrg> customerParentOrgs = getCustomerParentOrgs(parents);
            if (!customerParentOrgs.isEmpty()) {
                group.addClause(new WhereParameter(WhereParameter.Comparator.IN,"customerOrg","customerOrg", customerParentOrgs,null, false, WhereClause.ChainOp.OR));
            }
            query.addWhere(group);
        }

        if (StringUtils.isNotBlank(orgQueryParser.getSearchTerm())) {
            query.addWhere(new WhereParameter<String>(WhereParameter.Comparator.LIKE, "name", "name", orgQueryParser.getSearchTerm(), WhereParameter.WILDCARD_BOTH, false));
        }

        query.setLimit(Math.min(threshold*5,100));  // allow for more than threshold, so we can provide a quality sample.
        List<BaseOrg> result = persistenceService.findAll(query);
        //add Parents To Results
        for (BaseOrg org:parents) {
            if (!result.contains(org)) {
                result.add(org);
            }
        }

        return new OrgList(result, orgQueryParser, threshold);
    }

    private List<PrimaryOrg> getPrimaryParentOrgs(List<? extends BaseOrg> parents) {
        List<PrimaryOrg> result = Lists.newArrayList();
        for (BaseOrg org:parents) { 
            if (org instanceof PrimaryOrg) {
                result.add((PrimaryOrg)org);
            }
        }
        return result;
    }

    private List<CustomerOrg> getCustomerParentOrgs(List<? extends BaseOrg> parents) {
        List<CustomerOrg> result = Lists.newArrayList();
        for (BaseOrg org:parents) {
            if (org instanceof CustomerOrg) {
                result.add((CustomerOrg)org);
            }
        }
        return result;
    }

    private List<? extends BaseOrg> findParentsLike(OrgQueryParser orgQueryParser) {
        List<BaseOrg> result = Lists.newArrayList();
        switch (orgQueryParser.getParentTerms().size()) {
            case 0:
                return Lists.newArrayList();
            case 1: //just a single term like    "toron:xx"   search for any orgs that have parent (customer OR org) like "%toron%"
                QueryBuilder<BaseOrg> query = createUserSecurityBuilder(BaseOrg.class);
                String term = orgQueryParser.getParentTerms().get(0);
                query.addWhere(new WhereParameter<String>(WhereParameter.Comparator.LIKE, "name", "name", term, WhereParameter.WILDCARD_BOTH, false));
                query.addWhere(WhereClauseFactory.createIsNull("divisionOrg"));
                return persistenceService.findAll(query);
            case 2: // two levels like  "foo:bar:xx"    need to find orgs that have parent of bar, grandparent of foo
                String org = orgQueryParser.getParentTerms().get(0);
                String customer = orgQueryParser.getParentTerms().get(1);
                QueryBuilder<CustomerOrg> customerQuery = createUserSecurityBuilder(CustomerOrg.class);
                customerQuery.addWhere(new WhereParameter<String>(WhereParameter.Comparator.LIKE, "name", "name", customer, WhereParameter.WILDCARD_BOTH, false));
                customerQuery.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "parent_name", "parent.name", org, WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.AND));
                return persistenceService.findAll(customerQuery);
            default:
                throw new IllegalStateException("parser can only supports having 1 or 2 parents specified.");
        }
    }

}


