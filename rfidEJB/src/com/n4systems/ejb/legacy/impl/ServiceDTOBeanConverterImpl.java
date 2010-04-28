package com.n4systems.ejb.legacy.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;



import javax.persistence.EntityManager;


import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.InspectionScheduleManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Deficiency;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.Observation;
import com.n4systems.model.Product;
import com.n4systems.model.ProductType;
import com.n4systems.model.ProductTypeGroup;
import com.n4systems.model.ProductTypeSchedule;
import com.n4systems.model.Project;
import com.n4systems.model.Recommendation;
import com.n4systems.model.State;
import com.n4systems.model.StateSet;
import com.n4systems.model.Status;
import com.n4systems.model.SubInspection;
import com.n4systems.model.SubProduct;
import com.n4systems.model.Tenant;
import com.n4systems.model.inspectionbook.InspectionBookByNameLoader;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.FindOwnerByLegacyIds;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.services.TenantCache;
import com.n4systems.util.BitField;
import com.n4systems.webservice.dto.AbstractBaseOrgServiceDTO;
import com.n4systems.webservice.dto.AbstractExternalOrgServiceDTO;
import com.n4systems.webservice.dto.AbstractInspectionServiceDTO;
import com.n4systems.webservice.dto.CriteriaResultServiceDTO;
import com.n4systems.webservice.dto.CriteriaSectionServiceDTO;
import com.n4systems.webservice.dto.CriteriaServiceDTO;
import com.n4systems.webservice.dto.CustomerOrgServiceDTO;
import com.n4systems.webservice.dto.DTOHasOwners;
import com.n4systems.webservice.dto.DivisionOrgServiceDTO;
import com.n4systems.webservice.dto.ImageServiceDTO;
import com.n4systems.webservice.dto.InfoFieldNameServiceDTO;
import com.n4systems.webservice.dto.InfoFieldServiceDTO;
import com.n4systems.webservice.dto.InspectionBookServiceDTO;
import com.n4systems.webservice.dto.InspectionInfoOptionServiceDTO;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;
import com.n4systems.webservice.dto.InspectionTypeServiceDTO;
import com.n4systems.webservice.dto.InternalOrgServiceDTO;
import com.n4systems.webservice.dto.JobServiceDTO;
import com.n4systems.webservice.dto.ObservationResultServiceDTO;
import com.n4systems.webservice.dto.ObservationServiceDTO;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupServiceDTO;
import com.n4systems.webservice.dto.ProductTypeScheduleServiceDTO;
import com.n4systems.webservice.dto.ProductTypeServiceDTO;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.StateServiceDTO;
import com.n4systems.webservice.dto.StateSetServiceDTO;
import com.n4systems.webservice.dto.SubInspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;
import com.n4systems.webservice.dto.TenantServiceDTO;
import com.n4systems.webservice.dto.VendorServiceDTO;
import com.n4systems.webservice.dto.ObservationResultServiceDTO.ObservationState;
import com.n4systems.webservice.dto.ObservationResultServiceDTO.ObservationType;

import fieldid.web.services.dto.AbstractBaseServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;

/**
 * This class converts from ServiceDTOs to beans and from beans back to
 * serviceDTOs
 * 
 * @author Jesse Miller
 * 
 */

public class ServiceDTOBeanConverterImpl implements ServiceDTOBeanConverter {
	private static final Logger logger = Logger.getLogger(ServiceDTOBeanConverter.class);
	public static final long NULL_ID = -1024L;
	public static final String GENERATE_SERIAL_NUMBER = "[[GENERATE]]";

	
	protected EntityManager em;

	
	private PersistenceManager persistenceManager;
	
	private InspectionScheduleManager inspectionScheduleManager;
	
	private SerialNumberCounter serialNumberCounter;

	
	
	
	public ServiceDTOBeanConverterImpl(){}
	
	public ServiceDTOBeanConverterImpl(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.inspectionScheduleManager = new InspectionScheduleManagerImpl(em);
		this.serialNumberCounter = new SerialNumberCounterManager(em);
	}

	/**
	 * Given a non-null, non-zero id, looks up an entity of type clazz from the
	 * database. If id is equal to {@link #NULL_ID} returns null. Returns
	 * currentValue otherwise. Used to implement logic of null means 'do not
	 * change' and {@link #NULL_ID} means set null.
	 * 
	 * @param clazz
	 *            Class of the field
	 * @param id
	 *            Long id from the webservice
	 * @param currentValue
	 *            Current value of the field
	 * @return The current value, loaded value, or null
	 */
	private <T> T convertField(Class<T> clazz, Long id, T currentValue) {
		T decoded = currentValue;

		if (id != null && id > 0) {
			decoded = em.find(clazz, id);
		} else if (id == NULL_ID) {
			decoded = null;
		}

		return decoded;
	}

	public InspectionBookServiceDTO convert(InspectionBook inspectionBook) {

		InspectionBookServiceDTO bookDTO = new InspectionBookServiceDTO();
		bookDTO.setName(inspectionBook.getName());
		bookDTO.setBookOpen(inspectionBook.isOpen());
		bookDTO.setId(inspectionBook.getId());

		populateOwners(inspectionBook.getOwner(), bookDTO);

		return bookDTO;
	}

	private void populateAbstractInspectionInfo(AbstractInspectionServiceDTO inspectionDTO, AbstractInspection inspection) {
		inspectionDTO.setComments(inspection.getComments());
		inspectionDTO.setFormVersion(inspection.getFormVersion());
		inspectionDTO.setId(inspection.getId());

		Map<String, String> infoOptionMap = inspection.getInfoOptionMap();
		Set<String> infoOptionKeys = inspection.getInfoOptionMap().keySet();
		Iterator<String> It = infoOptionKeys.iterator();
		while (It.hasNext()) {
			String infoFieldName = (String) (It.next());
			inspectionDTO.getInfoOptions().add(convert(inspection.getId(), infoFieldName, infoOptionMap.get(infoFieldName)));
		}

		inspectionDTO.setInspectionTypeId(inspection.getType().getId());
		inspectionDTO.setProductId(inspection.getProduct().getId());
		for (CriteriaResult criteriaResult : inspection.getResults()) {
			inspectionDTO.getResults().add(convert(criteriaResult));
		}

	}

	public com.n4systems.webservice.dto.InspectionServiceDTO convert(Inspection inspection) {

		com.n4systems.webservice.dto.InspectionServiceDTO inspectionDTO = new com.n4systems.webservice.dto.InspectionServiceDTO();
		persistenceManager.reattach(inspection, false);

		populateAbstractInspectionInfo(inspectionDTO, inspection);

		inspectionDTO.setOwnerId(retrieveOwnerId(inspection.getOwner()));
		inspectionDTO.setLocation( inspection.getLocation() );
		inspectionDTO.setPerformedById( inspection.getInspector().getUniqueID() );
		inspectionDTO.setStatus( inspection.getStatus().name() );
		inspectionDTO.setInspectionBookId( ( inspection.getBook() != null ) ? inspection.getBook().getId() : 0L );
		inspectionDTO.setUtcDate(inspection.getDate());

		populateOwners(inspection.getOwner(), inspectionDTO);

		// TODO convert date to their time zone
		inspectionDTO.setDate(AbstractBaseServiceDTO.dateToString(inspection.getDate()));

		for (SubInspection subInspection : inspection.getSubInspections()) {
			inspectionDTO.getSubInspections().add(convert(subInspection));
		}

		return inspectionDTO;

	}

	public SubInspectionServiceDTO convert(SubInspection subInspection) {
		SubInspectionServiceDTO subInspectionDTO = new SubInspectionServiceDTO();

		populateAbstractInspectionInfo(subInspectionDTO, subInspection);

		subInspectionDTO.setName(subInspection.getName());

		return subInspectionDTO;
	}

	public List<com.n4systems.webservice.dto.InspectionServiceDTO> convert(InspectionGroup inspectionGroup) {
		persistenceManager.reattach(inspectionGroup, false);

		List<com.n4systems.webservice.dto.InspectionServiceDTO> inspectionDTOs = new ArrayList<com.n4systems.webservice.dto.InspectionServiceDTO>();
		for (Inspection inspection : inspectionGroup.getAvailableInspections()) {
			inspectionDTOs.add(convert(inspection));
		}

		return inspectionDTOs;
	}

	public ProductServiceDTO convert(Product product) {

		ProductServiceDTO productDTO = new ProductServiceDTO();

		persistenceManager.reattach(product, false);

		populateOwners(product.getOwner(), productDTO);

		productDTO.setId(product.getId());
		productDTO.setCustomerRefNumber(product.getCustomerRefNumber());
		productDTO.setIdentified(AbstractBaseServiceDTO.dateToString(product.getIdentified()));
		productDTO.setLastInspectionDate(AbstractBaseServiceDTO.dateToString(product.getLastInspectionDate()));
		productDTO.setLocation(product.getLocation());
		productDTO.setMobileGuid(product.getMobileGUID());
		productDTO.setProductStatusId(product.getProductStatus() != null ? product.getProductStatus().getUniqueID() : 0);
		productDTO.setProductTypeId(product.getType().getId());
		productDTO.setPurchaseOrder(product.getPurchaseOrder());
		productDTO.setRfidNumber(product.getRfidNumber() == null ? null : product.getRfidNumber().toUpperCase());
		productDTO.setSerialNumber(product.getSerialNumber());
		productDTO.setComments(product.getComments());
		productDTO.setIdentifiedById(product.getIdentifiedBy() != null ? product.getIdentifiedBy().getUniqueID() : 0);
		productDTO.setModifiedById(product.getModifiedBy() != null ? product.getModifiedBy().getUniqueID() : 0);
		productDTO.setOrderNumber(product.getShopOrder() != null ? product.getShopOrder().getOrder().getOrderNumber() : "");
		productDTO.setModified(product.getModified());

		if (product.getDescription() != null && product.getDescription().length() >= 255) {
			productDTO.setDescription(product.getDescription().substring(0, 255));
		} else {
			productDTO.setDescription(product.getDescription());
		}

		for (InfoOptionBean infoOption : product.getInfoOptions()) {
			productDTO.getInfoOptions().add(convert(infoOption, infoOption.getInfoField().getUniqueID()));
		}

		new FindSubProducts(persistenceManager, product).fillInSubProducts();
		if (product.getSubProducts() != null) {
			SubProductMapServiceDTO subProductMap = null;
			for (SubProduct subProduct : product.getSubProducts()) {
				subProductMap = new SubProductMapServiceDTO();
				subProductMap.setName(subProduct.getLabel());
				subProductMap.setSubProductId(subProduct.getProduct().getId());
				subProductMap.setProductId(product.getId());
				productDTO.getSubProducts().add(subProductMap);
			}
		}

		List<InspectionSchedule> schedules = inspectionScheduleManager.getAvailableSchedulesFor(product);
		for (InspectionSchedule schedule : schedules) {
			productDTO.getSchedules().add(convert(schedule));
		}

		return productDTO;
	}

	public Product convert(ProductServiceDTO productServiceDTO, Product targetProduct, long tenantId) {

		Tenant tenantOrganization = TenantCache.getInstance().findTenant(tenantId);
		PrimaryOrg primaryOrg = TenantCache.getInstance().findPrimaryOrg(tenantOrganization.getId());

		targetProduct.setComments(productServiceDTO.getComments());
		targetProduct.setCustomerRefNumber(productServiceDTO.getCustomerRefNumber());
		targetProduct.setLocation(productServiceDTO.getLocation());
		targetProduct.setType(em.find(ProductType.class, productServiceDTO.getProductTypeId()));
		targetProduct.setPurchaseOrder(productServiceDTO.getPurchaseOrder());
		targetProduct.setRfidNumber(productServiceDTO.getRfidNumber());
		targetProduct.setTenant(tenantOrganization);

		if (productServiceDTO.getSerialNumber().equals(GENERATE_SERIAL_NUMBER)) {
			targetProduct.setSerialNumber(serialNumberCounter.generateSerialNumber(primaryOrg));
		} else {
			targetProduct.setSerialNumber(productServiceDTO.getSerialNumber());
		}

		BaseOrg owner = null;
		if (productServiceDTO.ownerIdExists()) {
			owner = persistenceManager.find(BaseOrg.class, productServiceDTO.getOwnerId(), new TenantOnlySecurityFilter(tenantId));
		} else {
			// This is here to support mobiles before version 1.14
			FindOwnerByLegacyIds ownerFinder = getFindOwnerByLegacyIds(tenantId);
			ownerFinder.setLegacyCustomerId(productServiceDTO.customerExists() ? productServiceDTO.getCustomerId() : null);
			ownerFinder.setLegacyDivisionId(productServiceDTO.divisionExists() ? productServiceDTO.getDivisionId() : null);
			ownerFinder.setLegacyJobSiteId(productServiceDTO.jobSiteExists() ? productServiceDTO.getJobSiteId() : null);

			owner = ownerFinder.retrieveOwner();
		}
		targetProduct.setOwner(owner);

		targetProduct.setProductStatus(convertField(ProductStatusBean.class, productServiceDTO.getProductStatusId(), targetProduct.getProductStatus()));

		if (productServiceDTO.identifiedByExists()) {
			UserBean user = em.find(UserBean.class, productServiceDTO.getIdentifiedById());
			targetProduct.setIdentifiedBy(user);
		}

		if (productServiceDTO.modifiedByIdExists()) {
			UserBean modifiedBy = em.find(UserBean.class, productServiceDTO.getModifiedById());
			targetProduct.setModifiedBy(modifiedBy);
		}

		if (productServiceDTO.getInfoOptions() != null) {
			targetProduct.setInfoOptions(convertInfoOptions(productServiceDTO));
		}

		if (convertStringToDate(productServiceDTO.getIdentified()) != null) {
			targetProduct.setIdentified(convertStringToDate(productServiceDTO.getIdentified()));
		}

		if (targetProduct.isNew()) {
			targetProduct.setMobileGUID(productServiceDTO.getMobileGuid());
		}

		return targetProduct;
	}

	public Set<InfoOptionBean> convertInfoOptions(ProductServiceDTO productServiceDTO) {
		Set<InfoOptionBean> infoOptions = new TreeSet<InfoOptionBean>();
		for (com.n4systems.webservice.dto.InfoOptionServiceDTO infoOptionServiceDTO : productServiceDTO.getInfoOptions()) {
			try {
				infoOptions.add(convert(infoOptionServiceDTO));
			} catch (MissingEntityException e) {

			}
		}
		return infoOptions;
	}

	protected FindOwnerByLegacyIds getFindOwnerByLegacyIds(long tenantId) {
		FindOwnerByLegacyIds ownerFinder = new FindOwnerByLegacyIds(persistenceManager, tenantId);
		return ownerFinder;
	}

	public InfoOptionBean convert(com.n4systems.webservice.dto.InfoOptionServiceDTO infoOptionServiceDTO) {
		InfoOptionBean infoOption = null;

		if (!infoOptionServiceDTO.isCreatedOnMobile()) {
			infoOption = em.find(InfoOptionBean.class, infoOptionServiceDTO.getId());
		}

		if (infoOption == null) {
			infoOption = new InfoOptionBean();
			infoOption.setInfoField(findInfoField(infoOptionServiceDTO));
			infoOption.setName(infoOptionServiceDTO.getName());
		}

		return infoOption;
	}

	private InfoFieldBean findInfoField(com.n4systems.webservice.dto.InfoOptionServiceDTO infoOptionServiceDTO) {

		InfoFieldBean infoField = em.find(InfoFieldBean.class, infoOptionServiceDTO.getInfoFieldId());
		if (infoField == null) {
			throw new MissingEntityException(String.format("Could not find info field [%d]", infoOptionServiceDTO.getInfoFieldId()));
		}
		return infoField;
	}

	/**
	 * Populates an abstract inspection with the fields from an abstract
	 * inspection service dto
	 * 
	 * @param inspection
	 * @param inspectionDTO
	 */
	private void populate(AbstractInspection inspection, AbstractInspectionServiceDTO inspectionServiceDTO, Tenant tenant) {

		inspection.setComments(inspectionServiceDTO.getComments());

		// Required object lookups
		inspection.setTenant(tenant);
		inspection.setType(persistenceManager.find(InspectionType.class, inspectionServiceDTO.getInspectionTypeId(), new TenantOnlySecurityFilter(tenant.getId())));
		inspection.setProduct((Product) em.find(Product.class, inspectionServiceDTO.getProductId()));

		// Optional object lookups
		if (inspectionServiceDTO.getResults() != null) {
			inspection.setResults(convert(inspectionServiceDTO.getResults(), tenant, inspection));
		}

		if (inspectionServiceDTO.getInfoOptions() != null) {
			for (InspectionInfoOptionServiceDTO infoOption : inspectionServiceDTO.getInfoOptions()) {
				inspection.getInfoOptionMap().put(infoOption.getInfoFieldName(), infoOption.getInfoOptionValue());
			}
		}
		inspection.setFormVersion(inspectionServiceDTO.getFormVersion());

		inspection.setMobileGUID(inspectionServiceDTO.getInspectionMobileGUID());

	}

	public Inspection convert(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO, Long tenantId) throws IOException {

		Tenant tenant = persistenceManager.find(Tenant.class, tenantId);

		Inspection inspection = new Inspection();

		populate(inspection, inspectionServiceDTO, tenant);

		inspection.setLocation(inspectionServiceDTO.getLocation());
		inspection.setPrintable(inspectionServiceDTO.isPrintable());

		// Check if utcDate is set, if not, dealing with a PRE 1.11 version: use
		// date
		if (inspectionServiceDTO.getUtcDate() != null) {
			inspection.setDate(inspectionServiceDTO.getUtcDate());
		} else {
			// TODO convert from their set timezone
			inspection.setDate( convertStringToDate(inspectionServiceDTO.getDate()) );
		}		
		
		// Required object lookups		
		UserBean inspector = (UserBean)em.find(UserBean.class, inspectionServiceDTO.getPerformedById());
		inspection.setModifiedBy( inspector );		
		inspection.setInspector( inspector );


		BaseOrg owner = null;
		if (inspectionServiceDTO.ownerIdExists()) {
			owner = persistenceManager.find(BaseOrg.class, inspectionServiceDTO.getOwnerId(), new TenantOnlySecurityFilter(tenantId));
		} else {
			// This is for mobile versions PRE 1.14
			FindOwnerByLegacyIds ownerFinder = new FindOwnerByLegacyIds(persistenceManager, tenantId);
			ownerFinder.setLegacyCustomerId(inspectionServiceDTO.customerExists() ? inspectionServiceDTO.getCustomerId() : null);
			ownerFinder.setLegacyDivisionId(inspectionServiceDTO.divisionExists() ? inspectionServiceDTO.getDivisionId() : null);
			ownerFinder.setLegacyJobSiteId(inspectionServiceDTO.jobSiteExists() ? inspectionServiceDTO.getJobSiteId() : null);
			owner = ownerFinder.retrieveOwner();
		}
		inspection.setOwner(owner);

		if (inspectionServiceDTO.inspectionBookExists()) {
			inspection.setBook(persistenceManager.find(InspectionBook.class, inspectionServiceDTO.getInspectionBookId()));
		} else if (inspectionServiceDTO.getInspectionBookTitle() != null) {

			InspectionBookByNameLoader loader = new InspectionBookByNameLoader(new OrgOnlySecurityFilter(inspector.getOwner()));
			loader.setName(inspectionServiceDTO.getInspectionBookTitle());
			loader.setOwner(owner);
			InspectionBook inspectionBook = loader.load(em, new OrgOnlySecurityFilter(inspector.getOwner()));

			if (inspectionBook == null) {
				inspectionBook = new InspectionBook();
				inspectionBook.setName(inspectionServiceDTO.getInspectionBookTitle());
				inspectionBook.setOwner(inspection.getOwner());
				inspectionBook.setTenant(tenant);

				persistenceManager.save(inspectionBook);
			}

			inspection.setBook(inspectionBook);
		}

		if (inspectionServiceDTO.inspectionGroupExists()) {
			inspection.setGroup(persistenceManager.find(InspectionGroup.class, inspectionServiceDTO.getInspectionGroupId()));
		}

		if (inspectionServiceDTO.getStatus() != null) {
			inspection.setStatus(Status.valueOf(inspectionServiceDTO.getStatus()));
		}

		if (inspectionServiceDTO.getSubInspections() != null) {
			for (SubInspectionServiceDTO subInspection : inspectionServiceDTO.getSubInspections()) {
				inspection.getSubInspections().add(convert(subInspection, tenant, inspector));
			}
		}

		inspection.setProductStatus(convertProductStatus(inspectionServiceDTO));

		inspection.getAttachments().addAll(convertToFileAttachmentsAndWriteToTemp(inspectionServiceDTO.getImages(), tenant, inspector));

		return inspection;
	}

	public FileAttachment convert(AbstractInspection inspection, com.n4systems.webservice.dto.InspectionImageServiceDTO inspectionImageServiceDTO, UserBean inspector) throws IOException {
		return convertToFileAttachment(inspectionImageServiceDTO.getImage(), inspection.getTenant(), inspector);

	}

	private FileAttachment convertToFileAttachment(ImageServiceDTO imageServiceDTO, Tenant tenant, UserBean modifiedBy) throws IOException {
		// some files come prepended with a '\'. We should remove these here.
		String fileName = imageServiceDTO.getFileName().replace("\\", "");

		FileAttachment fileAttachment = new FileAttachment(tenant, modifiedBy, fileName);
		fileAttachment.setComments(imageServiceDTO.getComments());

		return fileAttachment;
	}

	private FileAttachment convertFileAttachmentAndWriteToTemp(ImageServiceDTO imageServiceDTO, Tenant tenant, UserBean modifiedBy) throws IOException {
		FileAttachment fileAttachment = null;

		try {
			File tempImageFile = PathHandler.getTempFile(imageServiceDTO.getFileName());
			tempImageFile.getParentFile().mkdirs();

			FileOutputStream fileOut = new FileOutputStream(tempImageFile);
			fileOut.write(imageServiceDTO.getImage());

			// Must get the full path name and then remove the temporary root
			// path to conform to how the processor accepts it
			String fileName = tempImageFile.getPath();
			fileName = fileName.substring(fileName.indexOf(PathHandler.getTempRoot().getPath()) + PathHandler.getTempRoot().getPath().length());

			fileAttachment = new FileAttachment(tenant, modifiedBy, fileName);
			fileAttachment.setComments(imageServiceDTO.getComments());

		} catch (IOException e) {
			logger.error("Problem saving images from mobile", e);
			throw e;
		}

		return fileAttachment;
	}

	private List<FileAttachment> convertToFileAttachmentsAndWriteToTemp(List<ImageServiceDTO> images, Tenant tenant, UserBean modifiedBy) throws IOException {
		List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();

		if (images != null) {
			for (ImageServiceDTO imageServiceDTO : images) {
				fileAttachments.add(convertFileAttachmentAndWriteToTemp(imageServiceDTO, tenant, modifiedBy));
			}
		}

		return fileAttachments;
	}

	private SubInspection convert(SubInspectionServiceDTO subInspectionServiceDTO, Tenant tenant, UserBean inspector) throws IOException {
		SubInspection subInspection = new SubInspection();
		populate(subInspection, subInspectionServiceDTO, tenant);
		subInspection.setName(subInspectionServiceDTO.getName());
		subInspection.getAttachments().addAll(convertToFileAttachmentsAndWriteToTemp(subInspectionServiceDTO.getImages(), tenant, inspector));

		return subInspection;
	}

	private Set<CriteriaResult> convert(List<CriteriaResultServiceDTO> resultDTOs, Tenant tenant, AbstractInspection inspection) {

		Set<CriteriaResult> results = new HashSet<CriteriaResult>();

		for (CriteriaResultServiceDTO resultDTO : resultDTOs) {
			CriteriaResult result = new CriteriaResult();
			result.setState(persistenceManager.find(State.class, resultDTO.getStateId()));
			result.setCriteria(persistenceManager.find(Criteria.class, resultDTO.getCriteriaId()));
			result.setTenant(tenant);
			result.setInspection(inspection);

			if (resultDTO.getRecommendations() != null) {
				for (ObservationResultServiceDTO recommendationDTO : resultDTO.getRecommendations()) {
					result.getRecommendations().add(convertRecommendation(recommendationDTO, tenant));
				}
			}

			if (resultDTO.getDeficiencies() != null) {
				for (ObservationResultServiceDTO deficiencyDTO : resultDTO.getDeficiencies()) {
					result.getDeficiencies().add(convertDeficiency(deficiencyDTO, tenant));
				}
			}

			results.add(result);
		}

		return results;
	}

	private Recommendation convertRecommendation(ObservationResultServiceDTO recommendationDTO, Tenant tenant) {
		Recommendation recommendation = new Recommendation();
		recommendation.setText(recommendationDTO.getText());
		recommendation.setTenant(tenant);
		recommendation.setState(convert(recommendationDTO.getState()));
		return recommendation;
	}

	private Deficiency convertDeficiency(ObservationResultServiceDTO deficiencyDTO, Tenant tenant) {
		Deficiency deficiency = new Deficiency();
		deficiency.setTenant(tenant);
		deficiency.setText(deficiencyDTO.getText());
		deficiency.setState(convert(deficiencyDTO.getState()));
		return deficiency;
	}

	private Observation.State convert(ObservationResultServiceDTO.ObservationState state) {
		switch (state) {
		case COMMENT:
			return Observation.State.COMMENT;
		case OUTSTANDING:
			return Observation.State.OUTSTANDING;
		case REPAIRED:
			return Observation.State.REPAIRED;
		case REPAIREDONSITE:
			return Observation.State.REPAIREDONSITE;
		}

		return Observation.State.COMMENT;
	}

	public InspectionSchedule convertInspectionSchedule(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO) {
		InspectionSchedule schedule = null;

		if (inspectionServiceDTO.inspectionScheduleExists()) {
			schedule = persistenceManager.find(InspectionSchedule.class, inspectionServiceDTO.getInspectionScheduleId());
		}

		return schedule;
	}

	public ProductStatusServiceDTO convert(ProductStatusBean productStatus) {

		ProductStatusServiceDTO productStatusServiceDTO = new ProductStatusServiceDTO();
		productStatusServiceDTO.setId(productStatus.getUniqueID());
		productStatusServiceDTO.setName(productStatus.getName());
		productStatusServiceDTO.setTenantId(productStatus.getTenant().getId());
		productStatusServiceDTO.setCreated(productStatus.getDateCreated().toString());
		productStatusServiceDTO.setModified(productStatus.getDateModified().toString());
		productStatusServiceDTO.setModifiedBy(productStatus.getModifiedBy());

		return productStatusServiceDTO;
	}

	private ProductTypeScheduleServiceDTO convert(ProductTypeSchedule productTypeSchedule) {
		ProductTypeScheduleServiceDTO productTypeScheduleServiceDTO = new ProductTypeScheduleServiceDTO();
		productTypeScheduleServiceDTO.setDtoVersion(ProductTypeScheduleServiceDTO.CURRENT_DTO_VERSION);
		productTypeScheduleServiceDTO.setInspectionTypeId(productTypeSchedule.getInspectionType().getId());
		productTypeScheduleServiceDTO.setFrequency(productTypeSchedule.getFrequency());
		productTypeScheduleServiceDTO.setId(productTypeSchedule.getId());
		productTypeScheduleServiceDTO.setProductTypeId(productTypeSchedule.getProductType().getId());
		populateOwners(productTypeSchedule.getOwner(), productTypeScheduleServiceDTO);
		return productTypeScheduleServiceDTO;
	}

	@SuppressWarnings("deprecation")
	public ProductTypeServiceDTO convert_new(ProductType productType) {

		ProductTypeServiceDTO productTypeDTO = new ProductTypeServiceDTO();
		productTypeDTO.setDtoVersion(ProductTypeServiceDTO.CURRENT_DTO_VERSION);
		productTypeDTO.setId(productType.getId());
		productTypeDTO.setName(productType.getName());

		for (InfoFieldBean infoField : productType.getInfoFields()) {
			if (!infoField.isRetired()) {
				productTypeDTO.getInfoFields().add(convert_new(infoField, productType.getId()));
			}
		}

		for (InspectionType inspectionType : productType.getInspectionTypes()) {
			productTypeDTO.getInspectionTypeIds().add(inspectionType.getId());
		}

		for (ProductTypeSchedule schedule : productType.getSchedules()) {
			productTypeDTO.getSchedules().add(convert(schedule));
		}

		for (ProductType subType : productType.getSubTypes()) {
			productTypeDTO.getSubTypes().add(subType.getId());
		}

		productTypeDTO.setGroupId(productType.getGroup() != null ? productType.getGroup().getId() : NULL_ID);
		productTypeDTO.setMaster(productType.isMaster());

		return productTypeDTO;
	}

	public InspectionTypeServiceDTO convert(InspectionType inspectionType) {

		InspectionTypeServiceDTO inspectionTypeService = new InspectionTypeServiceDTO();
		inspectionTypeService.setDescription(inspectionType.getDescription());
		inspectionTypeService.setId(inspectionType.getId());
		inspectionTypeService.setName(inspectionType.getName());
		inspectionTypeService.setPrintable(inspectionType.isPrintable());
		inspectionTypeService.setMaster(inspectionType.isMaster());
		inspectionTypeService.setGroupId(inspectionType.getGroup().getId());
		inspectionTypeService.setFormVersion(inspectionType.getFormVersion());

		for (CriteriaSection section : inspectionType.getSections()) {
			if (!section.isRetired()) {
				inspectionTypeService.getSections().add(convert(section, inspectionType.getId()));
			}
		}

		// Info Field Names ; sent with an order index
		int i = 0;
		for (String infoFieldName : inspectionType.getInfoFieldNames()) {
			InfoFieldNameServiceDTO infoField = new InfoFieldNameServiceDTO();
			infoField.setName(infoFieldName);
			infoField.setOrderIndex(i);
			inspectionTypeService.getInfoFieldNames().add(infoField);
			i++;
		}

		return inspectionTypeService;
	}

	private CriteriaSectionServiceDTO convert(CriteriaSection section, Long inspectionTypeId) {

		CriteriaSectionServiceDTO sectionServiceDTO = new CriteriaSectionServiceDTO();
		sectionServiceDTO.setId(section.getId());
		sectionServiceDTO.setTitle(section.getTitle());
		sectionServiceDTO.setInspectionTypeId(inspectionTypeId);

		for (Criteria criteria : section.getCriteria()) {
			if (!criteria.isRetired()) {
				sectionServiceDTO.getCriteria().add(convert(criteria, section.getId()));
			}
		}

		return sectionServiceDTO;
	}

	private CriteriaServiceDTO convert(Criteria criteria, Long criteriaSectionId) {

		CriteriaServiceDTO criteriaServiceDTO = new CriteriaServiceDTO();
		criteriaServiceDTO.setId(criteria.getId());
		criteriaServiceDTO.setDisplayText(criteria.getDisplayText());
		criteriaServiceDTO.setPrincipal(criteria.isPrincipal());
		criteriaServiceDTO.setCriteriaSectionId(criteriaSectionId);
		criteriaServiceDTO.setStateSetId(criteria.getStates().getId());

		int i = 0;
		for (String recommendation : criteria.getRecommendations()) {
			criteriaServiceDTO.getRecommendations().add(convert(recommendation, ObservationServiceDTO.RECOMMENDATION, i, criteria.getId()));
			i++;
		}

		i = 0;
		for (String deficiency : criteria.getDeficiencies()) {
			criteriaServiceDTO.getDeficiencies().add(convert(deficiency, ObservationServiceDTO.DEFICIENCY, i, criteria.getId()));
			i++;
		}

		return criteriaServiceDTO;
	}

	private CriteriaResultServiceDTO convert(CriteriaResult criteriaResult) {

		CriteriaResultServiceDTO criteriaResultServiceDTO = new CriteriaResultServiceDTO();
		criteriaResultServiceDTO.setId(criteriaResult.getId());
		criteriaResultServiceDTO.setCriteriaId(criteriaResult.getCriteria().getId());
		criteriaResultServiceDTO.setInspectionId(criteriaResult.getInspection().getId());
		criteriaResultServiceDTO.setStateId(criteriaResult.getState().getId());

		int i = 0;
		for (Recommendation recommendation : criteriaResult.getRecommendations()) {
			criteriaResultServiceDTO.getRecommendations().add(convert(recommendation, ObservationServiceDTO.RECOMMENDATION, i, criteriaResult.getId()));
			i++;
		}

		i = 0;
		for (Deficiency deficiency : criteriaResult.getDeficiencies()) {
			criteriaResultServiceDTO.getDeficiencies().add(convert(deficiency, ObservationServiceDTO.DEFICIENCY, i, criteriaResult.getId()));
			i++;
		}

		return criteriaResultServiceDTO;
	}

	private ObservationType convert(Observation.Type type) {
		ObservationType observationType = null;
		switch (type) {
		case DEFICIENCY:
			observationType = ObservationType.DEFICIENCY;
			break;
		case RECOMMENDATION:
			observationType = ObservationType.RECOMENDATION;
			break;
		}
		return observationType;
	}

	private ObservationState convert(Observation.State state) {
		ObservationState observationState = null;
		switch (state) {
		case COMMENT:
			observationState = ObservationState.COMMENT;
			break;
		case OUTSTANDING:
			observationState = ObservationState.OUTSTANDING;
			break;
		case REPAIRED:
			observationState = ObservationState.REPAIRED;
			break;
		case REPAIREDONSITE:
			observationState = ObservationState.REPAIREDONSITE;
			break;
		}
		return observationState;
	}

	private ObservationResultServiceDTO convert(Observation observation, String typeDiscriminator, int orderIndex, long criteriaResultId) {

		ObservationResultServiceDTO observationResultServiceDTO = new ObservationResultServiceDTO();

		observationResultServiceDTO.setState(convert(observation.getState()));
		observationResultServiceDTO.setText(observation.getText());
		observationResultServiceDTO.setType(convert(observation.getType()));

		observationResultServiceDTO.setCriteriaResultId(criteriaResultId);
		observationResultServiceDTO.setOrderIndex(orderIndex);

		return observationResultServiceDTO;

	}

	private InspectionInfoOptionServiceDTO convert(long inspectionId, String infoFieldName, String infoOptionValue) {

		InspectionInfoOptionServiceDTO inspectionInfoOptionServiceDTO = new InspectionInfoOptionServiceDTO();
		inspectionInfoOptionServiceDTO.setInspectionId(inspectionId);
		inspectionInfoOptionServiceDTO.setInfoFieldName(infoFieldName);
		inspectionInfoOptionServiceDTO.setInfoOptionValue(infoOptionValue);

		return inspectionInfoOptionServiceDTO;
	}

	private ObservationServiceDTO convert(String observationText, String typeDiscriminator, int orderIndex, long criteriaId) {
		ObservationServiceDTO observation = new ObservationServiceDTO();
		observation.setObservationText(observationText);
		observation.setTypeDiscriminator(typeDiscriminator);
		observation.setOrderIndex(orderIndex);
		observation.setCriteriaId(criteriaId);

		return observation;
	}

	public StateSetServiceDTO convert(StateSet stateSet) {
		StateSetServiceDTO stateSetServiceDTO = new StateSetServiceDTO();
		stateSetServiceDTO.setId(stateSet.getId());
		stateSetServiceDTO.setName(stateSet.getName());

		for (State state : stateSet.getStates()) {
			stateSetServiceDTO.getStates().add(convert(state, stateSet.getId()));
		}

		return stateSetServiceDTO;
	}

	private StateServiceDTO convert(State state, Long stateSetId) {

		StateServiceDTO stateServiceDTO = new StateServiceDTO();
		stateServiceDTO.setId(state.getId());
		stateServiceDTO.setButtonName(state.getButtonName());
		stateServiceDTO.setDisplayText(state.getDisplayText());
		stateServiceDTO.setStatus(state.getStatus().name());
		stateServiceDTO.setStateSetId(stateSetId);
		stateServiceDTO.setRetired(state.isRetired());

		return stateServiceDTO;
	}

	private InfoFieldServiceDTO convert_new(InfoFieldBean infoField, Long productTypeId) {
		InfoFieldServiceDTO infoFieldDTO = new InfoFieldServiceDTO();
		infoFieldDTO.setDtoVersion(InfoFieldServiceDTO.CURRENT_DTO_VERSION);
		infoFieldDTO.setId(infoField.getUniqueID());
		infoFieldDTO.setFieldType(infoField.getFieldType());
		infoFieldDTO.setName(infoField.getName());
		infoFieldDTO.setRequired(infoField.isRequired());
		infoFieldDTO.setUsingUnitOfMeasure(infoField.isUsingUnitOfMeasure());
		infoFieldDTO.setWeight(infoField.getWeight());
		infoFieldDTO.setProductTypeId(productTypeId);
		if (infoField.getUnitOfMeasure() != null) {
			infoFieldDTO.setDefaultUnitOfMeasureId(infoField.getUnitOfMeasure().getId());
		}

		// Put together the list of only static info options; dynamic ones are
		// sent with their product
		List<com.n4systems.webservice.dto.InfoOptionServiceDTO> infoOptions = new ArrayList<com.n4systems.webservice.dto.InfoOptionServiceDTO>();
		for (InfoOptionBean infoOption : infoField.getInfoOptions()) {
			infoOptions.add(convert(infoOption, infoField.getUniqueID()));
		}
		infoFieldDTO.setInfoOptions(infoOptions);

		return infoFieldDTO;
	}

	private com.n4systems.webservice.dto.InfoOptionServiceDTO convert(InfoOptionBean infoOption, Long infoFieldId) {
		com.n4systems.webservice.dto.InfoOptionServiceDTO infoOptionDTO = new com.n4systems.webservice.dto.InfoOptionServiceDTO();
		infoOptionDTO.setDtoVersion(com.n4systems.webservice.dto.InfoOptionServiceDTO.CURRENT_DTO_VERSION);
		infoOptionDTO.setId(infoOption.getUniqueID());
		infoOptionDTO.setName(infoOption.getName());
		infoOptionDTO.setStaticData(infoOption.isStaticData());
		infoOptionDTO.setWeight(infoOption.getWeight());
		infoOptionDTO.setInfoFieldId(infoFieldId);
		return infoOptionDTO;
	}

	public com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO convert(AutoAttributeCriteria criteria) {
		com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO serviceCriteria = new com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO();

		serviceCriteria.setId(criteria.getId());
		serviceCriteria.setProductTypeId(criteria.getProductType().getId());

		for (InfoFieldBean field : criteria.getInputs()) {
			serviceCriteria.getInputInfoFields().add(field.getUniqueID());
		}

		for (InfoFieldBean field : criteria.getOutputs()) {
			serviceCriteria.getOutputInfoFields().add(field.getUniqueID());
		}

		return serviceCriteria;
	}

	public com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO convert(AutoAttributeDefinition definition) {
		com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO serviceDefinition = new com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO();

		serviceDefinition.setId(definition.getId());
		serviceDefinition.setAutoAttributeCriteriaId(definition.getCriteria().getId());

		for (InfoOptionBean infoOption : definition.getInputs()) {
			serviceDefinition.getInputInfoOptions().add(infoOption.getUniqueID());
		}

		for (InfoOptionBean infoOption : definition.getOutputs()) {
			serviceDefinition.getOutputInfoOptions().add(convert(infoOption, infoOption.getInfoField().getUniqueID()));
		}

		return serviceDefinition;
	}

	public com.n4systems.webservice.dto.UserServiceDTO convert(UserBean user) {
		persistenceManager.reattach(user);

		com.n4systems.webservice.dto.UserServiceDTO userService = new com.n4systems.webservice.dto.UserServiceDTO();
		userService.setId(user.getId());
		userService.setUserId(user.getUserID().toLowerCase());
		userService.setHashPassword(user.getHashPassword());
		userService.setSecurityRfidNumber(user.getHashSecurityCardNumber());
		BitField permField = new BitField(user.getPermissions());
		userService.setAllowedToIdentify(permField.isSet(Permissions.Tag));
		userService.setAllowedToInspect(permField.isSet(Permissions.CreateInspection));

		populateOwners(user.getOwner(), userService);

		userService.setAttachedToPrimaryOrg(user.getOwner().getInternalOrg().isPrimary());

		return userService;
	}

	private void populateOwners(BaseOrg baseOrg, DTOHasOwners dto) {
		long customerId = NULL_ID;
		long orgId = NULL_ID;
		long divisionId = NULL_ID;

		if (baseOrg.isDivision()) {
			divisionId = baseOrg.getId();
			CustomerOrg customerOrg = baseOrg.getCustomerOrg();
			customerId = customerOrg.getId();
			orgId = retrieveOwnerId(customerOrg.getParent());
		} else if (baseOrg.isCustomer()) {
			customerId = baseOrg.getId();
			orgId = retrieveOwnerId(baseOrg.getParent());
		} else {
			orgId = retrieveOwnerId(baseOrg);
		}

		dto.setOwnerId(baseOrg.getId());
		dto.setOrgId(orgId);
		dto.setCustomerId(customerId);
		dto.setDivisionId(divisionId);
	}

	private long retrieveOwnerId(BaseOrg baseOrg) {
		return baseOrg.getId();
	}

	public UserBean convert(com.n4systems.webservice.dto.UserServiceDTO userDTO) {
		UserBean user = new UserBean();

		user.setUniqueID((userDTO.getId() == NULL_ID) ? null : userDTO.getId());
		user.setUserID(userDTO.getUserId());

		return user;
	}

	public ProductTypeGroupServiceDTO convert(ProductTypeGroup productTypeGroup) {

		ProductTypeGroupServiceDTO groupServiceDTO = new ProductTypeGroupServiceDTO();
		groupServiceDTO.setId(productTypeGroup.getId());
		groupServiceDTO.setName(productTypeGroup.getName());
		groupServiceDTO.setOrderIdx(productTypeGroup.getOrderIdx());

		return groupServiceDTO;
	}

	public TenantServiceDTO convert(PrimaryOrg tenant) {

		TenantServiceDTO tenantService = new TenantServiceDTO();
		tenantService.setId(tenant.getTenant().getId());
		tenantService.setName(tenant.getTenant().getName());
		tenantService.setDisplayName(tenant.getName());
		tenantService.setSerialNumberFormat(tenant.getSerialNumberFormat());
		tenantService.setUsingJobs(tenant.getExtendedFeatures().contains(ExtendedFeature.Projects));
		tenantService.setUsingSerialNumber(tenant.isUsingSerialNumber());
		tenantService.setUsingJobSites(tenant.getExtendedFeatures().contains(ExtendedFeature.JobSites));

		return tenantService;
	}

	public Date convertStringToDate(String stringDate) {
		if (stringDate == null || stringDate.length() == 0)
			return null;

		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
		Date dateConvert = null;
		try {
			dateConvert = df.parse(stringDate);
		} catch (ParseException e) {

			// try another way
			try {
				df = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
				dateConvert = df.parse(stringDate);
			} catch (ParseException ee) {
				// do nothing, return null
				logger.warn("failed to parse string date " + stringDate.toString(), ee);
			}
		}

		return dateConvert;
	}

	public Date convertNextDate(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO) {
		return convertStringToDate(inspectionServiceDTO.getNextDate());
	}

	private ProductStatusBean convertProductStatus(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO) {
		if (inspectionServiceDTO.getProductStatusId() == -1L) {
			return null;
		}

		return (ProductStatusBean) em.find(ProductStatusBean.class, inspectionServiceDTO.getProductStatusId());
	}

	public JobServiceDTO convert(Project job) {
		JobServiceDTO jobService = new JobServiceDTO();

		jobService.setId(job.getId());
		jobService.setName(job.getName());
		jobService.setProjectId(job.getProjectID());

		populateOwners(job.getOwner(), jobService);

		for (UserBean user : job.getResources()) {
			jobService.getResourceUserIds().add(user.getUniqueID());
		}

		return jobService;
	}

	private InspectionScheduleServiceDTO convert(InspectionSchedule inspectionSchedule) {
		InspectionScheduleServiceDTO scheduleService = new InspectionScheduleServiceDTO();

		scheduleService.setId(inspectionSchedule.getId());
		scheduleService.setNextDate(AbstractBaseServiceDTO.dateToString(inspectionSchedule.getNextDate()));
		scheduleService.setProductId(inspectionSchedule.getProduct().getId());
		scheduleService.setInspectionTypeId(inspectionSchedule.getInspectionType().getId());
		scheduleService.setJobId(inspectionSchedule.getProject() != null ? inspectionSchedule.getProject().getId() : NULL_ID);
		scheduleService.setCompleted(inspectionSchedule.getStatus() == InspectionSchedule.ScheduleStatus.COMPLETED);

		return scheduleService;
	}

	public InspectionSchedule convert(InspectionScheduleServiceDTO inspectionScheduleServiceDTO, long tenantId) {

		Tenant tenant = persistenceManager.find(Tenant.class, tenantId);

		InspectionSchedule inspectionSchedule = new InspectionSchedule();

		inspectionSchedule.setMobileGUID(inspectionScheduleServiceDTO.getMobileGuid());
		inspectionSchedule.setNextDate(AbstractBaseServiceDTO.stringToDate(inspectionScheduleServiceDTO.getNextDate()));
		inspectionSchedule.setTenant(tenant);

		return inspectionSchedule;

	}

	public SetupDataLastModDatesServiceDTO convert(SetupDataLastModDates setupModDates) {
		SetupDataLastModDatesServiceDTO dto = new SetupDataLastModDatesServiceDTO();

		dto.setAutoAttributes(setupModDates.getAutoAttributes());
		dto.setInspectionTypes(setupModDates.getInspectionTypes());
		dto.setOwners(setupModDates.getOwners());
		dto.setProductTypes(setupModDates.getProductTypes());
		dto.setJobs(setupModDates.getJobs());

		return dto;
	}

	public CustomerOrgServiceDTO convert(CustomerOrg customerOrg) {
		CustomerOrgServiceDTO dto = new CustomerOrgServiceDTO();
		populate(customerOrg, dto);
		return dto;
	}

	public DivisionOrgServiceDTO convert(DivisionOrg divisionOrg) {
		DivisionOrgServiceDTO dto = new DivisionOrgServiceDTO();
		populate(divisionOrg, dto);
		return dto;
	}

	public InternalOrgServiceDTO convert(InternalOrg internalOrg) {
		InternalOrgServiceDTO dto = new InternalOrgServiceDTO();
		populate(internalOrg, dto);

		dto.setPrimaryOrg(internalOrg.isPrimary());

		return dto;
	}

	private void populate(BaseOrg baseOrg, AbstractBaseOrgServiceDTO baseOrgDto) {
		baseOrgDto.setId(baseOrg.getId());
		baseOrgDto.setName(baseOrg.getName());
	}

	private void populate(BaseOrg baseOrg, AbstractExternalOrgServiceDTO baseOrgDto) {
		populate(baseOrg, (AbstractBaseOrgServiceDTO) baseOrgDto);
		baseOrgDto.setParentId(baseOrg.getParent().getId());
	}

	public VendorServiceDTO convert(OrgConnection orgConnection) {
		InternalOrg vendor = orgConnection.getVendor();

		VendorServiceDTO vendorDTO = new VendorServiceDTO();
		vendorDTO.setId(vendor.getId());
		vendorDTO.setName(vendor.getName());
		vendorDTO.setOwnerId(orgConnection.getCustomer().getId());

		return vendorDTO;
	}

	public BaseOrg convert(long ownerId, long tenantId) {
		BaseOrg owner = null;
		owner = persistenceManager.find(BaseOrg.class, ownerId, new TenantOnlySecurityFilter(tenantId));

		return owner;
	}

}
