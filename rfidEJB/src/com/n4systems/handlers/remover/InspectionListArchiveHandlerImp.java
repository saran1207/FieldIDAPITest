package com.n4systems.handlers.remover;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.n4systems.exceptions.ProcessFailureException;
import com.n4systems.handlers.remover.summary.InspectionArchiveSummary;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.utils.LargeInClauseSelect;
import com.n4systems.persistence.utils.LargeUpdateQueryRunner;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.SimpleSelect;


public class InspectionListArchiveHandlerImp implements InspectionTypeListArchiveHandler {

	private final ScheduleListDeleteHandler scheduleListDeleter;  
	
	private InspectionType inspectionType;

	
	public InspectionListArchiveHandlerImp(ScheduleListDeleteHandler scheduleListDeleter) {
		super();
		this.scheduleListDeleter = scheduleListDeleter;
	}
	
	
	public void remove(Transaction transaction) {
		archiveInspections(transaction.getEntityManager());
		scheduleListDeleter.targetCompleted().setInspectionType(inspectionType).remove(transaction);
	}


	private void archiveInspections(EntityManager em) {
		List<Long> ids = getInspectionIds(em);
		
		archiveInspections(em, ids);
		updateProductsLastInspectionDate(em, ids);
	}


	private void archiveInspections(EntityManager em, List<Long> ids) {
		String archiveQuery = "UPDATE " + Inspection.class.getName() + " SET state = :archived, modified = :now " +
			" WHERE id IN(:ids)";
		Query query = em.createQuery(archiveQuery);
		query.setParameter("archived", EntityState.ARCHIVED);
		query.setParameter("now", new Date());
 
		new LargeUpdateQueryRunner(query, ids).executeUpdate();
	}



	private void updateProductsLastInspectionDate(EntityManager em, List<Long> ids) {
		
		List<Long> productsToUpdateInspectionDate = new LargeInClauseSelect<Long>( new QueryBuilder<Long>(Inspection.class, new OpenSecurityFilter())
				.setSimpleSelect("product.id", true)
				.addSimpleWhere("product.state", EntityState.ACTIVE),
		  ids,
		  em).getResultList();
		
		for (Long productId : new HashSet<Long>(productsToUpdateInspectionDate)) {
			Product product = em.find(Product.class, productId);
			
			
			QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Inspection.class, new OpenSecurityFilter(), "i");
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
