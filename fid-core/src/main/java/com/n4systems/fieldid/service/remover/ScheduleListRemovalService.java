package com.n4systems.fieldid.service.remover;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.handlers.remover.summary.ScheduleListRemovalSummary;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.api.Archivable;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereParameter;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

public class ScheduleListRemovalService extends FieldIdPersistenceService {

    @Transactional
	public void remove(AssetType assetType, EventType eventType, EventSchedule.ScheduleStatusGrouping scheduleStatus) {
		List<Long> ids = scheduleIds(assetType, eventType, scheduleStatus);
		Query query = null;

		if (scheduleStatus == EventSchedule.ScheduleStatusGrouping.NON_COMPLETE) {
			String archiveQuery = "DELETE FROM " + EventSchedule.class.getName() +	" WHERE id IN (:ids)";
            query = persistenceService.createQuery(archiveQuery, new HashMap<String, Object>());
		} else {
            String archiveQuery = "UPDATE " + EventSchedule.class.getName() + " SET state = :archivedState, modified = :now " +
					" WHERE id IN (:ids)";

			final HashMap<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("archivedState", Archivable.EntityState.ARCHIVED);
			queryParams.put("now", new Date());
            query = persistenceService.createQuery(archiveQuery, queryParams);
		}

		new LargeInListQueryExecutor().executeUpdate(query, ids);
	}

	private List<Long> scheduleIds(AssetType assetType, EventType eventType, EventSchedule.ScheduleStatusGrouping scheduleStatus) {
		QueryBuilder<Long> schedulesToDelete = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		schedulesToDelete.setSelectArgument(new SimpleSelect("id")).addSimpleWhere("state", Archivable.EntityState.ACTIVE).addSimpleWhere("eventType", eventType);
		schedulesToDelete.addWhere(WhereParameter.Comparator.IN, "status", "status", Arrays.asList(scheduleStatus.getMembers()));

		if (assetType != null) {
			schedulesToDelete.addSimpleWhere("asset.type", assetType);
		}

        return persistenceService.findAll(schedulesToDelete);
	}

    @Transactional
	public ScheduleListRemovalSummary summary(AssetType assetType, EventType eventType, EventSchedule.ScheduleStatusGrouping scheduleStatus) {
		return new ScheduleListRemovalSummary((long)scheduleIds(assetType,  eventType, scheduleStatus).size());
	}

}
