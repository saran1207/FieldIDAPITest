package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.fieldid.service.eventbook.EventBookListFilterCriteria;
import com.n4systems.model.EventBook;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;

import java.util.Date;
import java.util.List;

public class EventBookService extends FieldIdPersistenceService {

    private static String [] DEFAULT_ORDER = {"firstName", "lastName"};

    public List<EventBook> getAllEventBooks() {
        return persistenceService.findAll(EventBook.class);
    }

    public List<EventBook> getActiveEventBooks() {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class);

        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    public List<EventBook> getArchivedEventBooks() {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class, true);

        query.addOrder("name");

        return persistenceService.findAll(query);
    }

    public Long getActiveEventBookCount() {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class);

        query.addSimpleWhere("state",
                             Archivable.EntityState.ACTIVE);

        return persistenceService.count(query);
    }

    public Long getArchivedEventBookCount() {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class, true);

        query.addSimpleWhere("state",
                Archivable.EntityState.ARCHIVED);

        return persistenceService.count(query);
    }

    public EventBook updateEventBook(EventBook eventBook, User user) {
        eventBook.setModified(new Date(System.currentTimeMillis()));
        eventBook.setModifiedBy(user);

        return persistenceService.update(eventBook);
    }

    public EventBook saveEventBook(EventBook eventBook, User user) {
        eventBook.setCreated(new Date(System.currentTimeMillis()));
        eventBook.setModified(new Date(System.currentTimeMillis()));
        eventBook.setCreatedBy(user);
        eventBook.setModifiedBy(user);

        Long id = persistenceService.save(eventBook);

        return persistenceService.find(EventBook.class, id);
    }

    public EventBook getEventBookByName(String name) {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class);

        query.addSimpleWhere("state", Archivable.EntityState.ACTIVE);
        query.addSimpleWhere("name", name);

        return persistenceService.find(query);
    }

    public EventBook getEventBookById(Long id) {
        QueryBuilder<EventBook> queryBuilder = createUserSecurityBuilder(EventBook.class, true).addSimpleWhere("id", id);
        return persistenceService.find(queryBuilder);
    }

    public void archiveEventBook(EventBook eventBook) {
        eventBook.archiveEntity();
        persistenceService.update(eventBook);
    }

    public void unarchiveStatus(EventBook eventBook) {
        eventBook.activateEntity();
        persistenceService.update(eventBook);
    }

    public void openEventBook(EventBook eventBook) {
        eventBook.setOpen(true);
        persistenceService.update(eventBook);
    }

    public void closeEventBook(EventBook eventBook) {
        eventBook.setOpen(false);
        persistenceService.update(eventBook);
    }

    public boolean exists(String name, Long id) {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class);

        query.addSimpleWhere("name", name);
        if(id != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "id", id));
        }

        return persistenceService.exists(query);
    }

    public Long getEventBooksCountByState(EventBookListFilterCriteria criteria, Archivable.EntityState state) {
        if(Archivable.EntityState.ACTIVE.equals(state)) {
            return getActiveEventBookCountByCriteria(criteria);
        } else {
            return getArchivedEventBookCountByCriteria(criteria);
        }
    }

    public Long getActiveEventBookCountByCriteria(EventBookListFilterCriteria criteria) {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class);
        applyFilter(query, criteria);
        query.addSimpleWhere("state",
                Archivable.EntityState.ACTIVE);

        return persistenceService.count(query);
    }

    public Long getArchivedEventBookCountByCriteria(EventBookListFilterCriteria criteria) {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class, true);
        applyFilter(query, criteria);
        query.addSimpleWhere("state",
                Archivable.EntityState.ARCHIVED);

        return persistenceService.count(query);
    }

    private void applyFilter(QueryBuilder<EventBook> builder, EventBookListFilterCriteria criteria) {

        if (criteria.getOwner() != null) {
            builder.applyFilter(new OwnerAndDownFilter(criteria.getOwner()));
        }

        if (criteria.getTitleFilter() != null && !criteria.getTitleFilter().isEmpty()) {
            WhereParameterGroup whereGroup = new WhereParameterGroup();
            whereGroup.addClause(WhereClauseFactory.create(WhereParameter.Comparator.LIKE, "name", criteria.getTitleFilter(), WhereParameter.IGNORE_CASE | WhereParameter.WILDCARD_BOTH,
                    WhereClause.ChainOp.OR));
            builder.addWhere(whereGroup);
        }

        boolean needsCreatedBySortJoin = false;
        boolean needsModifiedBySortJoin = false;
        if (criteria.getOrder() != null) {
            String[] orders = criteria.getOrder().split(",");
            for (String subOrder : orders) {
                subOrder=subOrder.trim();
                if (subOrder.startsWith("createdBy")) {
                    subOrder = subOrder.replaceAll("createdBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, criteria.isAscending() ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    builder.getOrderArguments().add(sortTerm.toSortField());
                    needsCreatedBySortJoin = true;
                } else if (subOrder.startsWith("modifiedBy")) {
                    subOrder = subOrder.replaceAll("modifiedBy", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, criteria.isAscending() ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    builder.getOrderArguments().add(sortTerm.toSortField());
                    needsModifiedBySortJoin = true;
                } else {
                    builder.addOrder(subOrder, criteria.isAscending());
                }
            }
        }

        if (needsCreatedBySortJoin) {
            builder.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "createdBy", "sortJoin", true));
        }

        if (needsModifiedBySortJoin) {
            builder.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "modifiedBy", "sortJoin", true));
        }

    }

    public Long getEventBooksCountByState(Archivable.EntityState state) {
        if(Archivable.EntityState.ACTIVE.equals(state)) {
            return getActiveEventBookCount();
        } else {
            return getArchivedEventBookCount();
        }
    }

    public List<EventBook> getEventBooks(EventBookListFilterCriteria criteria, Archivable.EntityState state) {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class, true);
        applyFilter(query, criteria);
        query.addSimpleWhere("state", state);
        return persistenceService.findAll(query);

    }

    public List<EventBook> getPagedEventBooksListByState(Archivable.EntityState state,
                                                         String order,
                                                         boolean ascending,
                                                         int first,
                                                         int count) {

        QueryBuilder<EventBook> query = (Archivable.EntityState.ACTIVE.equals(state) ?
                                            createUserSecurityBuilder(EventBook.class) :
                                            createUserSecurityBuilder(EventBook.class, true));

        query.addSimpleWhere("state", state);

        if(order != null) {
            for (String subOrder : order.split(",")) {
                query.addOrder(subOrder.trim(), ascending);
            }
        }

        return persistenceService.findAllPaginated(query, first, count);
    }
}
