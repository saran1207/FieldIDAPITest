package com.n4systems.fieldid.service.asset;

import com.n4systems.fieldid.service.CrudService;
import com.n4systems.fieldid.service.event.EventTypeRulesService;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
public class AssetStatusService extends CrudService<AssetStatus> {

    @Autowired
    private EventTypeRulesService eventTypeRulesService;

    public AssetStatusService() {
        super(AssetStatus.class);
    }

    public List<AssetStatus> getActiveStatuses() {
		QueryBuilder<AssetStatus> builder = createUserSecurityBuilder(AssetStatus.class);

		builder.addOrder("name")
               .addSimpleWhere("state",
                       Archivable.EntityState.ACTIVE);

        return persistenceService.findAll(builder);
    }

    public List<AssetStatus> getArchivedStatuses() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class, true);

        query.addOrder("name")
             .addSimpleWhere("state",
                             Archivable.EntityState.ARCHIVED);

        return persistenceService.findAll(query);
    }

    /**
     * This method returns the count of ACTIVE AssetStatus entities associated with the user's Tenant.
     *
     * @return A <b>Long</b> value representing the amount of ACTIVE Asset Statuses.
     */
    public Long getActiveStatusCount() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class);

        query.addSimpleWhere("state",
                             Archivable.EntityState.ACTIVE);

        return persistenceService.count(query);
    }

    /**
     * This method returns the count of ARCHIVED AssetStatus entities associated with the user's Tenant.
     *
     * @return A <b>Long</b> value representing the amount of ARCHIVED Asset Statuses.
     */
    public Long getArchivedStatusCount() {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class, true);

        query.addSimpleWhere("state",
                             Archivable.EntityState.ARCHIVED);

        return persistenceService.count(query);
    }

    public AssetStatus update(AssetStatus assetStatus, User modifiedBy) {
        assetStatus.setModifiedBy(modifiedBy);
        assetStatus.setModified(new Date(System.currentTimeMillis()));

        return persistenceService.update(assetStatus);
    }

    public AssetStatus saveAssetStatus(AssetStatus assetStatus, User user) {
        assetStatus.setCreated(new Date(System.currentTimeMillis()));
        assetStatus.setCreatedBy(user);
        assetStatus.setModified(new Date(System.currentTimeMillis()));
        assetStatus.setModifiedBy(user);

        Long id = persistenceService.save(assetStatus);

        return persistenceService.find(AssetStatus.class, id);
    }
    
    public AssetStatus getStatusByName(String name) { 
		QueryBuilder<AssetStatus> builder = createUserSecurityBuilder(AssetStatus.class);

        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        builder.addSimpleWhere("name", name);

        return persistenceService.find(builder);
    }

    public AssetStatus getStatusById(Long id) {
        QueryBuilder<AssetStatus> queryBuilder = createUserSecurityBuilder(AssetStatus.class, true).addSimpleWhere("id", id);
        return persistenceService.find(queryBuilder);
    }

    public void archiveStatus(AssetStatus assetStatus) {
        eventTypeRulesService.deleteRules(assetStatus);
        assetStatus.archiveEntity();
        persistenceService.update(assetStatus);
    }

    public void unarchiveStatus(AssetStatus assetStatus) {
        assetStatus.activateEntity();
        persistenceService.update(assetStatus);
    }

    public boolean exists(String name, Long id) {
        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class);
        query.addWhere(WhereClauseFactory.create("name", name));
        if(id != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "id", id));
        }
        return persistenceService.exists(query);
    }

    /**
     * This method returns a paginated list of AssetStatus entities.
     *
     * @param state - The state of the desired list - ARCHIVED or ACTIVE.
     * @param order - A String value representing the field to order the results by.
     * @param ascending - A boolean value indicating if the sort should be ascending (true) or descending (false).
     * @param first - An <b>int</b> value representing "first."
     * @param count - An <b>int</b> value indicating the maximum amount of values to return in this "page."
     * @return A List populated with AssetStatus entities representing the desired paged List.
     */
    public List<AssetStatus> getPagedAssetStatusByState(Archivable.EntityState state,
                                                        String order,
                                                        boolean ascending,
                                                        int first,
                                                        int count) {

        QueryBuilder<AssetStatus> query = Archivable.EntityState.ACTIVE.equals(state) ?
                createUserSecurityBuilder(AssetStatus.class)
                :
                createUserSecurityBuilder(AssetStatus.class, true);

        query.addSimpleWhere("state", state);

        //The amount of situations where we have to do something weird like this are so limited that I'm not sure
        //there's much value in adding changes to the underlying QueryBuilder class to support it.
        //Since the user can potentially be null for CreatedBy or ModifiedBy, we need a way of including any results
        //with null values after sorting everything else.

        boolean includeNullCreatedBy = false;
        boolean includeNullModifiedBy = false;

        if(order != null) {
            String[] orders = order.split(",");
            for(String oneOrder : orders) {
                if(oneOrder.startsWith("createdBy")) {
                    includeNullCreatedBy = true;
                    oneOrder = oneOrder.replaceAll("createdBy",
                                                   "sortCreatedByJoin");

                    SortTerm term = new SortTerm(oneOrder,
                                                 ascending ? SortDirection.ASC : SortDirection.DESC);
                    term.setAlwaysDropAlias(true);
                    term.setFieldAfterAlias(oneOrder.substring("sortCreatedByJoin".length() + 1));
                    query.getOrderArguments().add(term.toSortField());
                } else if(oneOrder.startsWith("modifiedBy")) {
                    includeNullModifiedBy = true;
                    oneOrder = oneOrder.replaceAll("modifiedBy",
                                                   "sortModifiedByJoin");

                    SortTerm term = new SortTerm(oneOrder,
                                                 ascending ? SortDirection.ASC : SortDirection.DESC);
                    term.setAlwaysDropAlias(true);
                    term.setFieldAfterAlias(oneOrder.substring("sortModifiedByJoin".length() + 1));
                    query.getOrderArguments().add(term.toSortField());
                } else {
                    query.addOrder(oneOrder, ascending);
                }
            }
        }

        if(includeNullCreatedBy) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT,
                                         "createdBy",
                                         "sortCreatedByJoin",
                                         true));
        }

        if(includeNullModifiedBy) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT,
                                         "modifiedBy",
                                         "sortModifiedByJoin",
                                         true));
        }

        return persistenceService.findAllPaginated(query,
                                                   first,
                                                   count);
    }


    public Long getAssetStatusCountByState(Archivable.EntityState state) {
        if(Archivable.EntityState.ACTIVE.equals(state)) {
            return getActiveStatusCount();
        } else {
            return getArchivedStatusCount();
        }
    }

    /**
     * This method returns a paginated list of Active AssetStatus entities.
     *
     * @param order - A String value representing the field to order the results by.
     * @param ascending - A boolean value indicating if the sort should be ascending (true) or descending (false).
     * @param first - An <b>int</b> value representing "first."
     * @param count - An <b>int</b> value indicating the maximum amount of values to return in this "page."
     * @return A List populated with AssetStatus entities representing the desired paged List.
     */
    public List<AssetStatus> getPagedActiveList(String order,
                                                boolean ascending,
                                                int first,
                                                int count) {

        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class);

        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);

        if(order != null) {
            query.addOrder(order,
                    ascending);
        }

        return persistenceService.findAllPaginated(query,
                first,
                count);
    }

    /**
     * This method returns a paginated list of Archived AssetStatus entities.
     *
     * @param order - A String value representing the field to order the results by.
     * @param ascending - A boolean value indicating if the sort should be ascending (true) or descending (false).
     * @param first - An <b>int</b> value representing "first."
     * @param count - An <b>int</b> value indicating the maximum amount of values to return in this "page."
     * @return A List populated with AssetStatus entities representing the desired paged List.
     */
    public List<AssetStatus> getPagedArchivedList(String order,
                                                  boolean ascending,
                                                  int first,
                                                  int count) {

        QueryBuilder<AssetStatus> query = createUserSecurityBuilder(AssetStatus.class, true);

        query.addSimpleWhere("state", Archivable.EntityState.ARCHIVED);

        if(order != null) {
            query.addOrder(order,
                    ascending);
        }

        return persistenceService.findAllPaginated(query,
                first,
                count);
    }
}
