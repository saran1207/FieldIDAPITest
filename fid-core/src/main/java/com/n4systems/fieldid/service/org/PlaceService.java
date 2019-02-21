package com.n4systems.fieldid.service.org;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.amazon.S3Service;
import com.n4systems.model.Asset;
import com.n4systems.model.PlaceEvent;
import com.n4systems.model.PlaceEventType;
import com.n4systems.model.WorkflowState;
import com.n4systems.model.orgs.*;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserQueryHelper;
import com.n4systems.security.UserType;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import com.n4systems.util.time.MethodTimer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
public class PlaceService extends FieldIdPersistenceService {

    @Autowired private OrgService orgService;
    @Autowired private S3Service s3Service;

    /**
     * NOTE THAT ALL METHODS IN THIS SERVICE ARE JUST PLACEHOLDERS FOR 2013.8!!!!
     *
     * these need to be properly implemented and possibly moved into other services before Places feature is complete.
     * just putting stuff in here now to make short term merging easier.
     * (most likely you'll want to merge this and OrgService???)
     */

    public List<PlaceEventType> getAllEventTypes() {
        QueryBuilder<PlaceEventType> query = createUserSecurityBuilder(PlaceEventType.class);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public List<? extends User> getUsersFor(BaseOrg org) {
        QueryBuilder<User> query = createUserSecurityBuilder(User.class);
        query.addSimpleWhere("owner", org);
        return persistenceService.findAll(query);
    }

    public List<PlaceEvent> getEventsFor(BaseOrg org) {
        return getEventsFor(org, null, false, new ArrayList<WorkflowState>());
    }

    public List<PlaceEvent> getEventsFor(BaseOrg org, String order, boolean ascending, List<WorkflowState> workflowStates, int first, int count) {
        QueryBuilder<PlaceEvent> query = createUserSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("place", org);

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoin = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("performedBy")) {
                    subOrder = subOrder.replaceAll("performedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoin = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoin) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "performedBy", "sortJoin", true));
        }

        if(workflowStates!= null && !workflowStates.isEmpty()) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "workflowStatesList", "workflowState", workflowStates));
        }

        return persistenceService.findAllPaginated(query, first, count);
    }

    @Deprecated
    public List<PlaceEvent> getEventsFor(BaseOrg org, String order, boolean ascending, List<WorkflowState> workflowStates) {
        QueryBuilder<PlaceEvent> query = createUserSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("place", org);

        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                query.addOrder(subOrder, ascending);
            }
        }

        if(workflowStates!= null && !workflowStates.isEmpty()) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.IN, "workflowStatesList", "workflowState", workflowStates));
        }

        return persistenceService.findAll(query);
    }

    public List<PlaceEvent> getOpenEventsFor(BaseOrg org) {
        QueryBuilder<PlaceEvent> query = createUserSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("place",org);
        query.setOrder("dueDate",true);
        query.addSimpleWhere("workflowState", WorkflowState.OPEN);
        List<PlaceEvent> events = persistenceService.findAll(query);
        return events;
    }

    public List<PlaceEvent> getOpenEventsFor(Long org) {
        QueryBuilder<PlaceEvent> query = createUserSecurityBuilder(PlaceEvent.class);
        query.addSimpleWhere("place.id",org);
        query.setOrder("dueDate",true);
        query.addSimpleWhere("workflowState", WorkflowState.OPEN);
        List<PlaceEvent> events = persistenceService.findAll(query);
        return events;
    }

    public List<PlaceEvent> getOpenEventsFor(BaseOrg org, int days) {
        return getOpenEventsFor(org);
    }

    public int countEventsFor(BaseOrg org, List<WorkflowState> workflowStates) {
        return getEventsFor(org, null, false, workflowStates).size();
    }

    public void archive(BaseOrg org) {
        Set<BaseOrg> orgsToUpdate = archiveOrgs(org);
        persistenceService.update(Lists.newArrayList(orgsToUpdate));
        removeOrgsFromOfflineProfiles(orgsToUpdate);
    }

    private void removeOrgsFromOfflineProfiles(Set<BaseOrg> orgsToUpdate) {
       String orgIdList = orgsToUpdate.stream().filter(org -> !org.isPrimary()).map(org -> String.valueOf(org.getId())).collect(Collectors.joining(", "));

       String queryString = "DELETE FROM offline_profiles_orgs WHERE organizations IN (" + orgIdList + ")";

       getEntityManager().createNativeQuery(queryString).executeUpdate();
    }

    public void unarchive(BaseOrg org) {
        org.activateEntity();
        persistenceService.update(org);
    }

    private Set<BaseOrg> archiveOrgs(BaseOrg... orgs) {
        Set<BaseOrg> orgsToUpdate = archiveOrgs(new HashSet<BaseOrg>(), Lists.newArrayList(orgs));
        //We need to update the modifed on the parent so that mobile will know to sync down the changes.
        for (BaseOrg org:orgs) {
            BaseOrg parent = org.getParent();
            if (parent != null) {
                parent.touch();
                orgsToUpdate.add(parent);
            }
        }

        return orgsToUpdate;
    }

    private Set<BaseOrg> archiveOrgs(Set<BaseOrg> orgsToUpdate, List<? extends BaseOrg> orgs) {
        for (BaseOrg org:orgs) {
            List<User> users = getUsers(org);
            for (User user : users) {
                user.archiveUser();
            }
            persistenceService.update(users);

            archiveOrgs(orgsToUpdate, getChildOrgs(org));

            org.archiveEntity();
            orgsToUpdate.add(org);
        }
        return orgsToUpdate;
    }

    private List<? extends BaseOrg> getChildOrgs(BaseOrg org) {
        if (org instanceof SecondaryOrg) {
            QueryBuilder<CustomerOrg> builder = createUserSecurityBuilder(CustomerOrg.class);
            builder.addSimpleWhere("parent.id", org.getId());
            return persistenceService.findAll(builder);
        } else if (org instanceof CustomerOrg) {
            QueryBuilder<DivisionOrg> builder = createUserSecurityBuilder(DivisionOrg.class);
            builder.addSimpleWhere("parent.id", org.getId());
            return persistenceService.findAll(builder);
        } else if (org instanceof DivisionOrg) {
            return Lists.newArrayList();
        } else {
            throw new IllegalStateException("can't archive orgs of type " + org.getClass().getSimpleName());
        }
    }

    protected List<User> getUsers(BaseOrg org) {
        QueryBuilder<User> query = createUserSecurityBuilder(User.class);
        UserQueryHelper.applyFullyActiveFilter(query);
        query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "userType", UserType.SYSTEM));
        query.addSimpleWhere("owner", org);
        query.addOrder("firstName", "lastName");
        return persistenceService.findAll(query);
    }

    public Long getAssetCount(Long orgId) {
        QueryBuilder<Asset> query = createTenantSecurityBuilder(Asset.class);
        query.addSimpleWhere("owner.id", orgId);

        return persistenceService.count(query);
    }

    public List<PlaceEventType> getEventTypes() {
        QueryBuilder<PlaceEventType> query = createTenantSecurityBuilder(PlaceEventType.class);
        return persistenceService.findAll(query);
    }

    public Long countDescendants(BaseOrg org) {
        Preconditions.checkNotNull(org);
        QueryBuilder query = getDescendantsQuery(org);
        return persistenceService.count(query);
    }

    private QueryBuilder getDescendantsQuery(BaseOrg org) {
        QueryBuilder query;
        if (org.isPrimary()) {
            query = createTenantSecurityBuilder(PrimaryOrgChildren.class);
        } else if (org.isSecondary()) {
            query = createTenantSecurityBuilder(CustomerOrg.class);
        } else if (org.isCustomer()) {
            query = createTenantSecurityBuilder(DivisionOrg.class);
        } else {
            throw new IllegalStateException("can't get children for division " + org);
        }
        query.addSimpleWhere("parent", org);
        return query;
    }

    // TODO DD :     @Cacheable("descendants")
    public List<BaseOrg> getDescendants(BaseOrg org, int page, int pageSize) {
        List<BaseOrg> result = Lists.newArrayList();
        MethodTimer methodTimer = new MethodTimer().start();
        Preconditions.checkNotNull(org);
        QueryBuilder query = getDescendantsQuery(org);
        if (query.getFromArgument().getTableClass().equals(PrimaryOrgChildren.class)) {
            query.addOrder("org.name");
            List<PrimaryOrgChildren> children = persistenceService.findAllPaginated(query, page, pageSize);
            for (PrimaryOrgChildren child:children){
                result.add(child.getOrg());
            }
        } else {
            query.addOrder("name");
            result = persistenceService.findAllPaginated(query, page, pageSize);
        }
        methodTimer.stop();
        return result;
    }
}
