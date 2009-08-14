package com.n4systems.handlers.remover;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.solr.util.ArraysUtils;

import com.n4systems.handlers.remover.summary.ScheduleListRemovalSummary;
import com.n4systems.model.AssociatedInspectionType;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.ProductType;
import com.n4systems.model.InspectionSchedule.ScheduleStatusGrouping;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeUpdateQueryRunner;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class ScheduleListDeleteHandlerImpl implements ScheduleListDeleteHandler {
	
	
	private InspectionType inspectionType;
	private ProductType productType;
	private ScheduleStatusGrouping target = ScheduleStatusGrouping.NON_COMPLETE;
	
	private Transaction transaction;
	
	public void remove(Transaction transaction) {
		this.transaction = transaction;
		
		List<Long> ids = scheduleIds();
		Query query = null;
		
		if (target == ScheduleStatusGrouping.NON_COMPLETE) {
			String archiveQuery = "DELETE FROM " + InspectionSchedule.class.getName() +	" WHERE id IN (:ids)";
			query = this.transaction.getEntityManager().createQuery(archiveQuery);
		} else {
			String archiveQuery = "UPDATE " + InspectionSchedule.class.getName() + " SET state = ARCHVIED, modified = :now " +	
					" WHERE id IN (:ids)";
			query = this.transaction.getEntityManager().createQuery(archiveQuery);
			query.setParameter("state", EntityState.ARCHIVED);
			query.setParameter("modified", new Date());
		}
		
		new LargeUpdateQueryRunner(query, ids).executeUpdate();
	}

	private List<Long> scheduleIds() {
		QueryBuilder<Long> schedulesToDelete = new QueryBuilder<Long>(InspectionSchedule.class);
		schedulesToDelete.setSelectArgument(new SimpleSelect("id")).addSimpleWhere("state", EntityState.ACTIVE).addSimpleWhere("inspectionType", inspectionType);
		schedulesToDelete.addWhere(Comparator.IN, "status", "status", Arrays.asList(target.getMembers()));
				
		if (productType != null) {
			schedulesToDelete.addSimpleWhere("product.type", productType);
		}
		
		List<Long> ids = schedulesToDelete.getResultList(transaction.getEntityManager());
		return ids;
	}

	public ScheduleListRemovalSummary summary(Transaction transaction) {
		this.transaction = transaction;
		return new ScheduleListRemovalSummary(scheduleIds().size());
	}

	public ScheduleListDeleteHandler setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}

	public ScheduleListDeleteHandler setAssociatedInspectionType(AssociatedInspectionType associatedInspectionType) {
		this.productType = associatedInspectionType.getProductType();
		this.inspectionType = associatedInspectionType.getInspectionType();
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
