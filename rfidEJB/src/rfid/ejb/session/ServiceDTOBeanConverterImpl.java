package rfid.ejb.session;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rfid.ejb.entity.InfoFieldBean;
import rfid.ejb.entity.InfoOptionBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.InspectionManager;
import com.n4systems.ejb.InspectionScheduleManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.model.AbstractInspection;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.AutoAttributeDefinition;
import com.n4systems.model.Criteria;
import com.n4systems.model.CriteriaResult;
import com.n4systems.model.CriteriaSection;
import com.n4systems.model.Customer;
import com.n4systems.model.Deficiency;
import com.n4systems.model.Division;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.FileAttachment;
import com.n4systems.model.Inspection;
import com.n4systems.model.InspectionBook;
import com.n4systems.model.InspectionGroup;
import com.n4systems.model.InspectionSchedule;
import com.n4systems.model.InspectionType;
import com.n4systems.model.JobSite;
import com.n4systems.model.Observation;
import com.n4systems.model.Organization;
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
import com.n4systems.model.TenantOrganization;
import com.n4systems.model.tenant.SetupDataLastModDates;
import com.n4systems.model.utils.FindSubProducts;
import com.n4systems.reporting.PathHandler;
import com.n4systems.security.Permissions;
import com.n4systems.util.BitField;
import com.n4systems.util.SecurityFilter;
import com.n4systems.webservice.dto.AbstractInspectionServiceDTO;
import com.n4systems.webservice.dto.CriteriaResultServiceDTO;
import com.n4systems.webservice.dto.CriteriaSectionServiceDTO;
import com.n4systems.webservice.dto.CriteriaServiceDTO;
import com.n4systems.webservice.dto.CustomerServiceDTO;
import com.n4systems.webservice.dto.DivisionServiceDTO;
import com.n4systems.webservice.dto.ImageServiceDTO;
import com.n4systems.webservice.dto.InfoFieldNameServiceDTO;
import com.n4systems.webservice.dto.InfoFieldServiceDTO;
import com.n4systems.webservice.dto.InspectionBookServiceDTO;
import com.n4systems.webservice.dto.InspectionInfoOptionServiceDTO;
import com.n4systems.webservice.dto.InspectionScheduleServiceDTO;
import com.n4systems.webservice.dto.InspectionTypeServiceDTO;
import com.n4systems.webservice.dto.JobServiceDTO;
import com.n4systems.webservice.dto.JobSiteServiceDTO;
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

import fieldid.web.services.dto.AbstractBaseServiceDTO;
import fieldid.web.services.dto.AutoAttributeCriteriaServiceDTO;
import fieldid.web.services.dto.AutoAttributeDefinitionServiceDTO;
import fieldid.web.services.dto.EndUserServiceDTO;
import fieldid.web.services.dto.InfoOptionServiceDTO;
import fieldid.web.services.dto.ProductSerialServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;
import fieldid.web.services.dto.UserServiceDTO;


/**
 * This class converts from ServiceDTOs to beans and from beans back to serviceDTOs
 * 
 * @author Jesse Miller
 *
 */
@Interceptors({TimingInterceptor.class})
@Stateless
public class ServiceDTOBeanConverterImpl implements ServiceDTOBeanConverter {
	private static final Logger logger = Logger.getLogger(ServiceDTOBeanConverter.class);
	public static final long NULL_ID = -1024L;
	public static final String GENERATE_SERIAL_NUMBER = "[[GENERATE]]";
	
	@PersistenceContext (unitName="rfidEM")
	protected EntityManager em;	

	@EJB private PersistenceManager persistenceManager;	
	@EJB private InspectionManager inspectionManager;
	@EJB private CustomerManager customerManager;
	@EJB private InspectionScheduleManager inspectionScheduleManager;
	@EJB private SerialNumberCounter serialNumberCounter;
	
	/**
	 * Given a non-null, non-zero id, looks up an entity of type clazz from the database.  If id is equal to {@link #NULL_ID} 
	 * returns null.  Returns currentValue otherwise.  Used to implement logic of null means 'do not change'
	 * and {@link #NULL_ID} means set null.
	 * @param clazz			Class of the field
	 * @param id			Long id from the webservice
	 * @param currentValue	Current value of the field
	 * @return				The current value, loaded value, or null
	 */
	private <T> T convertField(Class<T> clazz, Long id, T currentValue) {
		T decoded = currentValue;
		
		if (id != null && id > 0) {
			decoded = em.find(clazz, id);
		} else if(id == NULL_ID) {
			decoded = null;
		}
		
		return decoded;
	}
	
	
	public ProductServiceDTO convert(ProductSerialServiceDTO productSerialServiceDTO) {
		if (productSerialServiceDTO == null) 
			return null;
		
		Long customerId = convertStringToLong(productSerialServiceDTO.getR_endUser());
		Long divisionId = convertStringToLong(productSerialServiceDTO.getR_Division());
		Long userId = convertStringToLong(productSerialServiceDTO.getR_User());
		Long productStatusId = convertStringToLong(productSerialServiceDTO.getR_productStatus());
		
		ProductServiceDTO product = new ProductServiceDTO();
		product.setId(convertStringToLong(productSerialServiceDTO.getUniqueID()));
		product.setComments(productSerialServiceDTO.getComment());
		product.setCustomerId( customerId != null ? customerId : 0L );
		product.setCustomerRefNumber( productSerialServiceDTO.getEndUserReferenceNumber() );
		product.setDivisionId( divisionId != null ? divisionId : 0L );
		product.setIdentified( productSerialServiceDTO.getDateCreated() );
		product.setIdentifiedById( userId != null ? userId : 0L );
		product.setInfoOptions( convertOldInfoOptionsToNew(productSerialServiceDTO.getInfoOptions()) );
		product.setJobSiteId( productSerialServiceDTO.getJobSiteId() );
		product.setLocation( productSerialServiceDTO.getLocation() );
		product.setMobileGuid( productSerialServiceDTO.getMobileGUID() );
		product.setProductStatusId( productStatusId != null ? productStatusId : 0L );
		product.setProductTypeId( convertStringToLong(productSerialServiceDTO.getR_productInfo()) );
		product.setRfidNumber( productSerialServiceDTO.getRfidNumber() );
		product.setSerialNumber( productSerialServiceDTO.getSerialNumber() );
		
		return product;
	}
	
	@SuppressWarnings("deprecation")
	private List<com.n4systems.webservice.dto.InfoOptionServiceDTO> convertOldInfoOptionsToNew(Collection<InfoOptionServiceDTO> oldInfoOptions) {
		List<com.n4systems.webservice.dto.InfoOptionServiceDTO> infoOptions = new ArrayList<com.n4systems.webservice.dto.InfoOptionServiceDTO>();
		
		com.n4systems.webservice.dto.InfoOptionServiceDTO infoOption = null;
		for (InfoOptionServiceDTO oldInfoOption : oldInfoOptions) {
			infoOption = new com.n4systems.webservice.dto.InfoOptionServiceDTO();
			infoOption.setId( convertStringToLong(oldInfoOption.getUniqueId()) );
			infoOption.setInfoFieldId( convertStringToLong(oldInfoOption.getR_infoField()) );
			infoOption.setName(oldInfoOption.getName());
			if (infoOption.getId() > 0) {
				infoOption.setStaticData(true);
			} else {
				infoOption.setStaticData(false);
			}
			infoOption.setWeight( convertStringToLong(oldInfoOption.getWeight()) );
			infoOptions.add(infoOption);
		}
		
		return infoOptions;
	}
	
	public JobSiteServiceDTO convert(JobSite jobSite) {
		JobSiteServiceDTO jobSiteDTO = new JobSiteServiceDTO();
		jobSiteDTO.setId(jobSite.getId());
		jobSiteDTO.setName(jobSite.getName());
		if (jobSite.getCustomer() != null) {
			jobSiteDTO.setCustomerId(jobSite.getCustomer().getId());
		}
		if (jobSite.getDivision() != null) {	
			jobSiteDTO.setDivisionId(jobSite.getDivision().getId());
		}
		return jobSiteDTO;
	}
	
	public InspectionBookServiceDTO convert(InspectionBook inspectionBook) {
		
		InspectionBookServiceDTO bookDTO = new InspectionBookServiceDTO();
		if (inspectionBook.getCustomer() != null) {
			bookDTO.setCustomerId(inspectionBook.getCustomer().getId());
		}
		bookDTO.setName(inspectionBook.getName());
		bookDTO.setBookOpen(inspectionBook.isOpen());
		bookDTO.setId(inspectionBook.getId());
		
		return bookDTO;		
	}
	
	public com.n4systems.webservice.dto.InspectionServiceDTO convert(Inspection inspection) {
		
		com.n4systems.webservice.dto.InspectionServiceDTO inspectionDTO = new com.n4systems.webservice.dto.InspectionServiceDTO();
		persistenceManager.reattach( inspection, false );
		inspectionDTO.setId( inspection.getId() );
		inspectionDTO.setComments( inspection.getComments() );
		inspectionDTO.setProductId( inspection.getProduct().getId() );
		inspectionDTO.setCustomerId( ( inspection.getCustomer() != null ) ? inspection.getCustomer().getId() : 0L );
		inspectionDTO.setDivisionId( ( inspection.getDivision() != null ) ? inspection.getDivision().getId() : 0L );
		inspectionDTO.setLocation( inspection.getLocation() );
		inspectionDTO.setInspectorId( inspection.getInspector().getUniqueID() );
		inspectionDTO.setInspectionTypeId( inspection.getType().getId() );
		inspectionDTO.setStatus( inspection.getStatus().name() );
		inspectionDTO.setInspectionBookId( ( inspection.getBook() != null ) ? inspection.getBook().getId() : 0L );
		inspectionDTO.setUtcDate(inspection.getDate());
		
		// TODO convert date to their time zone
		inspectionDTO.setDate( AbstractBaseServiceDTO.dateToString( inspection.getDate() ) );
		
		inspectionDTO.setJobSiteId( ( inspection.getJobSite() != null ) ? inspection.getJobSite().getId() : 0L );
		inspectionDTO.setFormVersion(inspection.getFormVersion());
		return inspectionDTO;
				
	}
	
	public List<com.n4systems.webservice.dto.InspectionServiceDTO> convert(InspectionGroup inspectionGroup) {
		persistenceManager.reattach( inspectionGroup, false );
		
		List<com.n4systems.webservice.dto.InspectionServiceDTO> inspectionDTOs = new ArrayList<com.n4systems.webservice.dto.InspectionServiceDTO>();
		for( Inspection inspection : inspectionGroup.getAvailableInspections() ) {
			inspectionDTOs.add( convert(inspection) );
		}
		
		return inspectionDTOs;
	}
	
	public ProductServiceDTO convert(Product product) {
		
		ProductServiceDTO productDTO = new ProductServiceDTO();
		
		persistenceManager.reattach( product, false );
		
		productDTO.setCustomerId(product.getOwner() != null ? product.getOwner().getId() : 0);
		productDTO.setCustomerRefNumber(product.getCustomerRefNumber());
		productDTO.setDivisionId(product.getDivision() != null ? product.getDivision().getId() : 0);
		productDTO.setId(product.getId());
		productDTO.setIdentified( AbstractBaseServiceDTO.dateToString(product.getIdentified()) );
		productDTO.setJobSiteId(product.getJobSite() != null ? product.getJobSite().getId() : 0);
		productDTO.setLastInspectionDate( AbstractBaseServiceDTO.dateToString(product.getLastInspectionDate()) );
		productDTO.setLocation(product.getLocation());
		productDTO.setMobileGuid(product.getMobileGUID());
		productDTO.setOrganizationId(product.getOrganization() != null ? product.getOrganization().getId() : 0);
		productDTO.setProductStatusId(product.getProductStatus() != null ? product.getProductStatus().getUniqueID() : 0);
		productDTO.setProductTypeId(product.getType().getId());
		productDTO.setPurchaseOrder(product.getPurchaseOrder());
		productDTO.setRfidNumber(product.getRfidNumber() == null ? null : product.getRfidNumber().toUpperCase());
		productDTO.setSerialNumber(product.getSerialNumber());
		productDTO.setComments(product.getComments());
		productDTO.setIdentifiedById(product.getIdentifiedBy() != null ? product.getIdentifiedBy().getUniqueID() : 0);
		productDTO.setOrderNumber(product.getShopOrder() != null ? product.getShopOrder().getOrder().getOrderNumber() : "");
		
		for (InfoOptionBean infoOption : product.getInfoOptions()) {
			productDTO.getInfoOptions().add( convert(infoOption, infoOption.getInfoField().getUniqueID()) );
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
	
	
	
	public Product convert( ProductServiceDTO productServiceDTO, Product targetProduct, long tenantId ) {
		
		TenantOrganization tenantOrganization = em.find(TenantOrganization.class, tenantId); 
		
		targetProduct.setComments(productServiceDTO.getComments());
		targetProduct.setCustomerRefNumber(productServiceDTO.getCustomerRefNumber());		
		targetProduct.setLocation(productServiceDTO.getLocation());
		targetProduct.setType( em.find(ProductType.class, productServiceDTO.getProductTypeId()) );
		targetProduct.setPurchaseOrder(productServiceDTO.getPurchaseOrder());
		targetProduct.setRfidNumber(productServiceDTO.getRfidNumber());
		targetProduct.setTenant( tenantOrganization );
		
		if (productServiceDTO.getSerialNumber().equals(GENERATE_SERIAL_NUMBER)) {
			targetProduct.setSerialNumber(serialNumberCounter.generateSerialNumber(tenantId));
		} else {
			targetProduct.setSerialNumber(productServiceDTO.getSerialNumber());
		}
		
		if( productServiceDTO.divisionExists() ) {
			targetProduct.setDivision( em.find(Division.class, productServiceDTO.getDivisionId()) );
		} else {
			targetProduct.setDivision( null );
		}
				
		if( productServiceDTO.customerExists() ) {
			targetProduct.setOwner( em.find(Customer.class, productServiceDTO.getCustomerId()) );
		} else {
			targetProduct.setOwner( null );
		}
		
		if( productServiceDTO.jobSiteExists() ) {
			targetProduct.setJobSite( em.find(JobSite.class, productServiceDTO.getJobSiteId()) );
		} else {
			targetProduct.setJobSite( null );
		}

		targetProduct.setProductStatus(convertField(ProductStatusBean.class, productServiceDTO.getProductStatusId(), targetProduct.getProductStatus()));
		
		if( productServiceDTO.identifiedByExists() ) {
			UserBean user = em.find(UserBean.class, productServiceDTO.getIdentifiedById());			
			targetProduct.setIdentifiedBy( user );			
			targetProduct.setOrganization(user.getOrganization());
		} else {
			targetProduct.setOrganization( tenantOrganization );
		}
	
		if ( productServiceDTO.getInfoOptions() != null ) {			
			Set<InfoOptionBean> infoOptions = new TreeSet<InfoOptionBean>();
			for ( com.n4systems.webservice.dto.InfoOptionServiceDTO infoOptionServiceDTO : productServiceDTO.getInfoOptions() ) {
				infoOptions.add( convert( infoOptionServiceDTO) );
			}
			
			targetProduct.setInfoOptions(infoOptions);
		}
		
		if (targetProduct.isNew()) {
		
			if (convertStringToDate(productServiceDTO.getIdentified()) != null ) {
				targetProduct.setIdentified( convertStringToDate(productServiceDTO.getIdentified()) );
			}
			
			targetProduct.setMobileGUID(productServiceDTO.getMobileGuid());
		}
		
		return targetProduct;		
	}
	

	
	public InfoOptionBean convert( InfoOptionServiceDTO infoOptionServiceDTO ) {
		
		InfoOptionBean infoOption = new InfoOptionBean();
				
		infoOption.setUniqueID( convertStringToLong( infoOptionServiceDTO.getUniqueID() ) );
		infoOption.setInfoField( findInfoField( infoOptionServiceDTO.getR_infoField() ) );
		infoOption.setName( infoOptionServiceDTO.getName() );
		infoOption.setStaticData( infoOptionServiceDTO.getStaticData() );
		infoOption.setWeight( convertStringToLong( infoOptionServiceDTO.getWeight() ) );
		
		return infoOption;		
	}
	
	public InfoOptionBean convert( com.n4systems.webservice.dto.InfoOptionServiceDTO infoOptionServiceDTO ) {
		InfoOptionBean infoOption = null;
		
		if( !infoOptionServiceDTO.isCreatedOnMobile() ) {
			infoOption = em.find( InfoOptionBean.class, infoOptionServiceDTO.getId() );
		}
		
		
		if( infoOption == null ) {
			infoOption = new InfoOptionBean();
			infoOption.setInfoField( em.find(InfoFieldBean.class, infoOptionServiceDTO.getInfoFieldId()) );
			infoOption.setName( infoOptionServiceDTO.getName() );
		}
		
		return infoOption;
	}
	
	@Deprecated
	public InfoOptionServiceDTO convert_OLD( InfoOptionBean infoOption ) {
		
		InfoOptionServiceDTO infoOptionServiceDTO = new InfoOptionServiceDTO();
				
		infoOptionServiceDTO.setUniqueId( infoOption.getUniqueID().toString() );
		infoOptionServiceDTO.setR_infoField( infoOption.getInfoField().getUniqueID().toString() );
		infoOptionServiceDTO.setName( infoOption.getName() );
		infoOptionServiceDTO.setStaticData( infoOption.isStaticData() );
		infoOptionServiceDTO.setWeight( infoOption.getWeight().toString() );
		
		return infoOptionServiceDTO;		
	}
	
	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}	

	/**
	 * Populates an abstract inspection with the fields from an abstract inspection service dto
	 * @param inspection
	 * @param inspectionDTO
	 */
	private void populate( AbstractInspection inspection, AbstractInspectionServiceDTO inspectionServiceDTO, TenantOrganization tenant ) {

		inspection.setComments( inspectionServiceDTO.getComments() );

		// Required object lookups
		inspection.setTenant( tenant );
		inspection.setType( persistenceManager.find(InspectionType.class, inspectionServiceDTO.getInspectionTypeId()) );
		inspection.setProduct( (Product)em.find(Product.class, inspectionServiceDTO.getProductId()) );
		
		// Optional object lookups
		if (inspectionServiceDTO.getResults() != null) {
			inspection.setResults( convert(inspectionServiceDTO.getResults(), tenant, inspection) ); 
		}
		
		if (inspectionServiceDTO.getInfoOptions() != null) {
			for (InspectionInfoOptionServiceDTO infoOption : inspectionServiceDTO.getInfoOptions()) {
				inspection.getInfoOptionMap().put(infoOption.getInfoFieldName(), infoOption.getInfoOptionValue());
			}
		}
		inspection.setFormVersion(inspectionServiceDTO.getFormVersion());
		
	}
	
	public Inspection convert( com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO, Long tenantId ) throws IOException {
		
		TenantOrganization tenant = persistenceManager.find(TenantOrganization.class, tenantId);

		Inspection inspection = new Inspection();
		
		populate(inspection, inspectionServiceDTO, tenant);
		
		inspection.setLocation( inspectionServiceDTO.getLocation() );
		inspection.setPrintable( inspectionServiceDTO.isPrintable() );
		
		// Check if utcDate is set, if not, dealing with a PRE 1.11 version: use date
		if (inspectionServiceDTO.getUtcDate() != null) {
			inspection.setDate(inspectionServiceDTO.getUtcDate());
		} else {
			// TODO convert from their set timezone
			inspection.setDate( convertStringToDate(inspectionServiceDTO.getDate()) );
		}		
		
		// Required object lookups		
		UserBean inspector = (UserBean)em.find(UserBean.class, inspectionServiceDTO.getInspectorId());
		inspection.setModifiedBy( inspector );		
		inspection.setInspector( inspector );
		inspection.setOrganization( persistenceManager.find(Organization.class, inspectionServiceDTO.getOrganizationId()) );

		// Optional object lookups
		
		// Customer is optional in case they are using job sites
		if (inspectionServiceDTO.customerExists() ) {
			inspection.setCustomer( (Customer)em.find(Customer.class, inspectionServiceDTO.getCustomerId()) );
		}
		
		if (inspectionServiceDTO.divisionExists()) {
			inspection.setDivision( (Division)em.find(Division.class, inspectionServiceDTO.getDivisionId()) );
		}
		
		if (inspectionServiceDTO.jobSiteExists()) {
			inspection.setJobSite( (JobSite)em.find(JobSite.class, inspectionServiceDTO.getJobSiteId()));
		}
		
		if ( inspectionServiceDTO.inspectionBookExists() ) {
			inspection.setBook( persistenceManager.find(InspectionBook.class, inspectionServiceDTO.getInspectionBookId()) );			
		} else if( inspectionServiceDTO.getInspectionBookTitle() != null ) {
			InspectionBook inspectionBook = inspectionManager.findInspectionBook( inspectionServiceDTO.getInspectionBookTitle(), new SecurityFilter(tenantId, inspectionServiceDTO.getCustomerId() ) );
			
			if( inspectionBook == null ) {
				inspectionBook = new InspectionBook();
				inspectionBook.setName(inspectionServiceDTO.getInspectionBookTitle());				
				inspectionBook.setTenant(tenant);
				
				// If we are using customer, use the inspection customer for this inspection book
				// otherwise, use the jobsite's customer for the inspection book
				if (inspection.getCustomer() != null) {				
					inspectionBook.setCustomer( inspection.getCustomer() );
				} else if (inspection.getJobSite() != null) {
					inspectionBook.setCustomer( inspection.getJobSite().getCustomer() );
				}
				
				persistenceManager.save(inspectionBook);
			}
			
			inspection.setBook( inspectionBook );
		}
		
		
		if (inspectionServiceDTO.inspectionGroupExists()) {
			inspection.setGroup( persistenceManager.find(InspectionGroup.class, inspectionServiceDTO.getInspectionGroupId()) ); 
		} 
		
		if (inspectionServiceDTO.getStatus() != null) {		
			inspection.setStatus( Status.valueOf(inspectionServiceDTO.getStatus()) );
		}
		
		if (inspectionServiceDTO.getSubInspections() != null) {
			for (SubInspectionServiceDTO subInspection : inspectionServiceDTO.getSubInspections()) {
				inspection.getSubInspections().add( convert(subInspection,tenant, inspector) );
			}			
		}
		
		inspection.getAttachments().addAll( convertToFileAttachments(inspectionServiceDTO.getImages(), tenant, inspector) );
		
		return inspection;
	}
	
	private List<FileAttachment> convertToFileAttachments(List<ImageServiceDTO> images, TenantOrganization tenant, UserBean modifiedBy) throws IOException {

		List<FileAttachment> fileAttachments = new ArrayList<FileAttachment>(); 
		
		if (images != null) {
			for (ImageServiceDTO imageServiceDTO : images) {
				try {
					File tempImageFile = PathHandler.getTempFile(imageServiceDTO.getFileName());
					tempImageFile.getParentFile().mkdirs();
					
					FileOutputStream fileOut = new FileOutputStream(tempImageFile);
					fileOut.write(imageServiceDTO.getImage());
					
					// Must get the full path name and then remove the temporary root path to conform to how the processor accepts it
					String fileName = tempImageFile.getPath();
					fileName = fileName.substring(fileName.indexOf(PathHandler.getTempRoot().getPath()) +  PathHandler.getTempRoot().getPath().length());
					
					FileAttachment fileAttachment = new FileAttachment(tenant, modifiedBy, fileName);
					fileAttachment.setComments(imageServiceDTO.getComments());
					
					fileAttachments.add(fileAttachment);
					
				} catch (IOException e) {
					logger.error("Problem saving images from mobile", e);	
					throw e;
				}
			}
		}
		
		return fileAttachments;
	}
	
	private SubInspection convert(SubInspectionServiceDTO subInspectionServiceDTO, TenantOrganization tenant, UserBean inspector) throws IOException {		
		SubInspection subInspection = new SubInspection();
		populate(subInspection, subInspectionServiceDTO, tenant);
		subInspection.setName(subInspectionServiceDTO.getName());
		subInspection.getAttachments().addAll( convertToFileAttachments(subInspectionServiceDTO.getImages(), tenant, inspector) );
		
		return subInspection;
	}
	
	private Set<CriteriaResult> convert(List<CriteriaResultServiceDTO> resultDTOs, TenantOrganization tenant, AbstractInspection inspection) {
		
		Set<CriteriaResult> results = new HashSet<CriteriaResult>();
		
		for (CriteriaResultServiceDTO resultDTO : resultDTOs) {
			CriteriaResult result = new CriteriaResult();
			result.setState( persistenceManager.find(State.class, resultDTO.getStateId()) );
			result.setCriteria( persistenceManager.find(Criteria.class, resultDTO.getCriteriaId()) );
			result.setTenant(tenant);
			result.setInspection(inspection);
			
			if (resultDTO.getRecommendations() != null) {
				for (ObservationResultServiceDTO recommendationDTO : resultDTO.getRecommendations()) {
					result.getRecommendations().add( convertRecommendation(recommendationDTO, tenant) );
				}
			}
			
			if (resultDTO.getDeficiencies() != null) {
				for (ObservationResultServiceDTO deficiencyDTO : resultDTO.getDeficiencies()) {
					result.getDeficiencies().add( convertDeficiency(deficiencyDTO, tenant) );
				}
			}
			
			results.add(result);
		}
		
		return results;
	}
	
	private Recommendation convertRecommendation(ObservationResultServiceDTO recommendationDTO, TenantOrganization tenant) {
		Recommendation recommendation = new Recommendation();
		recommendation.setText(recommendationDTO.getText());
		recommendation.setTenant(tenant);
		recommendation.setState(convert(recommendationDTO.getState()));
		return recommendation;
	}
	
	private Deficiency convertDeficiency(ObservationResultServiceDTO deficiencyDTO, TenantOrganization tenant) {
		Deficiency deficiency = new Deficiency();
		deficiency.setTenant(tenant);
		deficiency.setText(deficiencyDTO.getText());
		deficiency.setState(convert(deficiencyDTO.getState()));
		return deficiency;
	}
	
	private Observation.State convert(ObservationResultServiceDTO.ObservationState state) {
		switch (state) {
			case COMMENT : return Observation.State.COMMENT;
			case OUTSTANDING : return Observation.State.OUTSTANDING;
			case REPAIRED : return Observation.State.REPAIRED;
			case REPAIREDONSITE : return Observation.State.REPAIREDONSITE;
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
	
	public ProductSerialServiceDTO convert( Product productSerial, boolean moreData ) {
		ProductSerialServiceDTO productSerialDTO = new ProductSerialServiceDTO();
		
		productSerialDTO.setDtoVersion( ProductSerialServiceDTO.CURRENT_DTO_VERSION.toString() );
		
		productSerialDTO.setUniqueID(productSerial.getId().toString());

		
		productSerialDTO.setTenantId( productSerial.getTenant().getId().toString() );
		productSerialDTO.setRfidNumber(productSerial.getRfidNumber());
		productSerialDTO.setReelId(productSerial.getSerialNumber());
		productSerialDTO.setSerialNumber( productSerial.getSerialNumber());
		productSerialDTO.setR_ordermaster(productSerial.getShopOrder() != null ? productSerial.getShopOrder().getId().toString() : "-1");
		productSerialDTO.setDateModified(productSerial.getModified().toString());
		
		productSerialDTO.setR_endUser(productSerial.getOwner() != null ? productSerial.getOwner().getId().toString() : "-1");
		productSerialDTO.setR_Division(productSerial.getDivision() != null ? productSerial.getDivision().getId().toString() : "-1");		
		productSerialDTO.setR_productInfo(productSerial.getType() != null ? productSerial.getType().getId().toString() : "-1");
		
		productSerialDTO.setMobileGUID(productSerial.getMobileGUID());
		productSerialDTO.setEndUserReferenceNumber(productSerial.getCustomerRefNumber());
		productSerialDTO.setComment(productSerial.getComments());
		productSerialDTO.setLocation( productSerial.getLocation() );
		productSerialDTO.setR_productStatus(productSerial.getProductStatus() != null ? productSerial.getProductStatus().getUniqueID().toString() : "-1" );
		productSerialDTO.setJobSiteId( (productSerial.getJobSite() != null) ? productSerial.getJobSite().getId() : 0L );
		
		productSerialDTO.setMoreData(moreData);
		
		for (InfoOptionBean infoOptionBean: productSerial.getInfoOptions()) {
			if (!infoOptionBean.getInfoField().isRetired()) {
				productSerialDTO.getInfoOptions().add(convert_OLD(infoOptionBean));
			}
		}
				
		return productSerialDTO;		
	}
	
	
	public ProductStatusServiceDTO convert( ProductStatusBean productStatus ) {
		
		ProductStatusServiceDTO productStatusServiceDTO = new ProductStatusServiceDTO();
		productStatusServiceDTO.setId( productStatus.getUniqueID() );
		productStatusServiceDTO.setName( productStatus.getName() );
		productStatusServiceDTO.setTenantId( productStatus.getTenant().getId() );
		productStatusServiceDTO.setCreated( productStatus.getDateCreated().toString() );
		productStatusServiceDTO.setModified( productStatus.getDateModified().toString() );
		productStatusServiceDTO.setModifiedBy( productStatus.getModifiedBy() );
		
		return productStatusServiceDTO;
	}
		
	

	private ProductTypeScheduleServiceDTO convert( ProductTypeSchedule productTypeSchedule ) {
		ProductTypeScheduleServiceDTO productTypeScheduleServiceDTO = new ProductTypeScheduleServiceDTO();
		productTypeScheduleServiceDTO.setDtoVersion(ProductTypeScheduleServiceDTO.CURRENT_DTO_VERSION);
		if (productTypeSchedule.getCustomer() != null) {
			productTypeScheduleServiceDTO.setCustomerId(productTypeSchedule.getCustomer().getId());
		}
		productTypeScheduleServiceDTO.setInspectionTypeId(productTypeSchedule.getInspectionType().getId());
		productTypeScheduleServiceDTO.setFrequency(productTypeSchedule.getFrequency());
		productTypeScheduleServiceDTO.setId(productTypeSchedule.getId());
		productTypeScheduleServiceDTO.setProductTypeId(productTypeSchedule.getProductType().getId());
		return productTypeScheduleServiceDTO;
	}
	
	
	public ProductTypeServiceDTO convert_new( ProductType productType ) {
		
		ProductTypeServiceDTO productTypeDTO = new ProductTypeServiceDTO();
		productTypeDTO.setDtoVersion( ProductTypeServiceDTO.CURRENT_DTO_VERSION );
		productTypeDTO.setId( productType.getId() );
		productTypeDTO.setName( productType.getName() );
		
		for (InfoFieldBean infoField : productType.getInfoFields()) {
			if( !infoField.isRetired() ) {
				productTypeDTO.getInfoFields().add( convert_new(infoField, productType.getId()) );
			}
		}
		
		for (InspectionType inspectionType : productType.getInspectionTypes()) {
			productTypeDTO.getInspectionTypeIds().add( inspectionType.getId() );
		}
		
		for (ProductTypeSchedule schedule : productType.getSchedules()) {
			productTypeDTO.getSchedules().add( convert(schedule) );
		}
		
		for (ProductType subType : productType.getSubTypes()) {
			productTypeDTO.getSubTypes().add(subType.getId());
		}
		
		productTypeDTO.setGroupId(productType.getGroup() != null ? productType.getGroup().getId() : NULL_ID);
		
		return productTypeDTO;
	}
	
	public InspectionTypeServiceDTO convert( InspectionType inspectionType ) {
		
		InspectionTypeServiceDTO inspectionTypeService = new InspectionTypeServiceDTO();
		inspectionTypeService.setDescription( inspectionType.getDescription() );
		inspectionTypeService.setId( inspectionType.getId() );
		inspectionTypeService.setName( inspectionType.getName() );
		inspectionTypeService.setPrintable( inspectionType.isPrintable() );
		inspectionTypeService.setMaster( inspectionType.isMaster() );
		inspectionTypeService.setGroupId( inspectionType.getGroup().getId() );
		inspectionTypeService.setFormVersion(inspectionType.getFormVersion());
		
		for ( CriteriaSection section : inspectionType.getSections() ) {
			if( ! section.isRetired() ) {
				inspectionTypeService.getSections().add( convert(section, inspectionType.getId()) );
			}
		}
		
		// Info Field Names ; sent with an order index
		int i=0;
		for ( String infoFieldName : inspectionType.getInfoFieldNames() ) {
			InfoFieldNameServiceDTO infoField = new InfoFieldNameServiceDTO();
			infoField.setName(infoFieldName);
			infoField.setOrderIndex(i);			
			inspectionTypeService.getInfoFieldNames().add( infoField );
			i++;
		}
		
		return inspectionTypeService;		
	}
	
	private CriteriaSectionServiceDTO convert( CriteriaSection section, Long inspectionTypeId ) {
		
		CriteriaSectionServiceDTO sectionServiceDTO = new CriteriaSectionServiceDTO();
		sectionServiceDTO.setId( section.getId() );
		sectionServiceDTO.setTitle( section.getTitle() );
		sectionServiceDTO.setInspectionTypeId( inspectionTypeId );
		
		for ( Criteria criteria : section.getCriteria() ) {
			if( !criteria.isRetired() ) {
				sectionServiceDTO.getCriteria().add( convert(criteria, section.getId()) );
			}
		}
		
		return sectionServiceDTO;		
	}
	
	private CriteriaServiceDTO convert( Criteria criteria, Long criteriaSectionId ) {
		
		CriteriaServiceDTO criteriaServiceDTO = new CriteriaServiceDTO();
		criteriaServiceDTO.setId( criteria.getId() );
		criteriaServiceDTO.setDisplayText( criteria.getDisplayText() );
		criteriaServiceDTO.setPrincipal( criteria.isPrincipal() );
		criteriaServiceDTO.setCriteriaSectionId( criteriaSectionId );
		criteriaServiceDTO.setStateSetId( criteria.getStates().getId() );
		
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
	
	private ObservationServiceDTO convert( String observationText, String typeDiscriminator, int orderIndex, long criteriaId ) {
		ObservationServiceDTO observation = new ObservationServiceDTO();
		observation.setObservationText(observationText);
		observation.setTypeDiscriminator(typeDiscriminator);
		observation.setOrderIndex(orderIndex);
		observation.setCriteriaId(criteriaId);
		
		return observation;
	}
	
	public StateSetServiceDTO convert( StateSet stateSet )	{
		StateSetServiceDTO stateSetServiceDTO = new StateSetServiceDTO();
		stateSetServiceDTO.setId( stateSet.getId() );
		stateSetServiceDTO.setName( stateSet.getName() );
		
		for ( State state : stateSet.getStates() ) {
			if( !state.isRetired() ) {
				stateSetServiceDTO.getStates().add( convert(state, stateSet.getId()) );
			}
		}
		
		return stateSetServiceDTO;
	}
	
	private StateServiceDTO convert( State state, Long stateSetId ) {
		
		StateServiceDTO stateServiceDTO = new StateServiceDTO();
		stateServiceDTO.setId( state.getId() );
		stateServiceDTO.setButtonName( state.getButtonName() );
		stateServiceDTO.setDisplayText( state.getDisplayText() );
		stateServiceDTO.setStatus( state.getStatus().name() );
		stateServiceDTO.setStateSetId(stateSetId);
		
		return stateServiceDTO;
	}
	
	@SuppressWarnings("unused")
	private fieldid.web.services.dto.InfoFieldServiceDTO convert( InfoFieldBean infoField ) {
		fieldid.web.services.dto.InfoFieldServiceDTO infoFieldDTO = new fieldid.web.services.dto.InfoFieldServiceDTO();
		infoFieldDTO.setDtoVersion( String.valueOf(InfoFieldServiceDTO.CURRENT_DTO_VERSION) );
		infoFieldDTO.setTenantId( infoField.getProductInfo().getTenant().getId().toString() );
		infoFieldDTO.setUniqueID(infoField.getUniqueID().toString());
		infoFieldDTO.setName(infoField.getName());
		infoFieldDTO.setR_productInfo(infoField.getProductInfo().getId());
		infoFieldDTO.setRequired(infoField.isRequired());
		infoFieldDTO.setRetired(infoField.isRetired());
		infoFieldDTO.setUsedInTemplate(false);
		infoFieldDTO.setUsingUnitOfMeasure(infoField.isUsingUnitOfMeasure());
		infoFieldDTO.setR_unitOfMeasure(infoField.getUnitOfMeasure() != null ? infoField.getUnitOfMeasure().getId() : null);
		infoFieldDTO.setFieldType(infoField.getFieldType() != null ? infoField.getFieldType() : "");
		infoFieldDTO.setValueType( null );
		
		//fill the info options
		for(InfoOptionBean infoOption: infoField.getInfoOptions()){
			infoFieldDTO.getInfoOptions().add( convert_OLD(infoOption) );
		}
		
		return infoFieldDTO;		
	}
	
	private InfoFieldServiceDTO convert_new( InfoFieldBean infoField, Long productTypeId ) {		
		InfoFieldServiceDTO infoFieldDTO = new InfoFieldServiceDTO();
		infoFieldDTO.setDtoVersion( InfoFieldServiceDTO.CURRENT_DTO_VERSION );
		infoFieldDTO.setId( infoField.getUniqueID() );
		infoFieldDTO.setFieldType( infoField.getFieldType() );
		infoFieldDTO.setName( infoField.getName() );
		infoFieldDTO.setRequired( infoField.isRequired() );
		infoFieldDTO.setUsingUnitOfMeasure( infoField.isUsingUnitOfMeasure() );
		infoFieldDTO.setWeight( infoField.getWeight() );
		infoFieldDTO.setProductTypeId( productTypeId );
		if (infoField.getUnitOfMeasure() != null) {
			infoFieldDTO.setDefaultUnitOfMeasureId( infoField.getUnitOfMeasure().getId() );
		}
		
		// Put together the list of only static info options; dynamic ones are sent with their product
		List<com.n4systems.webservice.dto.InfoOptionServiceDTO> infoOptions = new ArrayList<com.n4systems.webservice.dto.InfoOptionServiceDTO>();
		for (InfoOptionBean infoOption : infoField.getInfoOptions() ) {
			infoOptions.add( convert(infoOption, infoField.getUniqueID()) );
		}
		infoFieldDTO.setInfoOptions(infoOptions);
		
		return infoFieldDTO;		
	}
	
	private com.n4systems.webservice.dto.InfoOptionServiceDTO convert( InfoOptionBean infoOption, Long infoFieldId ) {
		com.n4systems.webservice.dto.InfoOptionServiceDTO infoOptionDTO = new com.n4systems.webservice.dto.InfoOptionServiceDTO();
		infoOptionDTO.setDtoVersion( InfoOptionServiceDTO.CURRENT_DTO_VERSION );
		infoOptionDTO.setId( infoOption.getUniqueID() );
		infoOptionDTO.setName( infoOption.getName() );
		infoOptionDTO.setStaticData( infoOption.isStaticData() );
		infoOptionDTO.setWeight( infoOption.getWeight() );
		infoOptionDTO.setInfoFieldId( infoFieldId );
		return infoOptionDTO;
	}
	
	public com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO convert( AutoAttributeCriteria criteria ) {
		com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO serviceCriteria = new com.n4systems.webservice.dto.AutoAttributeCriteriaServiceDTO();
		
		serviceCriteria.setId( criteria.getId() );
		serviceCriteria.setProductTypeId( criteria.getProductType().getId() );
		
		for( InfoFieldBean field : criteria.getInputs() ) {
			serviceCriteria.getInputInfoFields().add(field.getUniqueID());
		}
		
		for ( InfoFieldBean field : criteria.getOutputs() ) {
			serviceCriteria.getOutputInfoFields().add(field.getUniqueID());
		}
		
		return serviceCriteria;
	}
	
	public com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO convert( AutoAttributeDefinition definition ) {
		com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO serviceDefinition = new com.n4systems.webservice.dto.AutoAttributeDefinitionServiceDTO();
		
		serviceDefinition.setId( definition.getId() );
		serviceDefinition.setAutoAttributeCriteriaId( definition.getCriteria().getId() );
		
		for (InfoOptionBean infoOption : definition.getInputs()) {
			serviceDefinition.getInputInfoOptions().add(infoOption.getUniqueID());
		}
		
		for (InfoOptionBean infoOption : definition.getOutputs()) {
			serviceDefinition.getOutputInfoOptions().add(convert(infoOption, infoOption.getInfoField().getUniqueID()));
		}
		
		return serviceDefinition;
	}
	
	public AutoAttributeCriteriaServiceDTO convert_old( AutoAttributeCriteria criteriaIn ) {
		AutoAttributeCriteriaServiceDTO serviceCriteria = new AutoAttributeCriteriaServiceDTO();
		
		AutoAttributeCriteria criteria = em.find( AutoAttributeCriteria.class, criteriaIn.getId() );
		
		serviceCriteria.setId( criteria.getId() );
		serviceCriteria.setTenantId( criteria.getTenant().getId() );
		serviceCriteria.setModified( criteria.getModified().toString() );
		
		serviceCriteria.setProductTypeId( criteria.getProductType().getId().toString() );
		
		List<String> inputs = new ArrayList<String>();
		for( InfoFieldBean field : criteria.getInputs() ) {
			inputs.add( field.getUniqueID().toString() );
		}
		serviceCriteria.setInputInfoFields( inputs );
		
		List<String> outputs = new ArrayList<String>();
		for( InfoFieldBean field : criteria.getOutputs() ) {
			outputs.add( field.getUniqueID().toString() );
		}
		serviceCriteria.setOutputInfoFields( outputs );
		
		List<AutoAttributeDefinitionServiceDTO> definitions = new ArrayList<AutoAttributeDefinitionServiceDTO>();
		for( AutoAttributeDefinition definition : criteria.getDefinitions() ) {
			definitions.add( convert_old( definition ) );
		}
		serviceCriteria.setDefinitions( definitions );
		
		
		
		return serviceCriteria;
	}
	
	public com.n4systems.webservice.dto.UserServiceDTO convert(UserBean user) {
		persistenceManager.reattach(user);
		
		com.n4systems.webservice.dto.UserServiceDTO userService = new com.n4systems.webservice.dto.UserServiceDTO();
		userService.setId(user.getId());
		userService.setUserId(user.getUserID().toLowerCase());
		userService.setHashPassword(user.getHashPassword());
		userService.setSecurityRfidNumber(user.getHashSecurityCardNumber());
		userService.setCustomerId(user.getR_EndUser() != null ? user.getR_EndUser() : NULL_ID);
		userService.setDivisionId(user.getR_Division() != null ? user.getR_Division() : NULL_ID);
		
		BitField permField = new BitField(user.getPermissions());
		userService.setAllowedToIdentify(permField.isSet(Permissions.TAG));
		userService.setAllowedToInspect(permField.isSet(Permissions.CREATEINSPECTION));
		
		return userService;
	}
	
	public UserBean convert(com.n4systems.webservice.dto.UserServiceDTO userDTO) {
		UserBean user = new UserBean();
		
		user.setUniqueID((userDTO.getId() == NULL_ID) ? null : userDTO.getId());
		user.setUserID(userDTO.getUserId());
		user.setR_EndUser((userDTO.getCustomerId() == NULL_ID) ? null : userDTO.getId());
		user.setR_Division((userDTO.getDivisionId() == NULL_ID) ? null : userDTO.getId());
		
		return user;
	}
	
	public ProductTypeGroupServiceDTO convert(ProductTypeGroup productTypeGroup) {
		
		ProductTypeGroupServiceDTO groupServiceDTO = new ProductTypeGroupServiceDTO();
		groupServiceDTO.setId(productTypeGroup.getId());
		groupServiceDTO.setName(productTypeGroup.getName());
		groupServiceDTO.setOrderIdx(productTypeGroup.getOrderIdx());
		
		return groupServiceDTO;
	}
	
	public TenantServiceDTO convert(TenantOrganization tenant) {
		
		TenantServiceDTO tenantService = new TenantServiceDTO();
		tenantService.setId(tenant.getId());
		tenantService.setName(tenant.getName());
		tenantService.setDisplayName(tenant.getDisplayName());
		tenantService.setSerialNumberFormat(tenant.getSerialNumberFormat());
		tenantService.setUsingJobSites(tenant.getExtendedFeatures().contains(ExtendedFeature.JobSites));
		tenantService.setUsingJobs(tenant.getExtendedFeatures().contains(ExtendedFeature.Projects));
		tenantService.setUsingSerialNumber(tenant.isUsingSerialNumber());
		
		return tenantService;
	}
	
	@SuppressWarnings("deprecation")
	public UserServiceDTO convert_old(UserBean user) {
		
		UserServiceDTO theDTO = new UserServiceDTO();
		theDTO.setFirstName(user.getFirstName());
		theDTO.setLastName(user.getLastName());
		theDTO.setInitials(user.getInitials());
		theDTO.setTenantId(user.getTenant().getId());
		theDTO.setR_Manufacture(user.getTenant().getId());		
		
		theDTO.setHashPassword(user.getHashPassword());
		theDTO.setUniqueID(user.getUniqueID().toString());
		theDTO.setUserID(user.getUserID());
		theDTO.setR_EndUser(user.getR_EndUser());
		theDTO.setHashSecurityCardNumber(user.getHashSecurityCardNumber());
		
		theDTO.setSerialNumberFormat(user.getTenant().getSerialNumberFormat());
		
		theDTO.setUsingJobSites( user.getTenant().getExtendedFeatures().contains(ExtendedFeature.JobSites) );
		
		return theDTO;
		
	}
	
	private AutoAttributeDefinitionServiceDTO convert_old( AutoAttributeDefinition definition ) {
		AutoAttributeDefinitionServiceDTO serviceDefinition = new AutoAttributeDefinitionServiceDTO();
		
		serviceDefinition.setId( definition.getId() );
		serviceDefinition.setTenantId( definition.getTenant().getId() );
		serviceDefinition.setAutoAttributeCritieraId( definition.getCriteria().getId() );
		serviceDefinition.setModified( definition.getModified().toString() );
		
		List<String> inputs = new ArrayList<String>();
		for( InfoOptionBean option : definition.getInputs() ) {
			inputs.add( option.getUniqueID().toString() );
		}
		serviceDefinition.setInputInfoOptions( inputs );
		
		List<InfoOptionServiceDTO> outputs = new ArrayList<InfoOptionServiceDTO>();
		for( InfoOptionBean option : definition.getOutputs() ) {
			outputs.add( convert_OLD( option ) );
		}
		serviceDefinition.setOutputInfoOptions( outputs );
		
		
		
		
		return serviceDefinition;
	}
	
	private InfoFieldBean findInfoField( String infoFieldIDString ) {
		InfoFieldBean infoField = null;
		
		Long infoFieldID = convertStringToLong( infoFieldIDString );
		if ( infoFieldID != null ) {
			infoField = (InfoFieldBean)em.find( InfoFieldBean.class, infoFieldID );
		}
		
		return infoField;
	}
	
	@SuppressWarnings("unused")
	private Customer findEndUser( String endUserIDString ) {
		Customer endUser = null;
		
		Long endUserID = convertStringToLong( endUserIDString );
		if ( endUserID != null ) {			
			endUser = (Customer)em.find( Customer.class, endUserID );			
		}
		
		return endUser;
	}
	
	@SuppressWarnings("unused")
	private UserBean findUser( String userIDString ) {
		UserBean user = null;
		
		Long userID = convertStringToLong( userIDString );
		if ( userID != null ) {
			user = (UserBean)em.find( UserBean.class, userID );
		}
		
		return user;		
	}
	
	@SuppressWarnings("unused")
	private ProductType findProductInfo( String productInfoIDString ) {
		ProductType productInfo = null;
		
		Long productInfoID = convertStringToLong( productInfoIDString );
		if ( productInfoID != null ) {
			productInfo = (ProductType)em.find( ProductType.class, productInfoID );			
		}
		
		return productInfo;
	}
	
	private Long convertStringToLong(String stringLong) {
		if (stringLong == null || stringLong.length() == 0) return null;
		
		Long longValue = null;
		try {
			longValue = Long.valueOf(stringLong);
		} catch (NumberFormatException e) {
			return null;
		}
		
		return longValue;
	}
	
	public Date convertStringToDate(String stringDate) {
		if (stringDate == null || stringDate.length() == 0) return null;
		
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
				logger.warn( "failed to parse string date " + stringDate.toString(), ee );
			}
		}
		
		return dateConvert;
	}

	public Date convertNextDate(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO) {		
		return convertStringToDate(inspectionServiceDTO.getNextDate());
	}

	public ProductStatusBean convertProductStatus(com.n4systems.webservice.dto.InspectionServiceDTO inspectionServiceDTO) {
		if (inspectionServiceDTO.getProductStatusId() == -1L) {
			return null;
		}
		
		return (ProductStatusBean)em.find(ProductStatusBean.class, inspectionServiceDTO.getProductStatusId());
	}

	public EndUserServiceDTO convertLegacy(Customer customer) {
		EndUserServiceDTO customerService = new EndUserServiceDTO();
		
		customerService.setUniqueID(customer.getId().toString());
		customerService.setTenantId(customer.getTenant().getId());
		customerService.setEndUserID(customer.getCustomerId());
		customerService.setEndUserName(customer.getName());

		customerService.setDivisions(convertAllLegacy(customerManager.findDivisions(customer, null)));
		
		return customerService;
	}
	
	public fieldid.web.services.dto.DivisionServiceDTO convertLegacy(Division division) {
		fieldid.web.services.dto.DivisionServiceDTO divisionService = new fieldid.web.services.dto.DivisionServiceDTO();
		
		divisionService.setUniqueID(division.getId().toString());
		divisionService.setTenantId(division.getCustomer().getTenant().getId());
		divisionService.setR_EndUser(division.getCustomer().getId().toString());
		divisionService.setName(division.getName());
		
		return divisionService;
	}

	public JobServiceDTO convert(Project job) {
		JobServiceDTO jobService = new JobServiceDTO();
		
		jobService.setId(job.getId());
		jobService.setCustomerId(job.getCustomer() != null ? job.getCustomer().getId() : NULL_ID);
		jobService.setDivisionId(job.getDivision() != null ? job.getDivision().getId() : NULL_ID);
		jobService.setJobSiteId(job.getJobSite() != null ? job.getJobSite().getId() : NULL_ID);
		jobService.setName(job.getName());
		jobService.setProjectId(job.getProjectID());
		
		for (UserBean user : job.getResources()) {
			jobService.getResourceUserIds().add(user.getUniqueID());
		}
		
		return jobService;
	}
	
	public CustomerServiceDTO convert(Customer customer, List<Division> divisions) {
		CustomerServiceDTO customerService = new CustomerServiceDTO();
		
		customerService.setId(customer.getId());
		customerService.setCustomerId(customer.getCustomerId());
		customerService.setName(customer.getDisplayName());
		
		if (customer.getContact() != null) {
			customerService.setContactName(customer.getContact().getName());
			customerService.setContactEmail(customer.getContact().getEmail());
		}
		
		if (divisions != null) {
			customerService.getDivisions().addAll(convertAll(divisions));
		}
		
		return customerService;
	}
	
	public DivisionServiceDTO convert(Division division) {
		DivisionServiceDTO divisionService = new DivisionServiceDTO();
		
		divisionService.setId(division.getId());
		divisionService.setName(division.getName());
		
		return divisionService;
	}

	private InspectionScheduleServiceDTO convert(InspectionSchedule inspectionSchedule) {
		InspectionScheduleServiceDTO scheduleService = new InspectionScheduleServiceDTO();
		
		scheduleService.setId(inspectionSchedule.getId());
		scheduleService.setNextDate( AbstractBaseServiceDTO.dateToString(inspectionSchedule.getNextDate()) );
		scheduleService.setProductId(inspectionSchedule.getProduct().getId());
		scheduleService.setInspectionTypeId(inspectionSchedule.getInspectionType().getId());
		scheduleService.setJobId(inspectionSchedule.getProject() != null ? inspectionSchedule.getProject().getId() : NULL_ID);
		scheduleService.setCompleted(inspectionSchedule.getStatus() == InspectionSchedule.ScheduleStatus.COMPLETED);
		
		return scheduleService;
	}

	public List<DivisionServiceDTO> convertAll(List<Division> divisions) {
		List<DivisionServiceDTO> divisionServices = new ArrayList<DivisionServiceDTO>();
		
		for (Division division: divisions) {
			divisionServices.add(convert(division));
		}
		
		return divisionServices;
	}
	
	@Deprecated
	public List<fieldid.web.services.dto.DivisionServiceDTO> convertAllLegacy(List<Division> divisions) {
		List<fieldid.web.services.dto.DivisionServiceDTO> divisionServices = new ArrayList<fieldid.web.services.dto.DivisionServiceDTO>();
		
		for(Division division: divisions) {
			divisionServices.add(convertLegacy(division));
		}
		
		return divisionServices;
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
}
