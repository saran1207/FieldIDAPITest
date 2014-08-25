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
import com.n4systems.util.persistence.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    public EventTypeGroup create(EventTypeGroup group, User user, Tenant tenant) {
        group.setModified(new Date());
        group.setModifiedBy(user);
        group.setTenant(tenant);
        eventTypeService.touchEventTypesForGroup(group.getId(), user);
        return persistenceService.update(group);
    }

    public boolean exists(String name) {
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class, true);
        query.addSimpleWhere("name", name);
        return (persistenceService.count(query) > 0);
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

        for (String subOrder : order.split(",")) {
            query.addOrder(subOrder.trim(), ascending);
        }

        return persistenceService.findAllPaginated(query, first, count);

    }

    public Long getEventTypeGroupsByStateCount(Archivable.EntityState state){
        QueryBuilder<EventTypeGroup> query = createUserSecurityBuilder(EventTypeGroup.class, true);
        query.addSimpleWhere("state", state);
        return persistenceService.count(query);
    }

    @Transactional
    public List<PrintOut> findCertPrintOuts(){
        return findPrintOutsOfType(PrintOut.PrintOutType.CERT);
    }

    @Transactional
    public List<PrintOut> findObservationPrintOuts(){
        return findPrintOutsOfType(PrintOut.PrintOutType.OBSERVATION);
    }

    @Transactional
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
