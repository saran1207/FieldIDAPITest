package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.impl.LegacyProductSerialManager;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.ManualSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;


public class InspectionManagerImpl implements InspectionManager {
	static Logger logger = Logger.getLogger(InspectionManagerImpl.class);

	
	private EntityManager em;

	private final PersistenceManager persistenceManager;
	private final ManagerBackedInspectionSaver inspectionSaver;


	private final EntityManagerLastInspectionDateFinder lastInspectionFinder;


	private final InspectionScheduleManager inspectionScheduleManager;

	
	public InspectionManagerImpl(EntityManager em) {
		super();
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.lastInspectionFinder = new EntityManagerLastInspectionDateFinder(persistenceManager, em);
		this.inspectionScheduleManager = new InspectionScheduleManagerImpl(em);
		this.inspectionSaver = new ManagerBackedInspectionSaver(new LegacyProductSerialManager(em), 
				persistenceManager, em, lastInspectionFinder);
	}

	/**
	 * finds all the groups that you can view with the defined security filter.
	 */
	@SuppressWarnings("unchecked")
	private List<InspectionGroup> findAllInspectionGroups(SecurityFilter userFilter, Long productId) {
		ManualSecurityFilter filter = new ManualSecurityFilter(userFilter);
		filter.setTargets("ig.tenant.id", "inspection.owner", null, null);

		String queryString = "Select DISTINCT ig FROM InspectionGroup as ig INNER JOIN ig.inspections as inspection LEFT JOIN inspection.product as product"
				+ " WHERE product.id = :id AND inspection.state = :activeState  AND " + filter.produceWhereClause() + " ORDER BY ig.created ";

		// TODO: move the query creation to PersistenceManager then this does
		// not need an Entity Manager at all.
		Query query = em.createQuery(queryString);

		filter.applyParameters(query);
		query.setParameter("id", productId);
		query.setParameter("activeState", EntityState.ACTIVE);

		return query.getResultList();
	}

	/**
	 * finds all the groups that you can view with the defined security filter.
	 */
	public List<InspectionGroup> findAllInspectionGroups(SecurityFilter filter, Long productId, String... postFetchFields) {
		return (List<InspectionGroup>) persistenceManager.postFetchFields(findAllInspectionGroups(filter, productId), postFetchFields);
	}

	
	public Inspection findInspectionThroughSubInspection(Long subInspectionId, SecurityFilter filter) {
		String str = "select i FROM Inspection i, IN( i.subInspections ) s WHERE s.id = :subInspection AND ";
		str += filter.produceWhereClause(Inspection.class, "i");
		Query query = em.createQuery(str);
		query.setParameter("subInspection", subInspectionId);
		filter.applyParameters(query, Inspection.class);
		try {
			return (Inspection) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub inspection attached", e);
			return null;
		}
	}

	public SubInspection findSubInspection(Long subInspectionId, SecurityFilter filter) {
		Inspection inspection = findInspectionThroughSubInspection(subInspectionId, filter);

		if (inspection == null) {
			return null;
		}

		try {
			return persistenceManager.find(SubInspection.class, subInspectionId, "attachments");
		} catch (NoResultException e) {
			return null;
		} catch (Exception e) {
			logger.error("Could not check if sub inspection attached ", e);
			return null;
		}
	}

	public Inspection findAllFields(Long id, SecurityFilter filter) {
		Inspection inspection = null;

		QueryBuilder<Inspection> queryBuilder = new QueryBuilder<Inspection>(Inspection.class, filter);
		queryBuilder.setSimpleSelect().addSimpleWhere("id", id).addSimpleWhere("state", EntityState.ACTIVE);
		queryBuilder.addOrder("created");
		queryBuilder.addPostFetchPaths("modifiedBy.userID", "type.sections", "type.supportedProofTests", "type.infoFieldNames", "attachments", "results", "product", "product.infoOptions",
				"infoOptionMap", "subInspections");

		try {
			inspection = persistenceManager.find(queryBuilder);
			if (inspection != null) {
				// now we need to postfetch the rec/def lists on the results. We
				// can't do this above since the results themselvs are a list.
				persistenceManager.postFetchFields(inspection.getResults(), "recommendations", "deficiencies");

			}

		} catch (InvalidQueryException iqe) {
			logger.error("bad query while loading inspection", iqe);
		}

		return inspection;
	}

	public List<Inspection> findInspectionsByDateAndProduct(Date inspectionDateRangeStart, Date inspectionDateRangeEnd, Product product, SecurityFilter filter) {
		

		QueryBuilder<Inspection> queryBuilder = new QueryBuilder<Inspection>(Inspection.class, filter);
		queryBuilder.setSimpleSelect();
		queryBuilder.addSimpleWhere("state", EntityState.ACTIVE);
		queryBuilder.addWhere(Comparator.GE, "beginingDate", "date", inspectionDateRangeStart).addWhere(Comparator.LE, "endingDate", "date", inspectionDateRangeEnd); 
		queryBuilder.addSimpleWhere("product", product);

		List<Inspection> inspections;
		try {
			inspections = persistenceManager.findAll(queryBuilder);
		} catch (InvalidQueryException e) {
			inspections = new ArrayList<Inspection>();
			logger.error("Unable to load Inspections by Date and Product", e);
		}

		return inspections;
	}


	

	
	public Inspection updateInspection(Inspection inspection, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		return inspectionSaver.updateInspection(inspection, userId, fileData, uploadedFiles);
	}



	public Inspection retireInspection(Inspection inspection, Long userId) {
		inspection.retireEntity();
		inspection = persistenceManager.update(inspection, userId);
		inspectionSaver.updateProductInspectionDate(inspection.getProduct());
		inspection.setProduct(persistenceManager.update(inspection.getProduct()));
		inspectionScheduleManager.restoreScheduleForInspection(inspection);
		return inspection;
	}

	

	
	

	
	/**
	 * This must be called AFTER the inspection and subinspection have been persisted
	 */
	public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		return inspectionSaver.attachFilesToSubInspection(inspection, subInspection, uploadedFiles);
	}

	

	
	
	/**
	 * ensure that all criteria are retired under a retired section.
	 */
	public InspectionType updateInspectionForm(InspectionType inspectionType, Long modifyingUserId) {
		if (inspectionType.getSections() != null && !inspectionType.getSections().isEmpty()) {
			for (CriteriaSection section : inspectionType.getSections()) {
				if (section.isRetired()) {
					for (Criteria criteria : section.getCriteria()) {
						criteria.setRetired(true);
					}
				}
			}
		}
		
		// any update to an inspection form, requires an increment of the form version
		inspectionType.incrementFormVersion();
		
		return persistenceManager.update(inspectionType, modifyingUserId);
	}

	

	public Pager<Inspection> findNewestInspections(WSSearchCritiera searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {

		List<Long> customerIds = searchCriteria.getCustomerIds();
		List<Long> divisionIds = searchCriteria.getDivisionIds();

		boolean setCustomerInfo = (customerIds != null && customerIds.size() > 0);
		boolean setDivisionInfo = (divisionIds != null && divisionIds.size() > 0);

		String selectStatement = " from Inspection i ";

		String whereClause = "where ( ";
		if (setCustomerInfo) {
			whereClause += "i.product.owner.customerOrg.id in (:customerIds) ";

			if (setDivisionInfo) {
				whereClause += "or ";
			}
		}

		if (setDivisionInfo) {
			whereClause += "i.product.owner.divisionOrg.id in (:divisionIds)";
		}

		whereClause += ") AND i.product.lastInspectionDate = i.date and " + securityFilter.produceWhereClause(Inspection.class, "i") + ")";

		Query query = em.createQuery("select i " + selectStatement + whereClause + " ORDER BY i.id");
		if (setCustomerInfo)
			query.setParameter("customerIds", customerIds);
		if (setDivisionInfo)
			query.setParameter("divisionIds", divisionIds);
		securityFilter.applyParameters(query, Inspection.class);

		Query countQuery = em.createQuery("select count( i.id ) " + selectStatement + whereClause);
		if (setCustomerInfo)
			countQuery.setParameter("customerIds", customerIds);
		if (setDivisionInfo)
			countQuery.setParameter("divisionIds", divisionIds);
		securityFilter.applyParameters(countQuery, Inspection.class);

		return new Page<Inspection>(query, countQuery, page, pageSize);
	}

	public Pager<Inspection> findNewestInspections(WSJobSearchCriteria searchCriteria, SecurityFilter securityFilter, int page, int pageSize) {

		List<Long> jobIds = searchCriteria.getJobIds();

		String selectStatement = " from Inspection i ";

		String whereClause = "where ( ";
		
		whereClause += "i.product.id in (select sch.product.id from Project p, IN (p.schedules) sch where p.id in (:jobIds))";

		whereClause += ") AND i.product.lastInspectionDate = i.date and " + securityFilter.produceWhereClause(InspectionGroup.class, "i") + ")";

		Query query = em.createQuery("select i " + selectStatement + whereClause + " ORDER BY i.id");
		query.setParameter("jobIds", jobIds);
		securityFilter.applyParameters(query, InspectionGroup.class);

		Query countQuery = em.createQuery("select count( i.id ) " + selectStatement + whereClause);
		countQuery.setParameter("jobIds", jobIds);
		securityFilter.applyParameters(countQuery, InspectionGroup.class);

		return new Page<Inspection>(query, countQuery, page, pageSize);
	}
	
	public boolean isMasterInspection(Long id) {
		Inspection inspection = em.find(Inspection.class, id);

		return (inspection != null) ? (!inspection.getSubInspections().isEmpty()) : false;
	}

	
	public Date findLastInspectionDate(InspectionSchedule schedule) {
		return lastInspectionFinder.findLastInspectionDate(schedule);
	}

	public Date findLastInspectionDate(Long scheduleId) {
		return lastInspectionFinder.findLastInspectionDate(scheduleId);
	}


}
