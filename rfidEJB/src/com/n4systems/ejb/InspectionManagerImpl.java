package com.n4systems.ejb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.LegacyProductSerial;

import com.n4systems.ejb.interceptor.AuditInterceptor;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.FileAttachmentException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.exceptions.ProcessingProofTestException;
import com.n4systems.exceptions.SubProductUniquenessException;
import com.n4systems.exceptions.TransactionAlreadyProcessedException;
import com.n4systems.exceptions.UnknownSubProduct;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Product;
import com.n4systems.model.ProofTestInfo;
import com.n4systems.model.Status;
import com.n4systems.model.SubInspection;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.security.ManualSecurityFilter;
import com.n4systems.model.security.OpenSecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.CreateInspectionAuditHandler;
import com.n4systems.security.CustomAuditHandler;
import com.n4systems.security.UpdateInspectionAuditHandler;
import com.n4systems.services.InspectionScheduleServiceImpl;
import com.n4systems.services.NextInspectionScheduleService;
import com.n4systems.tools.FileDataContainer;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.TransactionSupervisor;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter.Comparator;
import com.n4systems.webservice.dto.WSJobSearchCriteria;
import com.n4systems.webservice.dto.WSSearchCritiera;

@Interceptors( { TimingInterceptor.class })
@Stateless
public class InspectionManagerImpl implements InspectionManager {
	private static final String unitName = "rfidEM";
	private static Logger logger = Logger.getLogger(InspectionManagerImpl.class);

	@PersistenceContext(unitName = unitName)
	private EntityManager em;

	@EJB protected LegacyProductSerial legacyProductManager;
	@EJB protected PersistenceManager persistenceManager;
	@EJB protected ProductManager productManager;
	
	@EJB protected InspectionScheduleManager inspectionScheduleManager;

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

	public FileDataContainer createFileDataContainer(Inspection inspection, File proofTestFile) throws ProcessingProofTestException {
		InputStream is = null;

		if (proofTestFile == null) {
			return null;
		}

		if (!proofTestFile.canRead()) {
			throw new ProcessingProofTestException("Cannot read from Proof Test file at [" + proofTestFile.getAbsolutePath() + "]");
		}

		FileDataContainer fileData = new FileDataContainer();

		try {
			is = new FileInputStream(proofTestFile);
			fileData.setFileData(IOUtils.toByteArray(is));
			inspection.getProofTestInfo().getProofTestType().getFileProcessorInstance().processFile(fileData);

		} catch (Exception e) {
			logger.error("Failed during processing of ProofTest file", e);
			throw new ProcessingProofTestException(e);
		} finally {
			IOUtils.closeQuietly(is);

			// clean up the temp proof test file
			proofTestFile.delete();
		}

		return fileData;
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
			
			savedInspection = createInspection(inspection, nextInspectionDates.get(inspection), inspection.getModifiedBy().getId(), (FileDataContainer) null, fileAttachments);
			
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

	@Interceptors({AuditInterceptor.class})
	@CustomAuditHandler(CreateInspectionAuditHandler.class)
	public Inspection createInspection(Inspection inspection, Date nextInspectionDate, Long userId) throws ProcessingProofTestException, FileAttachmentException,
			UnknownSubProduct {
		return createInspection(inspection, nextInspectionDate, userId, (FileDataContainer) null, (List<FileAttachment>) null);
	}

	@Interceptors({AuditInterceptor.class})
	@CustomAuditHandler(CreateInspectionAuditHandler.class)
	public Inspection createInspection(Inspection inspection, Date nextInspectionDate, Long userId, File proofTestFile, List<FileAttachment> uploadedFiles)
			throws ProcessingProofTestException, FileAttachmentException, UnknownSubProduct {
		return createInspection(inspection, nextInspectionDate, userId, createFileDataContainer(inspection, proofTestFile), uploadedFiles);
	}

	@Interceptors({AuditInterceptor.class})
	@CustomAuditHandler(CreateInspectionAuditHandler.class)
	public Inspection createInspection(Inspection inspection, Date nextInspectionDate, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles)
			throws ProcessingProofTestException, FileAttachmentException, UnknownSubProduct {

		// if the inspection has no group, lets create a new one now
		if (inspection.getGroup() == null) {
			inspection.setGroup(new InspectionGroup());
			inspection.getGroup().setTenant(inspection.getTenant());
			persistenceManager.save(inspection.getGroup(), userId);
		}
		
		// set the status from the state sets
		inspection.setStatus(calculateInspectionResult(inspection));

		// set proof test info on the inspection from a file data container
		setProofTestData(inspection, fileData);

		confirmSubInspectionsAreAgainstAttachedSubProducts(inspection);

		setOrderForSubInspections(inspection);
		
		// persist the inspection
		persistenceManager.save(inspection, userId);

		// update the product data
		updateProduct(inspection, userId);

		// update the next inspection date
		processNextInspection(inspection, nextInspectionDate);

		// save the proof test file and chart image to disk
		saveProofTestFiles(inspection, fileData);

		// save attachments to disk
		processUploadedFiles(inspection, uploadedFiles);

		return inspection;
	}

	private void confirmSubInspectionsAreAgainstAttachedSubProducts(Inspection inspection) throws UnknownSubProduct {
		Product product = persistenceManager.find(Product.class, inspection.getProduct().getId());
		product = productManager.fillInSubProductsOnProduct(product);
		for (SubInspection subInspection : inspection.getSubInspections()) {
			if (!product.getSubProducts().contains(new SubProduct(subInspection.getProduct(), null))) {
				throw new UnknownSubProduct("product id " + subInspection.getProduct().getId() + " is not attached to product " + product.getId());
			}
		}
	}
	
	private void setOrderForSubInspections(Inspection inspection) {
		Product product = persistenceManager.find(Product.class, inspection.getProduct().getId());
		product = productManager.fillInSubProductsOnProduct(product);
		List<SubInspection> reorderedSubInspections = new ArrayList<SubInspection>();
		for (SubProduct subProduct : product.getSubProducts()) {
			for (SubInspection subInspection : inspection.getSubInspections()) {
				if (subInspection.getProduct().equals(subProduct.getProduct())) {
					reorderedSubInspections.add(subInspection);
				}
			}
		}
		inspection.setSubInspections(reorderedSubInspections);
		
	}

	private Status calculateInspectionResult(Inspection inspection) {
		// determine result of the sections.
		Status inspectionResult = Status.NA;
		inspectionResult = findInspectionResult(inspection);
		for (SubInspection subInspection : inspection.getSubInspections()) {
			inspectionResult = adjustStatus(inspectionResult, findInspectionResult(subInspection));
			if (inspectionResult == Status.FAIL) {
				break;
			}
		}

		return inspectionResult;
	}

	private Status findInspectionResult(AbstractInspection inspection) {
		Status inspectionResult = Status.NA;
		for (CriteriaResult result : inspection.getResults()) {

			inspectionResult = adjustStatus(inspectionResult, result.getResult());

			if (inspectionResult == Status.FAIL) {
				break;
			}
		}
		return inspectionResult;
	}

	private Status adjustStatus(Status currentStatus, Status newStatus) {
		if (newStatus == Status.FAIL) {
			currentStatus = Status.FAIL;

		} else if (newStatus == Status.PASS) {
			currentStatus = Status.PASS;
		}
		return currentStatus;
	}

	@Interceptors({AuditInterceptor.class})
	@CustomAuditHandler(UpdateInspectionAuditHandler.class)
	public Inspection updateInspection(Inspection inspection, Long userId) throws ProcessingProofTestException, FileAttachmentException {
		return updateInspection(inspection, userId, (FileDataContainer) null, (List<FileAttachment>) null);
	}

	@Interceptors({AuditInterceptor.class})
	@CustomAuditHandler(UpdateInspectionAuditHandler.class)
	public Inspection updateInspection(Inspection inspection, Long userId, File proofTestFile, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		return updateInspection(inspection, userId, createFileDataContainer(inspection, proofTestFile), uploadedFiles);
	}

	@Interceptors({AuditInterceptor.class})
	@CustomAuditHandler(UpdateInspectionAuditHandler.class)
	public Inspection updateInspection(Inspection inspection, Long userId, FileDataContainer fileData, List<FileAttachment> uploadedFiles) throws ProcessingProofTestException, FileAttachmentException {
		setProofTestData(inspection, fileData);
		updateDeficiencies(inspection.getResults());
		inspection = persistenceManager.update(inspection, userId);
		
		updateProductInspectionDate(inspection.getProduct());
		inspection.setProduct(persistenceManager.update(inspection.getProduct()));
		saveProofTestFiles(inspection, fileData);
		processUploadedFiles(inspection, uploadedFiles);
		
		updateScheduleOwnerShip(inspection, userId);
		return inspection;
	}

	private void updateScheduleOwnerShip(Inspection inspection, Long userId) {
		InspectionSchedule schedule = inspection.getSchedule();
		if (schedule != null) {
			schedule.setOwner(inspection.getOwner());
			schedule.setLocation(inspection.getLocation());
			new InspectionScheduleServiceImpl(persistenceManager).updateSchedule(schedule);
		}
	}

	/**
	 * Updates changed Deficiencies on a list of CriteriaResults. Deficiencies
	 * are detected as being changed, iff their modified date is null. This
	 * should only be called on inspection update. If the deficiency is new
	 * create it.
	 * 
	 * @param results
	 *            The list of CriteriaResults to look for changed Deficiencies
	 *            on.
	 */
	private void updateDeficiencies(Set<CriteriaResult> results) {
		// walk through the results and deficiencies and update them if needed.
		for (CriteriaResult result : results) {
			CriteriaResult originalResult = persistenceManager.find(CriteriaResult.class, result.getId());
			originalResult.setDeficiencies(result.getDeficiencies());
			originalResult.setRecommendations(result.getRecommendations());
			persistenceManager.update(originalResult);
		}
	}

	public Inspection retireInspection(Inspection inspection, Long userId) {
		inspection.retireEntity();
		inspection = persistenceManager.update(inspection, userId);
		updateProductInspectionDate(inspection.getProduct());
		inspection.setProduct(persistenceManager.update(inspection.getProduct()));
		inspectionScheduleManager.restoreScheduleForInspection(inspection);
		return inspection;
	}

	private void processNextInspection(Inspection inspection, Date nextDate) {
		Product product = em.find(Product.class, inspection.getProduct().getId());
		
		if (nextDate != null) {
			NextInspectionScheduleService scheduleService = new NextInspectionScheduleService(product, inspection.getType(), nextDate, inspectionScheduleManager);
			scheduleService.createNextSchedule();
		} 
	}

	private void processUploadedFiles(Inspection inspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		attachUploadedFiles(inspection, null, uploadedFiles);

		for (SubInspection subInspection : inspection.getSubInspections()) {
			attachUploadedFiles(inspection, subInspection, null);
		}

		inspection = persistenceManager.update(inspection);
	}

	private Inspection attachUploadedFiles(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		File attachmentDirectory;
		AbstractInspection targetInspection;
		if (subInspection == null) {
			attachmentDirectory = PathHandler.getAttachmentFile(inspection);
			targetInspection = inspection;
		} else {
			attachmentDirectory = PathHandler.getAttachmentFile(inspection, subInspection);
			targetInspection = subInspection;
		}
		File tmpDirectory = PathHandler.getTempRoot();

		if (uploadedFiles != null) {

			File tmpFile;
			// move and attach each uploaded file
			for (FileAttachment uploadedFile : uploadedFiles) {

				try {
					// move the file to it's new location, note that it's
					// location is currently relative to the tmpDirectory
					tmpFile = new File(tmpDirectory, uploadedFile.getFileName());
					FileUtils.copyFileToDirectory(tmpFile, attachmentDirectory);

					// clean up the temp file
					tmpFile.delete();

					// now we need to set the correct file name for the
					// attachment and set the modifiedBy
					uploadedFile.setFileName(tmpFile.getName());
					uploadedFile.setTenant(targetInspection.getTenant());
					uploadedFile.setModifiedBy(targetInspection.getModifiedBy());

					// attach the attachment
					targetInspection.getAttachments().add(uploadedFile);
				} catch (IOException e) {
					logger.error("failed to copy uploaded file ", e);
					throw new FileAttachmentException(e);
				}

			}
		}

		// Now we need to cleanup any files that are no longer attached to the
		// inspection
		if (attachmentDirectory.exists()) {

			/*
			 * We'll start by constructing a list of attached file names which
			 * will be used in a directory filter
			 */
			final List<String> attachedFiles = new ArrayList<String>();
			for (FileAttachment file : targetInspection.getAttachments()) {
				attachedFiles.add(file.getFileName());
			}

			/*
			 * This lists all files in the attachment directory
			 */
			for (File detachedFile : attachmentDirectory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					// accept only files that are not in our attachedFiles list
					return !attachedFiles.contains(name);
				}
			})) {
				/*
				 * any file returned from our fileNotAttachedFilter, is not in
				 * our attached file list and should be removed
				 */
				detachedFile.delete();

			}
		}
		return inspection;
	}
	
	
	

	private void setProofTestData(Inspection inspection, FileDataContainer fileData) {
		if (fileData == null) {
			return;
		}

		if (inspection.getProofTestInfo() == null) {
			inspection.setProofTestInfo(new ProofTestInfo());
		}

		inspection.getProofTestInfo().setProofTestType(fileData.getFileType());
		inspection.getProofTestInfo().setDuration(fileData.getTestDuration());
		inspection.getProofTestInfo().setPeakLoad(fileData.getPeakLoad());
		inspection.getProofTestInfo().setPeakLoadDuration(fileData.getPeakLoadDuration());
	}

	/*
	 * Writes the file data and chart image back onto the file system from a
	 * fully constructed FileDataContainer
	 */
	private void saveProofTestFiles(Inspection inspection, FileDataContainer fileData) throws ProcessingProofTestException {
		if (fileData == null) {
			return;
		}
		File proofTestFile = PathHandler.getProofTestFile(inspection);
		File chartImageFile = PathHandler.getChartImageFile(inspection);

		// we should make sure our parent directories exist first
		proofTestFile.getParentFile().mkdirs();
		chartImageFile.getParentFile().mkdirs();

		try {
			if (fileData.getFileData() != null) {
				FileUtils.writeByteArrayToFile(proofTestFile, fileData.getFileData());
			} else if (proofTestFile.exists()) {
				proofTestFile.delete();
			}

			if (fileData.getChart() != null) {
				FileUtils.writeByteArrayToFile(chartImageFile, fileData.getChart());
			} else if (chartImageFile.exists()) {
				chartImageFile.delete();
			}

		} catch (IOException e) {
			logger.error("Failed while writing Proof Test files", e);
			throw new ProcessingProofTestException(e);
		}

	}

	private Product updateProductInspectionDate(Product product) {
		product.setLastInspectionDate(findLastInspectionDate(product));
		return product;
	}

	private void updateProduct(Inspection inspection, Long modifiedById) {
		UserBean modifiedBy = em.find(UserBean.class, modifiedById);
		Product product = em.find(Product.class, inspection.getProduct().getId());

		updateProductInspectionDate(product);

		// pushes the location and the ownership to the product based on the
		// inspections data.
		product.setOwner(inspection.getOwner());
		product.setLocation(inspection.getLocation());
		product.setProductStatus(inspection.getProductStatus());
		
		try {
			legacyProductManager.update(product, modifiedBy);
		} catch (SubProductUniquenessException e) {
			logger.error("received a subproduct uniquness error this should not be possible form this type of update.", e);
			throw new RuntimeException(e);
		}

	}
	
	/**
	 * This must be called AFTER the inspection and subinspection have been persisted
	 */
	public Inspection attachFilesToSubInspection(Inspection inspection, SubInspection subInspection, List<FileAttachment> uploadedFiles) throws FileAttachmentException {
		inspection = attachUploadedFiles(inspection, subInspection, uploadedFiles);
		return persistenceManager.update(inspection);
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
