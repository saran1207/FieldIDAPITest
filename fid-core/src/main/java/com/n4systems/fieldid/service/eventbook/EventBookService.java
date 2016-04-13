package com.n4systems.fieldid.service.eventbook;

import com.n4systems.fieldid.service.CrudService;
import com.n4systems.model.EventBook;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OwnerAndDownFilter;
import com.n4systems.util.persistence.*;
import com.n4systems.util.persistence.search.SortDirection;
import com.n4systems.util.persistence.search.SortTerm;

import java.util.List;

public class EventBookService extends CrudService<EventBook> {

    private static String [] DEFAULT_ORDER = {"firstName", "lastName"};

    public EventBookService() {
        super(EventBook.class);
    }

    public List<EventBook> getAllEventBooks() {
        return persistenceService.findAll(EventBook.class);
    }

    public EventBook findArchivedEventBookById(Long id) {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class, true);

        query.addSimpleWhere("id", id);

        return persistenceService.find(query);
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
        boolean needsJobSiteSortJoin = false;
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
                } else if (subOrder.startsWith("owner")) {
                    subOrder = subOrder.replaceAll("owner", "sortJoin");
                    SortTerm sortTerm = new SortTerm(subOrder, criteria.isAscending() ? SortDirection.ASC : SortDirection.DESC);
                    sortTerm.setAlwaysDropAlias(true);
                    sortTerm.setFieldAfterAlias(subOrder.substring("sortJoin".length() + 1));
                    builder.getOrderArguments().add(sortTerm.toSortField());
                    needsJobSiteSortJoin = true;
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
        if (needsJobSiteSortJoin) {
            builder.addJoin(new JoinClause(JoinClause.JoinType.LEFT, "owner", "sortJoin", true));
        }

    }

    public List<EventBook> getEventBooks(EventBookListFilterCriteria criteria, Archivable.EntityState state) {
        QueryBuilder<EventBook> query = createUserSecurityBuilder(EventBook.class, true);
        applyFilter(query, criteria);
        query.addSimpleWhere("state", state);
        return persistenceService.findAll(query);

    }
}
