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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;

import com.n4systems.ejb.EventScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.impl.EventScheduleManagerImpl;
import com.n4systems.ejb.impl.PersistenceManagerImpl;
import com.n4systems.ejb.legacy.SerialNumberCounter;
import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.model.AbstractEvent;
import com.n4systems.model.Asset;
import com.n4systems.model.AssetStatus;
import com.n4systems.model.AssetType;
import com.n4systems.model.AssetTypeGroup;
import com.n4systems.model.AssetTypeSchedule;
import com.n4systems.model.ComboBoxCriteriaResult;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Event;
import com.n4systems.model.EventBook;
import com.n4systems.model.EventForm;
import com.n4systems.model.EventGroup;
import com.n4systems.model.EventSchedule;
import com.n4systems.model.EventType;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Observation;
import com.n4systems.model.OneClickCriteriaResult;
import com.n4systems.model.Project;
import com.n4systems.model.Recommendation;
import com.n4systems.model.SelectCriteriaResult;
import com.n4systems.model.SignatureCriteriaResult;
import com.n4systems.model.State;
import com.n4systems.model.Status;
import com.n4systems.model.SubAsset;
import com.n4systems.model.SubEvent;
import com.n4systems.model.Tenant;
import com.n4systems.model.TextFieldCriteriaResult;
import com.n4systems.model.UnitOfMeasureCriteriaResult;
import com.n4systems.model.eventbook.EventBookByMobileIdLoader;
import com.n4systems.model.eventbook.EventBookFindOrCreateLoader;
import com.n4systems.model.location.Location;
import com.n4systems.model.location.LocationContainer;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.PrimaryOrg;
import com.n4systems.model.safetynetwork.OrgConnection;
import com.n4systems.model.security.OrgOnlySecurityFilter;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.user.User;
import com.n4systems.model.utils.FindSubAssets;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.servicedto.converts.PrimaryOrgToServiceDTOConverter;
import com.n4systems.servicedto.converts.util.DtoDateConverter;
import com.n4systems.services.TenantFinder;
import com.n4systems.services.signature.SignatureService;
import com.n4systems.util.BitField;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.webservice.dto.AbstractBaseOrgServiceDTO;
import com.n4systems.webservice.dto.AbstractExternalOrgServiceDTO;
import com.n4systems.webservice.dto.AbstractInspectionServiceDTO;
import com.n4systems.webservice.dto.CriteriaResultServiceDTO;
import com.n4systems.webservice.dto.CustomerOrgServiceDTO;
import com.n4systems.webservice.dto.DTOHasOwners;
import com.n4systems.webservice.dto.DivisionOrgServiceDTO;
import com.n4systems.webservice.dto.ImageServiceDTO;
import com.n4systems.webservice.dto.InfoFieldServiceDTO;
import com.n4systems.webservice.dto.InspectionBookServiceDTO;
import com.n4systems.webservice.dto.InspectionInfoOptionServiceDTO;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;
import com.n4systems.webservice.dto.InspectionServiceDTO;
import com.n4systems.webservice.dto.InternalOrgServiceDTO;
import com.n4systems.webservice.dto.JobServiceDTO;
import com.n4systems.webservice.dto.LocationServiceDTO;
import com.n4systems.webservice.dto.ObservationResultServiceDTO;
import com.n4systems.webservice.dto.ObservationResultServiceDTO.ObservationState;
import com.n4systems.webservice.dto.ObservationResultServiceDTO.ObservationType;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.ProductTypeGroupServiceDTO;
import com.n4systems.webservice.dto.ProductTypeScheduleServiceDTO;
import com.n4systems.webservice.dto.ProductTypeServiceDTO;
import com.n4systems.webservice.dto.SetupDataLastModDatesServiceDTO;
import com.n4systems.webservice.dto.SubInspectionServiceDTO;
import com.n4systems.webservice.dto.SubProductMapServiceDTO;
import com.n4systems.webservice.dto.TenantServiceDTO;
import com.n4systems.webservice.dto.VendorServiceDTO;

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
	
	private EventScheduleManager eventScheduleManager;
	
	private SerialNumberCounter serialNumberCounter;

	
	
	
	public ServiceDTOBeanConverterImpl(){}
	
	public ServiceDTOBeanConverterImpl(EntityManager em) {
		this.em = em;
		this.persistenceManager = new PersistenceManagerImpl(em);
		this.eventScheduleManager = new EventScheduleManagerImpl(em);
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

	public InspectionBookServiceDTO convert(EventBook eventBook) {

		InspectionBookServiceDTO bookDTO = new InspectionBookServiceDTO();
		bookDTO.setName(eventBook.getName());
		bookDTO.setBookOpen(eventBook.isOpen());
		bookDTO.setId(eventBook.getId());

		populateOwners(eventBook.getOwner(), bookDTO);

		return bookDTO;
	}

	private void populateAbstractInspectionInfo(AbstractInspectionServiceDTO inspectionDTO, AbstractEvent event) {
		inspectionDTO.setComments(event.getComments());
		inspectionDTO.setId(event.getId());
		inspectionDTO.setInspectionMobileGUID(event.getMobileGUID());
		
		Map<String, String> infoOptionMap = event.getInfoOptionMap();
		Set<String> infoOptionKeys = event.getInfoOptionMap().keySet();
		Iterator<String> It = infoOptionKeys.iterator();
		while (It.hasNext()) {
			String infoFieldName = (String) (It.next());
			inspectionDTO.getInfoOptions().add(convert(event.getId(), infoFieldName, infoOptionMap.get(infoFieldName)));
		}

		if (event.getEventForm() != null)
			inspectionDTO.setFormId(event.getEventForm().getId());
		
		inspectionDTO.setEditable(event.isEditable());
		inspectionDTO.setInspectionTypeId(event.getType().getId());
		inspectionDTO.setProductId(event.getAsset().getId());
		inspectionDTO.setProductMobileGuid(event.getAsset().getMobileGUID());
		for (CriteriaResult criteriaResult : event.getResults()) {
			inspectionDTO.getResults().add(convert(criteriaResult));
		}

	}

	public com.n4systems.webservice.dto.InspectionServiceDTO convert(Event event) {
		InspectionServiceDTO inspectionDTO = new InspectionServiceDTO();
		persistenceManager.reattach(event, false);

		populateAbstractInspectionInfo(inspectionDTO, event);
		inspectionDTO.setOwnerId(retrieveOwnerId(event.getOwner()));
		inspectionDTO.setPerformedById( event.getPerformedBy().getId() );
		inspectionDTO.setStatus( event.getStatus().name() );
		inspectionDTO.setEventBookId((event.getBook() != null) ? event.getBook().getMobileId() : null);
		inspectionDTO.setUtcDate(event.getDate());
		inspectionDTO.setEventGroupId(event.getGroup().getMobileGuid());
		inspectionDTO.setPrintable(event.isPrintable());
		
		if (event.getAssetStatus() != null) {
			inspectionDTO.setProductStatusId(event.getAssetStatus().getId());
		}
		
		convertLocationToDTO(inspectionDTO, event);
		for (SubEvent subEvent : event.getSubEvents()) {
			inspectionDTO.getSubInspections().add(convert(subEvent));
		}
		return inspectionDTO;

	}

	public SubInspectionServiceDTO convert(SubEvent subEvent) {
		SubInspectionServiceDTO subInspectionDTO = new SubInspectionServiceDTO();

		populateAbstractInspectionInfo(subInspectionDTO, subEvent);

		subInspectionDTO.setName(subEvent.getName());

		return subInspectionDTO;
	}

	public List<com.n4systems.webservice.dto.InspectionServiceDTO> convert(EventGroup eventGroup) {
		persistenceManager.reattach(eventGroup, false);

		List<com.n4systems.webservice.dto.InspectionServiceDTO> inspectionDTOs = new ArrayList<com.n4systems.webservice.dto.InspectionServiceDTO>();
		for (Event event : eventGroup.getAvailableEvents()) {
			inspectionDTOs.add(convert(event));
		}

		return inspectionDTOs;
	}

	public ProductServiceDTO convert(Asset asset) {

		ProductServiceDTO productDTO = new ProductServiceDTO();

		persistenceManager.reattach(asset, false);

		populateOwners(asset.getOwner(), productDTO);

		productDTO.setId(asset.getId());
		productDTO.setCustomerRefNumber(asset.getCustomerRefNumber());
		productDTO.setIdentified(dateToString(asset.getIdentified()));
		productDTO.setLastInspectionDate(dateToString(asset.getLastEventDate()));
		productDTO.setMobileGuid(asset.getMobileGUID());
		productDTO.setProductStatusId(asset.getAssetStatus() != null ? asset.getAssetStatus().getId() : 0);
		productDTO.setProductTypeId(asset.getType().getId());
		productDTO.setPurchaseOrder(asset.getPurchaseOrder());
		productDTO.setRfidNumber(asset.getRfidNumber() == null ? null : asset.getRfidNumber().toUpperCase());
		productDTO.setSerialNumber(asset.getSerialNumber());
		productDTO.setComments(asset.getComments());
		productDTO.setIdentifiedById(asset.getIdentifiedBy() != null ? asset.getIdentifiedBy().getId() : 0);
		productDTO.setModifiedById(asset.getModifiedBy() != null ? asset.getModifiedBy().getId() : 0);
		productDTO.setOrderNumber(asset.getShopOrder() != null ? asset.getShopOrder().getOrder().getOrderNumber() : "");
		productDTO.setModified(asset.getModified());
		productDTO.setAssignedUserId(asset.getAssignedUser() != null ? asset.getAssignedUser().getId() : 0);

		if (asset.getDescription() != null && asset.getDescription().length() >= 255) {
			productDTO.setDescription(asset.getDescription().substring(0, 255));
		} else {
			productDTO.setDescription(asset.getDescription());
		}

		for (InfoOptionBean infoOption : asset.getInfoOptions()) {
			productDTO.getInfoOptions().add(convert(infoOption, infoOption.getInfoField().getUniqueID()));
		}

		new FindSubAssets(persistenceManager, asset).fillInSubAssets();
		if (asset.getSubAssets() != null) {
			SubProductMapServiceDTO subProductMap = null;
			for (SubAsset subAsset : asset.getSubAssets()) {
				subProductMap = new SubProductMapServiceDTO();
				subProductMap.setName(subAsset.getLabel());
				subProductMap.setSubProductId(subAsset.getAsset().getId());
				subProductMap.setSubAssetGuid(subAsset.getAsset().getMobileGUID());
				subProductMap.setProductId(asset.getId());
				subProductMap.setMasterAssetGuid(subAsset.getMasterAsset().getMobileGUID());
				productDTO.getSubProducts().add(subProductMap);
			}
		}

		List<EventSchedule> schedules = eventScheduleManager.getAvailableSchedulesFor(asset);
		for (EventSchedule schedule : schedules) {
			productDTO.getSchedules().add(convert(schedule));
		}

		convertLocationToDTO(productDTO, asset);

		return productDTO;
	}
	
	private void convertLocationToDTO(LocationServiceDTO locationDTO, LocationContainer locationContainer) {
		Location location = locationContainer.getAdvancedLocation();
		
		if (location == null) {
			return;
		}
		
		locationDTO.setLocation(location.getFreeformLocation());
		
		Long predefinedLocationId = (location.getPredefinedLocation() != null) ? location.getPredefinedLocation().getId() : null;
		locationDTO.setPredefinedLocationId(predefinedLocationId);
	}

	/**
	 * WARNING: this will NOT convert assignedUser or AdvancedLocation fields as those have been moved to the ProductServiceDTOConverter
	 * @deprecated Use the ProductServiceDTOConverter
	 */
	@Deprecated
	public Asset convert(ProductServiceDTO productServiceDTO, Asset targetAsset, long tenantId) {

		Tenant tenantOrganization = TenantFinder.getInstance().findTenant(tenantId);
		PrimaryOrg primaryOrg = TenantFinder.getInstance().findPrimaryOrg(tenantOrganization.getId());

		targetAsset.setComments(productServiceDTO.getComments());
		targetAsset.setCustomerRefNumber(productServiceDTO.getCustomerRefNumber());
		targetAsset.setType(em.find(AssetType.class, productServiceDTO.getProductTypeId()));
		targetAsset.setPurchaseOrder(productServiceDTO.getPurchaseOrder());
		targetAsset.setRfidNumber(productServiceDTO.getRfidNumber());
		targetAsset.setTenant(tenantOrganization);

		if (productServiceDTO.getSerialNumber().equals(GENERATE_SERIAL_NUMBER)) {
			targetAsset.setSerialNumber(serialNumberCounter.generateSerialNumber(primaryOrg));
		} else {
			targetAsset.setSerialNumber(productServiceDTO.getSerialNumber());
		}
		
		targetAsset.setOwner(em.find(BaseOrg.class, productServiceDTO.getOwnerId()));

		targetAsset.setAssetStatus(convertField(AssetStatus.class, productServiceDTO.getProductStatusId(), targetAsset.getAssetStatus()));

		if (productServiceDTO.identifiedByExists()) {
			User user = em.find(User.class, productServiceDTO.getIdentifiedById());
			targetAsset.setIdentifiedBy(user);
		}

		if (productServiceDTO.modifiedByIdExists()) {
			User modifiedBy = em.find(User.class, productServiceDTO.getModifiedById());
			targetAsset.setModifiedBy(modifiedBy);
		}

		if (productServiceDTO.getInfoOptions() != null) {
			targetAsset.setInfoOptions(convertInfoOptions(productServiceDTO));
		}

		if (DtoDateConverter.convertStringToDate(productServiceDTO.getIdentified()) != null) {
			targetAsset.setIdentified(DtoDateConverter.convertStringToDate(productServiceDTO.getIdentified()));
		}

		if (targetAsset.isNew()) {
			targetAsset.setMobileGUID(productServiceDTO.getMobileGuid());
		}
		
		return targetAsset;
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
	 * @param event
	 * @param inspectionDTO
	 */
	private void populate(AbstractEvent event, AbstractInspectionServiceDTO inspectionServiceDTO, Tenant tenant) {
		event.setComments(inspectionServiceDTO.getComments());

		// Required object lookups
		event.setTenant(tenant);
		event.setType(persistenceManager.find(EventType.class, inspectionServiceDTO.getInspectionTypeId(), new TenantOnlySecurityFilter(tenant.getId())));
		event.setAsset((Asset) em.find(Asset.class, inspectionServiceDTO.getProductId()));

		// Mobile prior to 1.26 will send formVersion.
		if (inspectionServiceDTO.getFormId() != null) {
			event.setEventForm(persistenceManager.find(EventForm.class, inspectionServiceDTO.getFormId()));
			event.setEditable(true);
		} else {
			// When event forms were split out from event types in 1.26, the forms were assigned the same id's as their event types.
			event.setEventForm(persistenceManager.find(EventForm.class, event.getType().getId(), new TenantOnlySecurityFilter(tenant.getId())));
			
			boolean eventEditable = inspectionServiceDTO.getFormVersion() == event.getType().getFormVersion();
			event.setEditable(eventEditable);
		}
		
		// Optional object lookups
		if (inspectionServiceDTO.getResults() != null) {
			event.setResults(convert(inspectionServiceDTO.getResults(), tenant, event));
		}

		if (inspectionServiceDTO.getInfoOptions() != null) {
			for (InspectionInfoOptionServiceDTO infoOption : inspectionServiceDTO.getInfoOptions()) {
				event.getInfoOptionMap().put(infoOption.getInfoFieldName(), infoOption.getInfoOptionValue());
			}
		}

		event.setMobileGUID(inspectionServiceDTO.getInspectionMobileGUID());
	}

	/**
	 * WARNING: this will NOT convert assignedUser or AdvancedLocation fields as those have been moved to the InspectionServiceDTOConverter
	 * @deprecated Use the InspectionServiceDTOConverter
	 */
	@Deprecated
	public Event convert(InspectionServiceDTO inspectionServiceDTO, Long tenantId) throws IOException {

		Tenant tenant = persistenceManager.find(Tenant.class, tenantId);

		Event event = new Event();

		populate(event, inspectionServiceDTO, tenant);
		
		event.setPrintable(inspectionServiceDTO.isPrintable());
		event.setDate(inspectionServiceDTO.getUtcDate());
		
		// Required object lookups		
		User performedBy = (User)em.find(User.class, inspectionServiceDTO.getPerformedById());
		event.setModifiedBy( performedBy );
		event.setPerformedBy( performedBy );

		BaseOrg owner = em.find(BaseOrg.class, inspectionServiceDTO.getOwnerId());
		event.setOwner(owner);

		findOrCreateEventBook(inspectionServiceDTO, event);

		if (inspectionServiceDTO.getEventGroupId() != null) {
			event.setGroup(findOrCreateEventGroup(inspectionServiceDTO.getEventGroupId(), performedBy));
		} else if (inspectionServiceDTO.inspectionGroupExists()) {
			event.setGroup(persistenceManager.find(EventGroup.class, inspectionServiceDTO.getInspectionGroupId()));
		}

		Status status = null;
		if (StringUtils.isNotBlank(inspectionServiceDTO.getStatus())) {
			try {
				status = Status.valueOf(inspectionServiceDTO.getStatus());
			} catch (Exception e) {
				logger.error("Unable to convert Event Status value of [" + inspectionServiceDTO.getStatus() + "] defaulting to N/A", e);
				status = Status.NA;
			}
		}
		event.setStatus(status);
		
		if (inspectionServiceDTO.getSubInspections() != null) {
			for (SubInspectionServiceDTO subInspection : inspectionServiceDTO.getSubInspections()) {
				event.getSubEvents().add(convert(subInspection, tenant, performedBy));
			}
		}

		event.setAssetStatus(convertProductStatus(inspectionServiceDTO));
		event.getAttachments().addAll(convertToFileAttachmentsAndWriteToTemp(inspectionServiceDTO.getImages(), tenant, performedBy));

		return event;
	}

	private void findOrCreateEventBook(InspectionServiceDTO eventDto, Event event) {
		SecurityFilter filter = new OrgOnlySecurityFilter(event.getPerformedBy().getOwner());
		
		EventBook book = null;
		if (eventDto.getEventBookId() != null){
			EventBookByMobileIdLoader loader = new EventBookByMobileIdLoader(null);
			book = loader.setMobileId(eventDto.getEventBookId()).load(em, filter);
			
			if (book == null && eventDto.getInspectionBookTitle() != null) {
				book = new EventBook();
				book.setMobileId(eventDto.getEventBookId());
				book.setName(eventDto.getInspectionBookTitle());
				book.setTenant(event.getOwner().getTenant());
				book.setOwner(event.getOwner());
				persistenceManager.save(book);
			}
		} else if (eventDto.inspectionBookExists()) { // Legacy
			book = persistenceManager.find(EventBook.class, eventDto.getInspectionBookId()); 
		} else if (eventDto.getInspectionBookTitle() != null) { // Legacy
			EventBookFindOrCreateLoader loader = new EventBookFindOrCreateLoader(null);
			loader.setName(eventDto.getInspectionBookTitle()).setOwner(event.getOwner());
			book = loader.load(em, filter);
		}
		
		event.setBook(book);
	}
	
	private EventGroup findOrCreateEventGroup(String mobileGuid, User createdBy) {
		QueryBuilder<EventGroup> builder = new QueryBuilder<EventGroup>(EventGroup.class, new UserSecurityFilter(createdBy));
		builder.addWhere(WhereClauseFactory.create("mobileGuid", mobileGuid));
		
		EventGroup group = persistenceManager.find(builder);
		
		if (group == null) {
			group = new EventGroup();
			group.setTenant(createdBy.getTenant());
			group.setMobileGuid(mobileGuid);
			persistenceManager.save(group, createdBy);
		}
		
		return group;
	}

	public FileAttachment convert(AbstractEvent event, com.n4systems.webservice.dto.InspectionImageServiceDTO inspectionImageServiceDTO, User performedBy) throws IOException {
		return convertToFileAttachment(inspectionImageServiceDTO.getImage(), event.getTenant(), performedBy);

	}

	private FileAttachment convertToFileAttachment(ImageServiceDTO imageServiceDTO, Tenant tenant, User modifiedBy) throws IOException {
		// some files come prepended with a '\'. We should remove these here.
		String fileName = imageServiceDTO.getFileName().replace("\\", "");

		FileAttachment fileAttachment = new FileAttachment(tenant, modifiedBy, fileName);
		fileAttachment.setComments(imageServiceDTO.getComments());

		return fileAttachment;
	}

	private FileAttachment convertFileAttachmentAndWriteToTemp(ImageServiceDTO imageServiceDTO, Tenant tenant, User modifiedBy) throws IOException {
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

	private List<FileAttachment> convertToFileAttachmentsAndWriteToTemp(List<ImageServiceDTO> images, Tenant tenant, User modifiedBy) throws IOException {
		List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>();

		if (images != null) {
			for (ImageServiceDTO imageServiceDTO : images) {
				fileAttachments.add(convertFileAttachmentAndWriteToTemp(imageServiceDTO, tenant, modifiedBy));
			}
		}

		return fileAttachments;
	}

	private SubEvent convert(SubInspectionServiceDTO subInspectionServiceDTO, Tenant tenant, User performedBy) throws IOException {
		SubEvent subEvent = new SubEvent();
		populate(subEvent, subInspectionServiceDTO, tenant);
		subEvent.setName(subInspectionServiceDTO.getName());
		subEvent.getAttachments().addAll(convertToFileAttachmentsAndWriteToTemp(subInspectionServiceDTO.getImages(), tenant, performedBy));

		return subEvent;
	}

	private Set<CriteriaResult> convert(List<CriteriaResultServiceDTO> resultDTOs, Tenant tenant, AbstractEvent event) {

		Set<CriteriaResult> results = new HashSet<CriteriaResult>();

		for (CriteriaResultServiceDTO resultDTO : resultDTOs) {
			CriteriaResult result;
			
			if (resultDTO.getType() == null || resultDTO.getType().equals(CriteriaResultServiceDTO.TYPE_ONE_CLICK)) {
				result = new OneClickCriteriaResult();
				((OneClickCriteriaResult)result).setState(persistenceManager.find(State.class, resultDTO.getStateId()));
			} else if (resultDTO.getType().equals(CriteriaResultServiceDTO.TYPE_TEXT_FIELD)) {
				result = new TextFieldCriteriaResult();
				((TextFieldCriteriaResult)result).setValue(resultDTO.getTextFieldValue());
			} else if (resultDTO.getType().equals(CriteriaResultServiceDTO.TYPE_SELECT_FIELD)) {
				result = new SelectCriteriaResult();
				((SelectCriteriaResult)result).setValue(resultDTO.getSelectFieldValue());
			} else if (resultDTO.getType().equals(CriteriaResultServiceDTO.TYPE_COMBO_BOX)) {
				result = new ComboBoxCriteriaResult();
				((ComboBoxCriteriaResult)result).setValue(resultDTO.getComboBoxFieldValue());
			} else if (resultDTO.getType().equals(CriteriaResultServiceDTO.TYPE_UNIT_OF_MEASURE)) {
				result = new UnitOfMeasureCriteriaResult();
				((UnitOfMeasureCriteriaResult)result).setPrimaryValue(resultDTO.getUnitOfMeasurePrimaryFieldValue());
				((UnitOfMeasureCriteriaResult)result).setSecondaryValue(resultDTO.getUnitOfMeasureSecondaryFieldValue());
			} else if (resultDTO.getType().equals(CriteriaResultServiceDTO.SIGNATURE)) {
				result = new SignatureCriteriaResult();
				((SignatureCriteriaResult)result).setSigned(resultDTO.getSignatureImage() != null);
				((SignatureCriteriaResult)result).setImage(resultDTO.getSignatureImage());
			} else {
				throw new NotImplementedException("Conversion of CriteriaResultServiceDTO [" + resultDTO.getType() + "] not implemented");
			}
			
			result.setCriteria(persistenceManager.find(Criteria.class, resultDTO.getCriteriaId()));
			result.setTenant(tenant);
			result.setEvent(event);

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

	private ProductTypeScheduleServiceDTO convert(AssetTypeSchedule assetTypeSchedule) {
		ProductTypeScheduleServiceDTO productTypeScheduleServiceDTO = new ProductTypeScheduleServiceDTO();
		productTypeScheduleServiceDTO.setDtoVersion(ProductTypeScheduleServiceDTO.CURRENT_DTO_VERSION);
		productTypeScheduleServiceDTO.setInspectionTypeId(assetTypeSchedule.getEventType().getId());
		productTypeScheduleServiceDTO.setFrequency(assetTypeSchedule.getFrequency());
		productTypeScheduleServiceDTO.setId(assetTypeSchedule.getId());
		productTypeScheduleServiceDTO.setProductTypeId(assetTypeSchedule.getAssetType().getId());
		populateOwners(assetTypeSchedule.getOwner(), productTypeScheduleServiceDTO);
		return productTypeScheduleServiceDTO;
	}

	@SuppressWarnings("deprecation")
	public ProductTypeServiceDTO convert_new(AssetType assetType) {

		ProductTypeServiceDTO productTypeDTO = new ProductTypeServiceDTO();
		productTypeDTO.setDtoVersion(ProductTypeServiceDTO.CURRENT_DTO_VERSION);
		productTypeDTO.setId(assetType.getId());
		productTypeDTO.setName(assetType.getName());

		for (InfoFieldBean infoField : assetType.getInfoFields()) {
			if (!infoField.isRetired()) {
				productTypeDTO.getInfoFields().add(convert_new(infoField, assetType.getId()));
			}
		}

		for (EventType eventType : assetType.getEventTypes()) {
			productTypeDTO.getInspectionTypeIds().add(eventType.getId());
		}

		for (AssetTypeSchedule schedule : assetType.getSchedules()) {
			productTypeDTO.getSchedules().add(convert(schedule));
		}

		for (AssetType subType : assetType.getSubTypes()) {
			productTypeDTO.getSubTypes().add(subType.getId());
		}

		productTypeDTO.setGroupId(assetType.getGroup() != null ? assetType.getGroup().getId() : NULL_ID);
		productTypeDTO.setMaster(assetType.isMaster());

		return productTypeDTO;
	}

	private CriteriaResultServiceDTO convert(CriteriaResult criteriaResult) {
		CriteriaResultServiceDTO criteriaResultServiceDTO = new CriteriaResultServiceDTO();
		
		if (criteriaResult instanceof OneClickCriteriaResult) {
			criteriaResultServiceDTO.setType(CriteriaResultServiceDTO.TYPE_ONE_CLICK);
			criteriaResultServiceDTO.setStateId(((OneClickCriteriaResult)criteriaResult).getState().getId());
		} else if (criteriaResult instanceof TextFieldCriteriaResult) {
			criteriaResultServiceDTO.setType(CriteriaResultServiceDTO.TYPE_TEXT_FIELD);
			criteriaResultServiceDTO.setTextFieldValue(((TextFieldCriteriaResult)criteriaResult).getValue());
		} else if (criteriaResult instanceof SelectCriteriaResult) {
			criteriaResultServiceDTO.setType(CriteriaResultServiceDTO.TYPE_SELECT_FIELD);
			criteriaResultServiceDTO.setSelectFieldValue(((SelectCriteriaResult)criteriaResult).getValue());
		} else if (criteriaResult instanceof ComboBoxCriteriaResult) {
			criteriaResultServiceDTO.setType(CriteriaResultServiceDTO.TYPE_COMBO_BOX);
			criteriaResultServiceDTO.setComboBoxFieldValue(((ComboBoxCriteriaResult)criteriaResult).getValue());
		} else if (criteriaResult instanceof UnitOfMeasureCriteriaResult) {
			criteriaResultServiceDTO.setType(CriteriaResultServiceDTO.TYPE_UNIT_OF_MEASURE);
			criteriaResultServiceDTO.setUnitOfMeasurePrimaryFieldValue(((UnitOfMeasureCriteriaResult)criteriaResult).getPrimaryValue());
			criteriaResultServiceDTO.setUnitOfMeasureSecondaryFieldValue(((UnitOfMeasureCriteriaResult)criteriaResult).getSecondaryValue());
		} else if (criteriaResult instanceof SignatureCriteriaResult) {
			criteriaResultServiceDTO.setType(CriteriaResultServiceDTO.SIGNATURE);
			
			SignatureCriteriaResult sigCriteria = (SignatureCriteriaResult) criteriaResult;
			if (sigCriteria.isSigned()) {
				byte[] signatureImage;
				try {
					signatureImage = new SignatureService().loadSignatureImage(criteriaResult.getTenant(), criteriaResult.getEvent().getId(), criteriaResult.getCriteria().getId());
					criteriaResultServiceDTO.setSignatureImage(signatureImage);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		} else {
			throw new NotImplementedException("Conversion of CriteriaResult [" + criteriaResult.getClass() + "] not implemented");
		}

		criteriaResultServiceDTO.setId(criteriaResult.getId());
		criteriaResultServiceDTO.setCriteriaId(criteriaResult.getCriteria().getId());
		criteriaResultServiceDTO.setInspectionId(criteriaResult.getEvent().getId());
		
		int i = 0;
		for (Recommendation recommendation : criteriaResult.getRecommendations()) {
			criteriaResultServiceDTO.getRecommendations().add(convert(recommendation, i, criteriaResult.getId()));
			i++;
		}

		i = 0;
		for (Deficiency deficiency : criteriaResult.getDeficiencies()) {
			criteriaResultServiceDTO.getDeficiencies().add(convert(deficiency, i, criteriaResult.getId()));
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

	private ObservationResultServiceDTO convert(Observation observation, int orderIndex, long criteriaResultId) {

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

	private InfoFieldServiceDTO convert_new(InfoFieldBean infoField, Long assetTypeId) {
		InfoFieldServiceDTO infoFieldDTO = new InfoFieldServiceDTO();
		infoFieldDTO.setDtoVersion(InfoFieldServiceDTO.CURRENT_DTO_VERSION);
		infoFieldDTO.setId(infoField.getUniqueID());
		infoFieldDTO.setFieldType(infoField.getFieldType());
		infoFieldDTO.setName(infoField.getName());
		infoFieldDTO.setRequired(infoField.isRequired());
		infoFieldDTO.setUsingUnitOfMeasure(infoField.isUsingUnitOfMeasure());
		infoFieldDTO.setWeight(infoField.getWeight());
		infoFieldDTO.setProductTypeId(assetTypeId);
		if (infoField.getUnitOfMeasure() != null) {
			infoFieldDTO.setDefaultUnitOfMeasureId(infoField.getUnitOfMeasure().getId());
		}

		// Put together the list of only static info options; dynamic ones are
		// sent with their asset
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

	public com.n4systems.webservice.dto.UserServiceDTO convert(User user) {
		persistenceManager.reattach(user);

		com.n4systems.webservice.dto.UserServiceDTO userService = new com.n4systems.webservice.dto.UserServiceDTO();
		userService.setId(user.getId());
		userService.setUserId(user.getUserID().toLowerCase());
		userService.setHashPassword(user.getHashPassword());
		userService.setSecurityRfidNumber(user.getHashSecurityCardNumber());
		BitField permField = new BitField(user.getPermissions());
		userService.setAllowedToIdentify(permField.isSet(Permissions.Tag));
		userService.setAllowedToInspect(permField.isSet(Permissions.CreateEvent));

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

	public User convert(com.n4systems.webservice.dto.UserServiceDTO userDTO) {
		User user = new User();

		user.setId(((userDTO.getId() == NULL_ID) ? null : userDTO.getId()));
		user.setUserID(userDTO.getUserId());

		return user;
	}

	public ProductTypeGroupServiceDTO convert(AssetTypeGroup assetTypeGroup) {

		ProductTypeGroupServiceDTO groupServiceDTO = new ProductTypeGroupServiceDTO();
		groupServiceDTO.setId(assetTypeGroup.getId());
		groupServiceDTO.setName(assetTypeGroup.getName());
		groupServiceDTO.setOrderIdx(assetTypeGroup.getOrderIdx());

		return groupServiceDTO;
	}

	public TenantServiceDTO convert(PrimaryOrg primaryOrg) {
		return new PrimaryOrgToServiceDTOConverter().convert(primaryOrg);
	}

	private AssetStatus convertProductStatus(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO) {
		if (inspectionServiceDTO.getProductStatusId() == -1L) {
			return null;
		}

		return (AssetStatus) em.find(AssetStatus.class, inspectionServiceDTO.getProductStatusId());
	}

	public JobServiceDTO convert(Project job) {
		JobServiceDTO jobService = new JobServiceDTO();

		jobService.setId(job.getId());
		jobService.setName(job.getName());
		jobService.setProjectId(job.getProjectID());

		populateOwners(job.getOwner(), jobService);

		for (User user : job.getResources()) {
			jobService.getResourceUserIds().add(user.getId());
		}

		return jobService;
	}

	private InspectionScheduleServiceDTO convert(EventSchedule eventSchedule) {
		InspectionScheduleServiceDTO scheduleService = new InspectionScheduleServiceDTO();

		scheduleService.setId(eventSchedule.getId());
		scheduleService.setNextDate(dateToString(eventSchedule.getNextDate()));
		scheduleService.setProductId(eventSchedule.getAsset().getId());
		scheduleService.setInspectionTypeId(eventSchedule.getEventType().getId());
		scheduleService.setJobId(eventSchedule.getProject() != null ? eventSchedule.getProject().getId() : NULL_ID);
		scheduleService.setCompleted(eventSchedule.getStatus() == EventSchedule.ScheduleStatus.COMPLETED);

		return scheduleService;
	}

	public EventSchedule convert(InspectionScheduleServiceDTO inspectionScheduleServiceDTO, long tenantId) {

		Tenant tenant = persistenceManager.find(Tenant.class, tenantId);

		EventSchedule eventSchedule = new EventSchedule();

		eventSchedule.setMobileGUID(inspectionScheduleServiceDTO.getMobileGuid());
		eventSchedule.setNextDate(stringToDate(inspectionScheduleServiceDTO.getNextDate()));
		eventSchedule.setTenant(tenant);

		return eventSchedule;

	}

	public SetupDataLastModDatesServiceDTO convert(SetupDataLastModDates setupModDates) {
		SetupDataLastModDatesServiceDTO dto = new SetupDataLastModDatesServiceDTO();

		dto.setAutoAttributes(setupModDates.getAutoAttributes());
		dto.setInspectionTypes(setupModDates.getEventTypes());
		dto.setOwners(setupModDates.getOwners());
		dto.setProductTypes(setupModDates.getAssetTypes());
		dto.setJobs(setupModDates.getJobs());
		dto.setEmployees(setupModDates.getEmployees());
		dto.setLocations(setupModDates.getLocations());
		
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
		baseOrgDto.setDeleted(!baseOrg.isActive());
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
	
	public static Date stringToDate(String originalDate) {
		SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
		Date dateConvert = null;		
		try {
			dateConvert = DF.parse(originalDate);
		} catch (ParseException e) {
			// do nothing, return null
		} catch (NullPointerException e) {
			// do nothing, return null
		}
		return dateConvert;
	}		
	
	public static String dateToString(Date originalDate){
		SimpleDateFormat DF = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
		String dateConvert = null;		
		try {
			dateConvert = DF.format( originalDate );
		} catch (NullPointerException e) {
			// do nothing, return null
		}	 
		return dateConvert;
	}

}
