package com.n4systems.ejb.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.impl.LegacyProductSerialManager;
import com.n4systems.ejb.parameters.CreateInspectionParameterBuilder;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.SubInspection;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.ManualSecurityFilter;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;


public class InspectionManagerImpl implements InspectionManager, LastInspectionDateFinder {
	static Logger logger = Logger.getLogger(InspectionManagerImpl.class);

	
	private EntityManager em;

	private final PersistenceManager persistenceManager;
	private final InspectionSaver inspectionSaver;

	
	public InspectionManagerImpl(EntityManager em) {
		super();
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.inspectionSaver = new InspectionSaver(new LegacyProductSerialManager(em), new InspectionScheduleManagerImpl(em), persistenceManager, em, this);
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

	/**
	 * Finds a unique inspection group for a tenant based on the mobile guid
	 */
	public InspectionGroup findInspectionGroupByMobileGuid(String mobileGuid, SecurityFilter filter) {
		InspectionGroup inspectionGroup = null;

		QueryBuilder<InspectionGroup> queryBuilder = new QueryBuilder<InspectionGroup>(InspectionGroup.class, filter);
		queryBuilder.setSimpleSelect().addSimpleWhere("mobileGuid", mobileGuid);

		try {
			inspectionGroup = persistenceManager.find(queryBuilder);
		} catch (InvalidQueryException iqe) {
			logger.error("bad query while loading inspection group", iqe);
		}

		return inspectionGroup;
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


	/**
	 * This will persist an entire list of inspections. If it encounters an
	 * inspection with no inspection group it will apply that inspection group
	 * to all the rest of the inspections with no inspection group.
	 * WARNING: All inspections passed into this method <b>MUST</b> be for the same
	 * Product (The Product on the Inspection may be changed otherwise).
	 */
	public List<Inspection> createInspections(String transactionGUID, List<Inspection> inspections, Map<Inspection, Date> nextInspectionDates)
			throws ProcessingProofTestException, FileAttachmentException, TransactionAlreadyProcessedException, UnknownSubProduct {
		List<Inspection> savedInspections = new ArrayList<Inspection>();

		/*
		 *  XXX - Here we pull the Product off the first inspection.  We then re-attach the product back into persistence managed scope.
		 *  This is done so that the infoOptions can be indexed after the Product is updated (otherwise you get a lazy load on infoOptions).
		 *  Since all inspections passed in here are for the same product, we cannot re-attach in the loop since only one entity type with the same id
		 *  can exist in managed scope.  In loop we then set the now managed entity back onto the inspection so they all point to the same
		 *  instance.  This is not optimal and a refactor is in order to avoid this strange case. 
		 */
		Product managedProduct = inspections.iterator().next().getProduct();
		persistenceManager.reattach(managedProduct);
		
		InspectionGroup createdInspectionGroup = null;
		Tenant tenant = null;
		Inspection savedInspection = null;
		for (Inspection inspection : inspections) {
			if (tenant == null) {
				tenant = inspection.getTenant();
			}
			if (createdInspectionGroup != null && inspection.getGroup() == null) {
				inspection.setGroup(createdInspectionGroup);
			}

			// set the managed product back onto the inspection.  See note above.
			inspection.setProduct(managedProduct);
			
			// Pull the attachments off the inspection and send them in seperately so that they get processed properly
			List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();
			fileAttachments.addAll(inspection.getAttachments());
			inspection.setAttachments(new ArrayList<FileAttachment>());
			
			// Pull off the sub inspection attachments and put them in a map for later use
			Map<Product, List<FileAttachment>> subInspectionAttachments = new HashMap<Product, List<FileAttachment>>();
			for (SubInspection subInspection : inspection.getSubInspections()) {
				subInspectionAttachments.put(subInspection.getProduct(), subInspection.getAttachments());
				subInspection.setAttachments(new ArrayList<FileAttachment>());
			}
			
			savedInspection = createInspection(new CreateInspectionParameterBuilder(inspection, inspection.getModifiedBy().getId())
					.withANextInspectionDate(nextInspectionDates.get(inspection)).withUploadedImages(fileAttachments).build());
			
			// handle the subinspection attachments
			SubInspection subInspection = null;
			for (int i =0; i < inspection.getSubInspections().size(); i++) {
				subInspection = inspection.getSubInspections().get(i);
				savedInspection = attachFilesToSubInspection(savedInspection, subInspection, new ArrayList<FileAttachment>(subInspectionAttachments.get(subInspection.getProduct())));
			}			

			// If the inspection didn't have an inspection group before saving,
			// and we havn't created an inspection group yet
			// hang on to the now created inspection group to apply to other
			// inspections
			if (createdInspectionGroup == null && inspection.getGroup() != null) {
				createdInspectionGroup = savedInspection.getGroup();
			}

			savedInspections.add(savedInspection);
		}

		TransactionSupervisor transaction = new TransactionSupervisor(persistenceManager);
		transaction.completeInspectionTransaction(transactionGUID, tenant);

		return savedInspections;
	}


	public Inspection createInspection(CreateInspectionParameter parameterObject) throws ProcessingProofTestException, FileAttachmentException, UnknownSubProduct {
		return inspectionSaver.createInspection(parameterObject);
	}



	

	

	public Inspection updateInspection(Inspection inspection, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		return inspectionSaver.updateInspection(inspection, userId, fileData, uploadedFiles);
	}



	public Inspection retireInspection(Inspection inspection, Long userId) {
		inspection.retireEntity();
		inspection = persistenceManager.update(inspection, userId);
		inspectionSaver.updateProductInspectionDate(inspection.getProduct());
		inspection.setProduct(persistenceManager.update(inspection.getProduct()));
		inspectionSaver.inspectionScheduleManager.restoreScheduleForInspection(inspection);
		return inspection;
	}

	

	
	

	
	/**
	 * This must be called AFTER the inspection and subinspection have been persisted
	 */
	public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		return inspectionSaver.attachFilesToSubInspection(inspection, subInspection, uploadedFiles);
	}

	public Date findLastInspectionDate(Product product) {
		return findLastInspectionDate(product, null);
	}

	public Date findLastInspectionDate(Long scheduleId) {
		return findLastInspectionDate(persistenceManager.find(InspectionSchedule.class, scheduleId));
	}

	public Date findLastInspectionDate(InspectionSchedule schedule) {
		return findLastInspectionDate(schedule.getProduct(), schedule.getInspectionType());
	}

	public Date findLastInspectionDate(Product product, InspectionType inspectionType) {

		QueryBuilder<Date> qBuilder = new QueryBuilder<Date>(Inspection.class, new OpenSecurityFilter(), "i");

		qBuilder.setMaxSelect("date");
		qBuilder.addSimpleWhere("product.id", product.getId());
		qBuilder.addSimpleWhere("state", EntityState.ACTIVE);

		if (inspectionType != null) {
			qBuilder.addSimpleWhere("type.id", inspectionType.getId());
		}

		Date lastInspectionDate = null;
		try {
			lastInspectionDate = qBuilder.getSingleResult(em);
		} catch (InvalidQueryException e) {
			logger.error("Unable to find last inspection date", e);
		} catch (Exception e) {
			logger.error("Unable to find last inspection date", e);
		}

		return lastInspectionDate;
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

	/**
	 * Returns an InspectionType using it's legacyEventId (EventType id) XXX -
	 * this should be removed once the legacyEventId is no longer needed
	 */
	public InspectionType findInspectionTypeByLegacyEventId(Long eventId, Long tenantId) {
		SecurityFilter filter = new TenantOnlySecurityFilter(tenantId);

		QueryBuilder<InspectionType> qBuilder = new QueryBuilder<InspectionType>(InspectionType.class, filter);
		qBuilder.setSimpleSelect().addSimpleWhere("legacyEventId", eventId);

		InspectionType inspectionType = null;
		try {
			inspectionType = persistenceManager.find(qBuilder);
		} catch (InvalidQueryException e) {
			logger.error("Failed while finding InspectionType by legacyEventId", e);
		}

		return inspectionType;
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
}
