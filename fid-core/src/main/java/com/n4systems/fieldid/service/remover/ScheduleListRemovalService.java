package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.ScheduleListRemovalSummary;
import com.n4systems.model.AssetType;
import com.n4systems.model.Event;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ScheduleListRemovalService extends FieldIdPersistenceService {

    // CAVEAT : using IN on events table sucks on MySQL.  you may want to use a loop if you know your list size.
    //  also, be careful how many id's you have in your in clause.  there are limits to be aware of...  most likely 1024
    //    http://www.dbforums.com/mysql/1022217-max-no-values-where-clause.html

    public static final String ARCHIVE_IDS_QUERY = "UPDATE %s SET state = :archivedState, modified = :now  WHERE id IN (:ids)";
    public static final String DELETE_IDS_QUERY = "DELETE FROM %s WHERE id IN (:ids)";

    @Transactional
    public void remove(AssetType assetType, EventType eventType, Event.EventStateGrouping eventStates) {
        List<Long> ids = scheduleIds(assetType, eventType, eventStates);
        Query query = null;

        if (eventStates == Event.EventStateGrouping.NON_COMPLETE) {
            String archiveQuery = String.format(DELETE_IDS_QUERY, EventSchedule.class.getName());
            query = persistenceService.createQuery(archiveQuery, new HashMap<String, Object>());
        } else {
            String archiveQuery = String.format(ARCHIVE_IDS_QUERY, EventSchedule.class.getName());

            final HashMap<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("archivedState", Archivable.EntityState.ARCHIVED);
            queryParams.put("now", new Date());
            query = persistenceService.createQuery(archiveQuery, queryParams);
        }

        new LargeInListQueryExecutor().executeUpdate(query, ids);
    }


    @Transactional
    public void archiveLegacySchedules(AssetType assetType, EventType eventType) {
        List<Long> ids = scheduleIds(assetType, eventType, Event.EventStateGrouping.NON_COMPLETE);

        String archiveQuery = String.format(ARCHIVE_IDS_QUERY, EventSchedule.class.getName());
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("archivedState", Archivable.EntityState.ARCHIVED);
        queryParams.put("now", new Date());
        Query query = persistenceService.createQuery(archiveQuery, queryParams);

        new LargeInListQueryExecutor().executeUpdate(query, ids);
    }


    @Transactional
    public void deleteAssociatedEvents(AssetType assetType, EventType eventType) {
        List<Long> ids = eventIds(assetType, eventType);

        String archiveQuery = String.format(ARCHIVE_IDS_QUERY, Event.class.getName());
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("archivedState", Archivable.EntityState.ARCHIVED);
        queryParams.put("now", new Date());

        Query query = persistenceService.createQuery(archiveQuery, queryParams);

        new LargeInListQueryExecutor().executeUpdate(query, ids);
    }


    private List<Long> eventIds(AssetType assetType, EventType eventType) {
        QueryBuilder<Long> query = new QueryBuilder<Long>(Event.class, new TenantOnlySecurityFilter(assetType.getTenant()));
        query.setSelectArgument(new SimpleSelect("id")).addSimpleWhere("state", Archivable.EntityState.ACTIVE).addSimpleWhere("eventState", Event.EventState.OPEN).addSimpleWhere("type", eventType);

        if (assetType != null) {
            query.addSimpleWhere("asset.type", assetType);
        }

        return persistenceService.findAll(query);
    }



    private List<Long> scheduleIds(AssetType assetType, EventType eventType, Event.EventStateGrouping eventStates) {
        QueryBuilder<Long> schedulesToDelete = new QueryBuilder<Long>(Event.class, new OpenSecurityFilter());
        schedulesToDelete.setSelectArgument(new SimpleSelect("id")).addSimpleWhere("state", Archivable.EntityState.ACTIVE).addSimpleWhere("type", eventType);
        schedulesToDelete.addWhere(WhereParameter.Comparator.IN, "eventState", "eventState", Arrays.asList(eventStates.getMembers()));
        schedulesToDelete.addWhere(WhereClauseFactory.createNotNull("nextDate"));

        if (assetType != null) {
            schedulesToDelete.addSimpleWhere("asset.type", assetType);
        }

        return persistenceService.findAll(schedulesToDelete);
    }

    @Transactional
	public ScheduleListRemovalSummary summary(AssetType assetType, EventType eventType, Event.EventStateGrouping eventStates) {
		return new ScheduleListRemovalSummary((long)scheduleIds(assetType,  eventType, eventStates).size());
	}

}
