package com.n4systems.handlers.remover;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.n4systems.handlers.remover.summary.ScheduleListRemovalSummary;
import com.n4systems.model.AssociatedEventType;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.AssetType;
import com.n4systems.model.EventSchedule.ScheduleStatusGrouping;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeInListQueryExecutor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ScheduleListDeleteHandlerImpl implements ScheduleListDeleteHandler {
	
	
	private EventType eventType;
	private AssetType assetType;
	private ScheduleStatusGrouping target = ScheduleStatusGrouping.NON_COMPLETE;
	
	private Transaction transaction;
	
	
	public void remove(Transaction transaction) {
		this.transaction = transaction;
		
		List<Long> ids = scheduleIds();
		Query query = null;
		
		if (target == ScheduleStatusGrouping.NON_COMPLETE) {
			String archiveQuery = "DELETE FROM " + EventSchedule.class.getName() +	" WHERE id IN (:ids)";
			query = this.transaction.getEntityManager().createQuery(archiveQuery);
		} else {
			String archiveQuery = "UPDATE " + EventSchedule.class.getName() + " SET state = :archivedState, modified = :now " +
					" WHERE id IN (:ids)";
			query = this.transaction.getEntityManager().createQuery(archiveQuery);
			query.setParameter("archivedState", EntityState.ARCHIVED);
			query.setParameter("now", new Date());
		}
		
		new LargeInListQueryExecutor().executeUpdate(query, ids);
	}

	
	private List<Long> scheduleIds() {
		QueryBuilder<Long> schedulesToDelete = new QueryBuilder<Long>(EventSchedule.class, new OpenSecurityFilter());
		schedulesToDelete.setSelectArgument(new SimpleSelect("id")).addSimpleWhere("state", EntityState.ACTIVE).addSimpleWhere("eventType", eventType);
		schedulesToDelete.addWhere(Comparator.IN, "status", "status", Arrays.asList(target.getMembers()));
				
		if (assetType != null) {
			schedulesToDelete.addSimpleWhere("asset.type", assetType);
		}
		
		List<Long> ids = schedulesToDelete.getResultList(transaction.getEntityManager());
		return ids;
	}

	
	public ScheduleListRemovalSummary summary(Transaction transaction) {
		this.transaction = transaction;
		return new ScheduleListRemovalSummary(new Long(scheduleIds().size()));
	}

	
	public ScheduleListDeleteHandler setInspectionType(EventType eventType) {
		this.eventType = eventType;
		return this;
	}

	
	public ScheduleListDeleteHandler setAssociatedEventType(AssociatedEventType associatedEventType) {
		this.assetType = associatedEventType.getAssetType();
		this.eventType = associatedEventType.getEventType();
		return this;
	}


	public ScheduleListDeleteHandler targetCompleted() {
		target = ScheduleStatusGrouping.COMPLETE;
		return this;
	}

	
	public ScheduleListDeleteHandler targetNonCompleted() {
		target = ScheduleStatusGrouping.NON_COMPLETE;
		return this;
	}

	
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
