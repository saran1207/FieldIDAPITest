package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventBook;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import java.util.Date;
import java.util.List;

public class EventBookService extends FieldIdPersistenceService {

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

    public Long getEventBooksCountByState(Archivable.EntityState state) {
        if(Archivable.EntityState.ACTIVE.equals(state)) {
            return getActiveEventBookCount();
        } else {
            return getArchivedEventBookCount();
        }
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
