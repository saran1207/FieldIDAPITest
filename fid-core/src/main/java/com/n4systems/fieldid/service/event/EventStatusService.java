package com.n4systems.fieldid.service.event;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.EventStatus;
import com.n4systems.model.api.Archivable;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import java.util.List;

public class EventStatusService extends FieldIdPersistenceService {

    public List<EventStatus> getActiveStatuses() {
        QueryBuilder<EventStatus> builder = createUserSecurityBuilder(EventStatus.class);

        builder.addOrder("name");
        builder.addSimpleWhere("state", Archivable.EntityState.ACTIVE);

        return persistenceService.findAll(builder);
    }

    public List<EventStatus> getArchivedStatues() {
        QueryBuilder<EventStatus> builder = createTenantSecurityBuilder(EventStatus.class, true);

        builder.addOrder("name");
        builder.addSimpleWhere("state", Archivable.EntityState.ARCHIVED);

        return persistenceService.findAll(builder);
    }

    public void create(EventStatus eventStatus) {
        persistenceService.save(eventStatus);
    }

    public void update(EventStatus eventStatus) {
        persistenceService.update(eventStatus);
    }

    public void archive(EventStatus eventStatus) {
        eventStatus.archiveEntity();
        update(eventStatus);
    }

    public void unarchive(EventStatus eventStatus) {
        eventStatus.activateEntity();
        update(eventStatus);
    }

    public boolean exists(String name, Long id) {
        QueryBuilder<EventStatus> builder = createUserSecurityBuilder(EventStatus.class);
        builder.addWhere(WhereClauseFactory.create("name", name));
        if(id != null) {
            builder.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE,  "id", id));
        }
        return persistenceService.exists(builder);
    }
}
