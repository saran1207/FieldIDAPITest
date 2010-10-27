package com.n4systems.handlers.remover;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.remover.summary.InspectionArchiveSummary;
import com.n4systems.model.Asset;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.archivers.InspectionListArchiver;
import com.n4systems.persistence.utils.LargeInClauseSelect;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;


public class InspectionListArchiveHandlerImp implements InspectionTypeListArchiveHandler {

	private final ScheduleListDeleteHandler scheduleListDeleter;
	private InspectionType inspectionType;

	public InspectionListArchiveHandlerImp(ScheduleListDeleteHandler scheduleListDeleter) {
		this.scheduleListDeleter = scheduleListDeleter;
	}
	
	public void remove(Transaction transaction) {
		archiveInspections(transaction.getEntityManager());
		scheduleListDeleter.targetCompleted().setInspectionType(inspectionType).remove(transaction);
	}

	private void archiveInspections(EntityManager em) {
		List<Long> ids = getInspectionIds(em);
		
		archiveInspections(em, ids);
		updateAssetsLastInspectionDate(em, ids);
	}

	private void archiveInspections(EntityManager em, List<Long> ids) {
		InspectionListArchiver archiver = new InspectionListArchiver(new TreeSet<Long>(ids));
		archiver.archive(em);
	}

	private void updateAssetsLastInspectionDate(EntityManager em, List<Long> ids) {
		
		List<Long> assetsToUpdateInspectionDate = new LargeInClauseSelect<Long>( new QueryBuilder<Long>(Inspection.class, new OpenSecurityFilter())
				.setSimpleSelect("asset.id", true)
				.addSimpleWhere("asset.state", EntityState.ACTIVE),
		  ids,
		  em).getResultList();
		
		for (Long assetId : new HashSet<Long>(assetsToUpdateInspectionDate)) {
			Asset asset = em.find(Asset.class, assetId);
			
			
			QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Inspection.class, new OpenSecurityFilter(), "i");
			qBuilder.setMaxSelect("date");
			qBuilder.addSimpleWhere("state", EntityState.ACTIVE);
			qBuilder.addSimpleWhere("asset", asset);


			Date lastInspectionDate = null;
			try {
				lastInspectionDate = qBuilder.getSingleResult(em);
			} catch (Exception e) {
				throw new ProcessFailureException("could not archive the inspections", e);
			}

			asset.setLastInspectionDate(lastInspectionDate);
			
			em.merge(asset);
		}
	}

	private List<Long> getInspectionIds(EntityManager em) {
		QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(Inspection.class, new OpenSecurityFilter())
				.setSelectArgument(new SimpleSelect("id"))
				.addSimpleWhere("type", inspectionType);
		return queryBuilder.getResultList(em);
	}
	
	public InspectionArchiveSummary summary(Transaction transaction) {
		InspectionArchiveSummary summary = new InspectionArchiveSummary();
		
		summary.setDeleteInspections(inspectionToBeDeleted(transaction));
		summary.setDeleteSchedules(scheduleListDeleter.targetCompleted().setInspectionType(inspectionType).summary(transaction).getSchedulesToRemove());
		
		return summary;
	}

	private Long inspectionToBeDeleted(Transaction transaction) {
		String archiveQuery = "SELECT COUNT(id) FROM " + Inspection.class.getName() + " i WHERE i.type = :inspectionType AND i.state = :active";
		Query query = transaction.getEntityManager().createQuery(archiveQuery);
		query.setParameter("inspectionType", inspectionType);
		query.setParameter("active", EntityState.ACTIVE);
		
		return (Long)query.getSingleResult();
	}
	
	public InspectionListArchiveHandlerImp setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		return this;
	}
	
}
