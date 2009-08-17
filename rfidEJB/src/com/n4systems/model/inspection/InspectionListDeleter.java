package com.n4systems.model.inspection;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.remover.summary.InspectionArchiveSummary;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeInClauseSelect;
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
 
		new LargeUpdateQueryRunner(query, ids).executeUpdate();
		
		
		List<Long> productsToUpdateInspectionDate = new LargeInClauseSelect<Long>( new QueryBuilder<Long>(Inspection.class)
																							.setSimpleSelect("product.id", true)
																							.addSimpleWhere("product.state", EntityState.ACTIVE),
																					  ids,
																					  em).getResultList();
		
		for (Long productId : new HashSet<Long>(productsToUpdateInspectionDate)) {
			Product product = em.find(Product.class, productId);
			
			
			QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Inspection.class, "i");
			qBuilder.setMaxSelect("date");
			qBuilder.addSimpleWhere("state", EntityState.ACTIVE);
			qBuilder.addSimpleWhere("product", product);


			Date lastInspectionDate = null;
			try {
				lastInspectionDate = qBuilder.getSingleResult(em);
			} catch (Exception e) {
				throw new ProcessFailureException("could not archive the inspections", e);
			}

			product.setLastInspectionDate(lastInspectionDate);
			
			em.merge(product);
		}
		
		
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
