package com.n4systems.fieldid.service.org;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.context.ThreadLocalInteractionContext;
import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.user.UserListFilterCriteria;
import com.n4systems.model.builders.CriteriaBuilder;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.orgs.*;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.security.OwnerAndDownWithPrimaryFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserGroup;
import com.n4systems.model.user.UserQueryHelper;
import com.n4systems.security.UserType;
import com.n4systems.services.config.ConfigService;
import com.n4systems.util.UserBelongsToFilter;
import com.n4systems.util.collections.OrgList;
import com.n4systems.util.persistence.*;
import net.sf.ehcache.search.Query;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class OrgService extends CrudService<BaseOrg> {

	public OrgService() {
		super(BaseOrg.class);
	}

    private static String [] DEFAULT_ORDER = {"name"};

    public List<Long> getIdOfAllVisibleOrgs() {
        //If this doesn't work, we actually have to individually grab all visible:
        // - PrimaryOrg
        // - InternalOrg
        // - DivisionOrg
        // - CustomerOrg
        QueryBuilder<Long> query = new QueryBuilder<>(BaseOrg.class, securityContext.getUserSecurityFilter());

        query.setSelectArgument(new NewObjectSelect(Long.class, "id"));

        return persistenceService.findAll(query);
    }

    public List<OrgIdTree> getIdVisibleOrgsIdTree() {

        QueryBuilder<BaseOrg> query = new QueryBuilder<>(BaseOrg.class, securityContext.getUserSecurityFilter());

        //The Final Tree
        List<OrgIdTree> tree = new ArrayList<>();

        //List of all visible Orgs
        List<BaseOrg> visibleOrgs = persistenceService.findAll(query);

        //Let's get the Root of the tree for what the user can see
        BaseOrg root = securityContext.getUserSecurityFilter().getOwner();

        //Let's create the root of Final Tree
        OrgIdTree treeRoot = new OrgIdTree();
        treeRoot.setId(root.getId());

        //Add the root to the list
        tree.add(treeRoot);

        if(root.isPrimary()) {
            //Build the multiple headed tree
            for(BaseOrg org:visibleOrgs) {
                if(org.getParent() != null && org.getParent().equals(root)) {
                    //Add it to the children of the Primary head
                    if(org.isCustomer()) {
                        OrgIdTree child = new OrgIdTree();
                        child.setId(org.getId());

                        treeRoot.getChildren().add(child);
                    }
                    //Add it as the extra heads (Secondary's) to the tree
                    if(org.isSecondary()) {
                        OrgIdTree secondary = new OrgIdTree();
                        secondary.setId(org.getId());

                        tree.add(secondary);
                    }
                }
            }

            //Build the children for the Secondary Org heads
            for(OrgIdTree head:tree) {
                if(head.getId()!=treeRoot.getId()) {
                    for(BaseOrg orgs:visibleOrgs) {
                        if(orgs.getParent() != null && orgs.isCustomer() && orgs.getParent().getId().equals(head.getId())) {
                            OrgIdTree child = new OrgIdTree();
                            child.setId(orgs.getId());

                            head.getChildren().add(child);
                        }
                    }
                }
            }

        } else if(root.isSecondary()) {
            //Build the single headed tree

            for(BaseOrg orgs:visibleOrgs) {
                if(orgs.getParent() != null && orgs.isCustomer() && orgs.getParent().getId().equals(treeRoot.getId())) {
                    OrgIdTree child = new OrgIdTree();
                    child.setId(orgs.getId());

                    treeRoot.getChildren().add(child);
                }
            }
        } else if(root.isCustomer()) {
            //Build the single node tree

            //the tree is the root
        } else if(root.isDivision()) {
            //Created a seperate else statement if we ever want to change the division to be handled differently
            //Single node (the parent of the division)

            //the tree is the root
        }

        return tree;
    }

    @Transactional(readOnly = true)
    public List<PrimaryOrg> getActivePrimaryOrgs() {
        QueryBuilder<PrimaryOrg> query =  new QueryBuilder<>(PrimaryOrg.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("tenant.disabled", false));
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    @Transactional(readOnly = true)
    public List<SecondaryOrg> getSecondaryOrgs() {
        QueryBuilder<SecondaryOrg> query =  new QueryBuilder<>(SecondaryOrg.class, new OpenSecurityFilter());
        query.addWhere(WhereClauseFactory.create("tenant.disabled", false));
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    @Transactional(readOnly = true)
    public SecondaryOrg getSecondaryOrg(Long id) {
        QueryBuilder<SecondaryOrg> builder =  createSecondaryOrgSecurityBuilder(SecondaryOrg.class);
        builder.addSimpleWhere("id", id);
        builder.addSimpleWhere("tenant.disabled", false);
        return persistenceService.find(builder);
    }

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
    public boolean secondaryNameExists(BaseOrg org, String name) {
        QueryBuilder<SecondaryOrg> query = createUserSecurityBuilder(SecondaryOrg.class);
        query.addSimpleWhere("primaryOrg", org);
        query.addSimpleWhere("name", name.trim());

        List<SecondaryOrg> resultSet = persistenceService.findAll(query);

        if(resultSet.size() > 0){
            return true;
        } else {
            return false;
        }
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

    /**
     * Get primary org for tenant. This method is intended to be used when there may not be
     * a FieldId session setup (such as in authenticating an incoming SSO request).
     * @param tenantId
     * @return
     */
    @Transactional(readOnly = true)
    public PrimaryOrg getPrimaryOrgForTenantNoSecurityFilter(Long tenantId) {
        QueryBuilder<PrimaryOrg> query =  new QueryBuilder<>(PrimaryOrg.class, new OpenSecurityFilter());
        query.addSimpleWhere("tenant.id", tenantId);
        return persistenceService.find(query);
    }

    public List<BaseOrg> getVisibleOrgs() {
        QueryBuilder<BaseOrg> query = createUserSecurityBuilder(BaseOrg.class);
        query.applyFilter(new OwnerAndDownFilter(getCurrentUser().getOwner()));
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public List<BaseOrg> getOwnerAndDownOrgs(BaseOrg currentOrg) {
        QueryBuilder<BaseOrg> query = createUserSecurityBuilder(BaseOrg.class);
        query.applyFilter(new OwnerAndDownFilter(currentOrg));
        return persistenceService.findAll(query);
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

    public OrgLocationTree getLocationTree(BaseOrg locationOwner, String search) {
        OrgLocationTree result = new OrgLocationTree().withFilter(search);

        QueryBuilder locQuery = createUserSecurityBuilder(PredefinedLocation.class);

        if(locationOwner != null) {
            locQuery.applyFilter(new OwnerAndDownWithPrimaryFilter(locationOwner));
        }

        if (StringUtils.isBlank(search)) {
            locQuery.addWhere(new WhereParameter<PredefinedLocation>(WhereParameter.Comparator.NULL, "parent"));
            List<PredefinedLocation> locations = persistenceService.findAll(locQuery);
            return new OrgLocationTree(locations, true);
        }

        result.addPredefinedLocations(persistenceService.findAll(locQuery));

        return result;
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
        OrgLocationTree result = getOrgTree(parentNodeId, type);
        QueryBuilder locQuery = createUserSecurityBuilder(PredefinedLocation.class);
        locQuery.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "owner_id", "owner.id", parentNodeId, 0, WhereClause.ChainOp.AND));
        result.addPredefinedLocations(persistenceService.findAll(locQuery));
        return result;
    }

    public OrgLocationTree getLocationTree(BaseOrg locationOwner, Long parentNodeId, OrgLocationTree.NodeType type) {
        OrgLocationTree result = getOrgTree(parentNodeId, type, locationOwner);
        QueryBuilder locQuery = createUserSecurityBuilder(PredefinedLocation.class);
        locQuery.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.EQ, "owner_id", "owner.id", parentNodeId, 0, WhereClause.ChainOp.AND));
        result.addPredefinedLocations(persistenceService.findAll(locQuery));
        return result;
    }

    public OrgLocationTree getOrgTree(Long parentNodeId, OrgLocationTree.NodeType type, BaseOrg locationOwner) {
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
                QueryBuilder<PredefinedLocation> locationQuery = createUserSecurityBuilder(PredefinedLocation.class);
                locationQuery.addSimpleWhere("parent.id", parentNodeId);
                if(locationOwner != null) {
                    locationQuery.applyFilter(new OwnerAndDownWithPrimaryFilter(locationOwner));
                }
                return new OrgLocationTree(persistenceService.findAll(locationQuery), true);
            default:
                throw new IllegalStateException("illegal type " + type);
        }
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
                QueryBuilder<PredefinedLocation> locationQuery = createUserSecurityBuilder(PredefinedLocation.class);
                locationQuery.addSimpleWhere("parent.id", parentNodeId);
                return new OrgLocationTree(persistenceService.findAll(locationQuery), true);
            default:
                throw new IllegalStateException("illegal type " + type);
        }
    }

    public OrgLocationTree getTopLevelOrgTree() {
        QueryBuilder<InternalOrg> query = createUserSecurityBuilder(InternalOrg.class);
        List<InternalOrg> orgs = persistenceService.findAll(query);
        return orgs.isEmpty() ?
            new OrgLocationTree(Lists.newArrayList(getCurrentUser().getOwner())) :
            new OrgLocationTree(orgs);
    }

    public List<? extends BaseOrg> search(String textFilter, Class<? extends BaseOrg> typeFilter, int page, int pageSize) {
        QueryBuilder<? extends BaseOrg> builder = createSearchQueryBuilder(textFilter, typeFilter);
        return persistenceService.findAll(builder, page, pageSize);
    }

    public Long getSearchCount(String textFilter, Class<? extends BaseOrg> typeFilter) {
        QueryBuilder<? extends BaseOrg> builder = createSearchQueryBuilder(textFilter,typeFilter);
        return persistenceService.count(builder);
    }

    private QueryBuilder<? extends BaseOrg> createSearchQueryBuilder(String textFilter,Class<? extends BaseOrg> typeFilter) {
        Class<? extends BaseOrg> type = typeFilter==null ? BaseOrg.class : typeFilter;
        QueryBuilder<? extends BaseOrg> builder = createUserSecurityBuilder(type);
        builder.addWhere(WhereParameter.Comparator.LIKE, "name", "name", textFilter, WhereParameter.WILDCARD_BOTH | WhereParameter.TRIM);
        builder.setOrder("name",true);
        return builder;
    }

    public List<? extends SecondaryOrg> searchSecondaryOrg(String textFilter, Class<? extends SecondaryOrg> typeFilter, int page, int pageSize) {
        QueryBuilder<? extends SecondaryOrg> builder = createSearchQueryBuilderSecondaryOrg(textFilter, typeFilter);
        return persistenceService.findAll(builder, page, pageSize);
    }

    private QueryBuilder<? extends SecondaryOrg> createSearchQueryBuilderSecondaryOrg(String textFilter,Class<? extends SecondaryOrg> typeFilter) {
        Class<? extends SecondaryOrg> type = typeFilter==null ? SecondaryOrg.class : typeFilter;
        QueryBuilder<? extends SecondaryOrg> builder = createUserSecurityBuilder(type);
        builder.addWhere(WhereParameter.Comparator.LIKE, "name", "name", textFilter, WhereParameter.WILDCARD_BOTH | WhereParameter.TRIM);
        builder.setOrder("name",true);
        return builder;
    }

    public Long getSearchSecondaryOrgCount(String textFilter, Class<? extends SecondaryOrg> typeFilter) {
        QueryBuilder<? extends SecondaryOrg> builder = createSearchQueryBuilderSecondaryOrg(textFilter,typeFilter);
        return persistenceService.count(builder);
    }

    public List<SecondaryOrg> getSecondaryOrgs(OrgListFilterCriteria criteria, int page, int pageSize) {
        QueryBuilder<SecondaryOrg> builder = createOrgQueryBuilder(criteria);

        return persistenceService.findAll(builder, page, pageSize);
    }

    public Long countSecondaryOrgs(OrgListFilterCriteria criteria) {
        QueryBuilder<SecondaryOrg> builder = createOrgQueryBuilder(criteria);

        return persistenceService.count(builder);
    }

    private QueryBuilder<SecondaryOrg> createOrgQueryBuilder(OrgListFilterCriteria criteria) {
        QueryBuilder<SecondaryOrg> builder = createSecondaryOrgSecurityBuilder(SecondaryOrg.class, criteria.isArchivedOnly());

        if (criteria.getOwner() != null) {
            builder.addSimpleWhere("owner", criteria.getOwner());
        }

        if (criteria.getCustomer() != null) {
            builder.addSimpleWhere("owner.customerOrg", criteria.getCustomer());
        }

        if(criteria.isFilterOnPrimaryOrg()) {
            builder.addWhere(WhereClauseFactory.createIsNull("owner.secondaryOrg"));
        }

        if(criteria.isFilterOnSecondaryOrg()) {
            builder.addSimpleWhere("owner.secondaryOrg", criteria.getOrgFilter());
        }

        if (criteria.getNameFilter() != null && !criteria.getNameFilter().isEmpty()) {
            WhereParameterGroup whereGroup = new WhereParameterGroup();
            whereGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "userID", criteria.getNameFilter(), WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
                    WhereClause.ChainOp.OR));
            whereGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "firstName", criteria.getNameFilter(), WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
                    WhereClause.ChainOp.OR));
            whereGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "lastName", criteria.getNameFilter(), WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
                    WhereClause.ChainOp.OR));
            builder.addWhere(whereGroup);
        }

        if (criteria.isArchivedOnly()) {
            OrgQueryHelper.applyArchivedFilter(builder);
        }

        if(criteria.getOrder() != null && !criteria.getOrder().isEmpty()) {
            if(criteria.getOrder().startsWith("owner")) {
                builder.addJoin(new JoinClause(JoinClause.JoinType.LEFT, criteria.getOrder(), "sort", true));

                OrderClause orderClause1 = new OrderClause("sort", criteria.isAscending());
                orderClause1.setAlwaysDropAlias(true);

                builder.getOrderArguments().add(orderClause1);
                builder.getOrderArguments().add(new OrderClause("id", criteria.isAscending()));
            } else
                for (String subOrder : criteria.getOrder().split(",")) {
                    builder.addOrder(subOrder.trim(), criteria.isAscending());
                }
        } else {
            for (String order : DEFAULT_ORDER) {
                builder.setOrder(order, criteria.isAscending());
            }
        }
        return builder;
    }

    public void archive(SecondaryOrg secondaryOrg) {
        secondaryOrg.archiveEntity();
        persistenceService.update(secondaryOrg);
    }

    public void unarchive(SecondaryOrg secondaryOrg) {
        secondaryOrg.activateEntity();
        persistenceService.update(secondaryOrg);
    }

    public void create(SecondaryOrg secondaryOrg) {
        persistenceService.save(secondaryOrg);
    }

}


