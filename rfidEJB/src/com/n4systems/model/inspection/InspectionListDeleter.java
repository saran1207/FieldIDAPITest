package com.n4systems.model.inspection;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeUpdateQueryRunner;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;


public class InspectionListDeleter {

	private InspectionType inspectionType;
	
	
	protected int archive(EntityManager em) {
		int result = archiveInspections(em);
		
		
		createArchiveSchedulesQuery(em).executeUpdate();
		return result;
	}

	private int archiveInspections(EntityManager em) {
		List<Long> ids = getInspectionIds(em);
		
		
		String archiveQuery = "UPDATE " + Inspection.class.getName() + " SET state = :archived, modified = :now " +
			" WHERE id IN(:ids)";
		Query query = em.createQuery(archiveQuery);
		query.setParameter("archived", EntityState.ARCHIVED);
		query.setParameter("now", new Date());
		//TODO: move to object that helps do queries with in clauses of more than 1000 elements. 
		new LargeUpdateQueryRunner(query, ids).executeUpdate();
		return ids.size();
	}

	private List<Long> getInspectionIds(EntityManager em) {
		QueryBuilder<Long> queryBuilder = new QueryBuilder<Long>(Inspection.class)
				.setSelectArgument(new SimpleSelect("id"))
				.addSimpleWhere("type", inspectionType);
		return queryBuilder.getResultList(em);
	}
	
	
	private Query createArchiveSchedulesQuery(EntityManager em) {
		String archiveQuery = "UPDATE " + InspectionSchedule.class.getName() + " SET state = :archived, modified = :now " +
				" WHERE inspectionType = :inspectionType AND state = :active";
		Query query = em.createQuery(archiveQuery);
		query.setParameter("archived", EntityState.ARCHIVED);
		query.setParameter("now", new Date());
		query.setParameter("inspectionType", inspectionType);
		query.setParameter("active", EntityState.ACTIVE);
		
		return query;
	}
	
	
	
	public int archive(Transaction transaction) {
		return archive(transaction.getEntityManager());
	}
	
	
	public InspectionArchiveSummary summary(Transaction transaction) {
		InspectionArchiveSummary summary = new InspectionArchiveSummary();
		
		summary.setDeleteInspections(inspectionToBeDeleted(transaction));
		
		return summary;
	}


	private Long inspectionToBeDeleted(Transaction transaction) {
		String archiveQuery = "SELECT COUNT(id) FROM " + Inspection.class.getName() + " i WHERE i.type = :inspectionType AND i.state = :active";
		Query query = transaction.getEntityManager().createQuery(archiveQuery);
		query.setParameter("inspectionType", inspectionType);
		query.setParameter("active", EntityState.ACTIVE);
		
		return (Long)query.getSingleResult();
	}
	

	
	
	public InspectionListDeleter setInspectionType(InspectionType inspectionType) {
		this.inspectionType = inspectionType;
		
		
		
		return this;
	}
	
}
