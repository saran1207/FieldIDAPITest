package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventType;
import com.n4systems.model.EventTypeGroup;
import com.n4systems.model.PrintOut;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.tenant.TenantCreationService;
import com.n4systems.util.persistence.JoinClause;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class EventTypeGroupService extends FieldIdPersistenceService {

	@Autowired private EventTypeService eventTypeService;

    public List<EventTypeGroup> getAllEventTypeGroups() {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class);
        query.addOrder("name");
        return persistenceService.findAll(query);
    }

    public EventTypeGroup getEventTypeGroupById(Long id) {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class);
        query.addSimpleWhere("id", id);
        return persistenceService.find(query);
    }
    
    public Long getNumberOfAssociatedEventTypes(EventTypeGroup group) {
        QueryBuilder<EventType> eventTypeCountQuery = createUserSecurityBuilder(EventType.class);
        eventTypeCountQuery.addSimpleWhere("group", group);
        return persistenceService.count(eventTypeCountQuery);
    }

    public EventTypeGroup update(EventTypeGroup group, User user) {
        group.setModified(new Date());
        group.setModifiedBy(user);
		eventTypeService.touchEventTypesForGroup(group.getId(), user);
        return persistenceService.update(group);
    }

    public void create(EventTypeGroup group, User user, Tenant tenant) {
        group.setModified(new Date());
        group.setModifiedBy(user);
        group.setTenant(tenant);
        eventTypeService.touchEventTypesForGroup(group.getId(), user);
        persistenceService.update(group);
    }

    public void archive(EventTypeGroup group, User user) {
        group.archiveEntity();
        update(group, user);
    }

    public User getUser(Long id){
        return persistenceService.find(User.class, id);
    }

    public EventTypeGroup getDefaultActionGroup() {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class);
        query.addSimpleWhere("action", true);
        // CAVEAT : assumes tenant_id/name=Actions is unique.
        query.addSimpleWhere("name", TenantCreationService.DEFAULT_ACTIONS_GROUP_NAME);
        return persistenceService.find(query);
    }

    public List<EventTypeGroup> getEventTypeGroupsByState(Archivable.EntityState state, String order, boolean ascending, int first, int count) {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class, true);
        query.addSimpleWhere("state", state);

        // "performedBy.fullName"...split('.')  a.b  pb.name....order by a, order by a.b
        // HACK : we need to do a *special* order by when chaining attributes together when the parent might be null.
        // so if we order by performedBy.firstName we need to add this NULLS LAST clause otherwise events with null performedBy values
        // will not be returned in the result list.
        // this should be handled more elegantly in the future but i'm fixing at the last second.
        boolean needsSortJoinForCreated = false;
        boolean needsSortJoinForModified = false;
        if (order != null) {
            String[] orders = order.split(",");
            for (String subOrder : orders) {
                if (subOrder.startsWith("createdBy")) {
                    subOrder = subOrder.replaceAll("createdBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoinForCreated = true;
                } else if (subOrder.startsWith("modifiedBy")) {
                    subOrder = subOrder.replaceAll("modifiedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, ascending ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    query.getOrderArguments().add(sortTerm.toSortField());
                    needsSortJoinForModified = true;
                } else {
                    query.addOrder(subOrder, ascending);
                }
            }
        }

        if (needsSortJoinForCreated) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "createdBy", "sortJoin", true));
        } else if (needsSortJoinForModified) {
            query.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "modifiedBy", "sortJoin", true));
        }

        return persistenceService.findAllPaginated(query, first, count);

    }

    public Long getEventTypeGroupsByStateCount(Archivable.EntityState state){
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class, true);
        query.addSimpleWhere("state", state);
        return persistenceService.count(query);
    }

    public List<PrintOut> findCertPrintOuts(){
        return findPrintOutsOfType(PrintOut.PrintOutType.CERT);
    }

    public List<PrintOut> findObservationPrintOuts(){
        return findPrintOutsOfType(PrintOut.PrintOutType.OBSERVATION);
    }

    private List<PrintOut> findPrintOutsOfType(PrintOut.PrintOutType type) {
        List<PrintOut> printOuts = null;

        QueryBuilder<PrintOut> queryBuilder = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("type", type);
        queryBuilder.addSimpleWhere("custom", false);
        queryBuilder.addOrder("name");
        printOuts = persistenceService.findAll(queryBuilder);

        QueryBuilder<PrintOut>queryBuilderCustomPrintOuts = createUserSecurityBuilder(PrintOut.class);
        queryBuilderCustomPrintOuts.addSimpleWhere("type", type);
        queryBuilderCustomPrintOuts.addSimpleWhere("custom", true);
        queryBuilder.addOrder("name");
        printOuts.addAll(persistenceService.findAll(queryBuilderCustomPrintOuts));

        return printOuts;
    }

    public EventTypeGroup getNewEventTypeGroup() {
        EventTypeGroup eventTypeGroup = new EventTypeGroup();
        eventTypeGroup.setPrintOut(getDefaultCertPrintOut());
        eventTypeGroup.setObservationPrintOut(null);

        return eventTypeGroup;
    }


    private PrintOut getDefaultCertPrintOut() {
        QueryBuilder<PrintOut> queryBuilder = new QueryBuilder<PrintOut>(PrintOut.class, new OpenSecurityFilter());
        queryBuilder.addSimpleWhere("name", "2 Column with Observations");
        queryBuilder.addSimpleWhere("custom", false);
        return persistenceService.find(queryBuilder);
    }

}
