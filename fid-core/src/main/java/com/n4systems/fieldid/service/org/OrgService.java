package com.n4systems.fieldid.service.org;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.*;
import com.n4systems.util.collections.OrgList;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClause;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
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

    public OrgList search(String term, int threshold) {
        OrgQueryParser orgQueryParser = new OrgQueryParser(term);

        QueryBuilder<BaseOrg> query = createUserSecurityBuilder(BaseOrg.class);
        query.setLimit(Math.min(threshold*5,100));  // allow for more than threshold, so we can provide a quality sample.
        if (StringUtils.isNotBlank(orgQueryParser.getSearchTerm())) {
            query.addWhere(new WhereParameter<String>(WhereParameter.Comparator.LIKE, "name", "name", orgQueryParser.getSearchTerm(), WhereParameter.WILDCARD_BOTH, false));
        }

        List<? extends BaseOrg> parents = findParentsLike(orgQueryParser);

        List<BaseOrg> results;

        // if we were intending to look for parents but found none, then abort search right now & return empty.
        if (parents.isEmpty()) {
            if (orgQueryParser.getParentTerms().size() > 0) {
                return new OrgList(new ArrayList<BaseOrg>(), orgQueryParser, threshold);
            } else {
                results = persistenceService.findAll(query);
            }
        } else {
            results = findChildOrgs(parents, threshold);
        }

        //add Parent To Results
        if (parents.size()==1) {
            if (!results.contains(parents.get(0))) {
                results.add(parents.get(0));
            }
        }

        return new OrgList(results, orgQueryParser, threshold);
    }

    private List<BaseOrg> findChildOrgs(List<? extends BaseOrg> parents, int threshold) {
        List<BaseOrg> results = new ArrayList<BaseOrg>();

        List<BaseOrg> primaryOrgs = findOrgsOfClass(parents, PrimaryOrg.class);
        if (!primaryOrgs.isEmpty()) {
            QueryBuilder<SecondaryOrg> secondaryOrgQueryBuilder = createUserSecurityBuilder(SecondaryOrg.class);
            secondaryOrgQueryBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "primaryOrg", "primaryOrg", primaryOrgs));
            secondaryOrgQueryBuilder.setLimit(threshold);
            results.addAll(persistenceService.findAll(secondaryOrgQueryBuilder));
        }

        QueryBuilder<CustomerOrg> customerOrgQueryBuilder = createUserSecurityBuilder(CustomerOrg.class);
        customerOrgQueryBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "parent", "parent", parents));
        customerOrgQueryBuilder.setLimit(threshold);
        results.addAll(persistenceService.findAll(customerOrgQueryBuilder));

        List<BaseOrg> customerOrgs = findOrgsOfClass(parents, CustomerOrg.class);
        if (!customerOrgs.isEmpty()) {
            QueryBuilder<DivisionOrg> divisionOrgQueryBuilder = createUserSecurityBuilder(DivisionOrg.class);
            divisionOrgQueryBuilder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "parent", "parent", customerOrgs));
            divisionOrgQueryBuilder.setLimit(threshold);
            results.addAll(persistenceService.findAll(divisionOrgQueryBuilder));
        }

        return results;
    }

    private List<BaseOrg> findOrgsOfClass(List<? extends BaseOrg> orgs, final Class filterClass) {
        return new ArrayList<BaseOrg>(Collections2.filter(orgs, new Predicate<BaseOrg>() {
            @Override
            public boolean apply(BaseOrg input) {
                return input.getClass().equals(filterClass);
            }
        }));
    }

    private List<? extends BaseOrg> findParentsLike(OrgQueryParser orgQueryParser) {
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

    public OrgLocationTree getOrgLocationTree(String search) {
        OrgLocationTree result = getOrgTree(search);
        QueryBuilder locQuery = createUserSecurityBuilder(PredefinedLocation.class);
        result.addPredefinedLocations(persistenceService.findAll(locQuery));
        return result;
    }

    public OrgLocationTree getOrgTree(String search) {
        if (StringUtils.isBlank(search)) {
            return getTopLevelOrgTree();
        }
        OrgLocationTree result = new OrgLocationTree().withFilter(search);
        QueryBuilder<BaseOrg> orgQuery = createUserSecurityBuilder(BaseOrg.class);
        orgQuery.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.LIKE,"name", search.trim(), WhereParameter.WILDCARD_BOTH, WhereClause.ChainOp.AND));
        result.addOrgs(persistenceService.findAll(orgQuery));
        return result;
    }

    public OrgLocationTree getOrgLocationTree(Long parentNodeId, OrgLocationTree.NodeType type) {
        OrgLocationTree result = getOrgTree(parentNodeId,type);
        QueryBuilder locQuery = createUserSecurityBuilder(PredefinedLocation.class);
        locQuery.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "owner_id", "owner.id", parentNodeId, 0, WhereClause.ChainOp.AND));
        result.addPredefinedLocations(persistenceService.findAll(locQuery));
        return result;
    }

    public OrgLocationTree getOrgTree(Long parentNodeId, OrgLocationTree.NodeType type) {
        switch (type) {
            case INTERNAL_ORG:
                QueryBuilder<CustomerOrg> customerQuery = createUserSecurityBuilder(CustomerOrg.class);
                customerQuery.addSimpleWhere("parent.id", parentNodeId);
                return new OrgLocationTree(persistenceService.findAll(customerQuery));
            case CUSTOMER_ORG:
                QueryBuilder<DivisionOrg> divisionQuery = createUserSecurityBuilder(DivisionOrg.class);
                divisionQuery.addSimpleWhere("parent.id", parentNodeId);
                return new OrgLocationTree(persistenceService.findAll(divisionQuery));
            case DIVISION_ORG:
            case LOCATION:
                return new OrgLocationTree(); /*no results ever for this case...locations optionally filled in by caller. */
            default:
                throw new IllegalStateException("illegal type " + type);
        }
    }

    public OrgLocationTree getTopLevelOrgTree() {
        QueryBuilder<InternalOrg> query = createUserSecurityBuilder(InternalOrg.class);
        OrgLocationTree result = new OrgLocationTree(persistenceService.findAll(query));
        return result;
    }
}


