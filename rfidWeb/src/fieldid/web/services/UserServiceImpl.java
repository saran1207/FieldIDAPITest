package fieldid.web.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import rfid.dto.CommentTempDTO;
import rfid.ejb.entity.FindProductOptionManufactureBean;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.LegacyProductSerial;
import rfid.ejb.session.Option;
import rfid.ejb.session.PopulatorLog;
import rfid.ejb.session.ServiceDTOBeanConverter;
import rfid.util.PopulatorLogger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.web.services.exceptions.InvalidTenantException;
import com.n4systems.fieldid.web.services.exceptions.WebServiceLoadingFailure;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.Product;
import com.n4systems.model.Tenant;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.DateHelper;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.ServiceLocator;
import com.n4systems.webservice.dto.ProductServiceDTO;
import com.n4systems.webservice.dto.RequestInformation;
import com.n4systems.webservice.server.DataService;
import com.n4systems.webservice.server.DataServiceImpl;

import fieldid.web.services.dto.AutoAttributeCriteriaServiceDTO;
import fieldid.web.services.dto.CommentTemplateServiceDTO;
import fieldid.web.services.dto.EndUserServiceDTO;
import fieldid.web.services.dto.FindProductOptionServiceDTO;
import fieldid.web.services.dto.InspectionBookServiceDTO;
import fieldid.web.services.dto.ProductSerialServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;
import fieldid.web.services.dto.SerialNumberCounterServiceDTO;
import fieldid.web.services.dto.TagOptionManufacturerServiceDTO;
import fieldid.web.services.dto.UnitOfMeasureServiceDTO;



public class UserServiceImpl implements IUserService {
	
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	
	
	private static final Long DEFAULT_SERVICE_VERSION = 1L;
	
	
	
	/**
	 * retrieves the auto attributes packages that have been modified since the given date and for the given tenant.
	 * 
	 * 
	 */
	public Collection<AutoAttributeCriteriaServiceDTO> findAllAutoAttributes(Long versionNumber, Long tenantId, Date beginDate) throws Exception {
		PersistenceManager persistenceManager = ServiceLocator.getPersistenceManager();
		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
		
		List<AutoAttributeCriteria> criterias = null;
		
		try {
			criterias = persistenceManager.findAllByDate( AutoAttributeCriteria.class, tenantId, beginDate, new Date(), 0L, null );
		} catch (Exception e) {
			logger.error("Finding criteria", e);
			throw new WebServiceLoadingFailure("Failure loading AutoAttributeCriteria", e ) ;
		}
		
		List<AutoAttributeCriteriaServiceDTO> criteriasServiceDTOs = new ArrayList<AutoAttributeCriteriaServiceDTO>();
		
		for (AutoAttributeCriteria criteria : criterias) {
			criteriasServiceDTOs.add( converter.convert_old( criteria ) );
		}
				
		return criteriasServiceDTOs;
	}
	
	
	/**
	 * Retrieve the serial number counter for the given manufacturer.  Don't get the
	 * counter, that will be set by the mobile
	 * 
	 */
	public SerialNumberCounterServiceDTO findSerialNumberCounter(Long versionNumber, long tenantId) throws Exception {
		// TODO implement this.		
		return null;
		
	}
	
	public List<ProductStatusServiceDTO> findProductStatus( long versionNumber, Long tenantId, Date beginDate )	{
		
		List<ProductStatusBean> productStatusList = ServiceLocator.getProductSerialManager().findProductStatus(tenantId, beginDate);
		
		ServiceDTOBeanConverter serviceDTOBeanConverter = ServiceLocator.getServiceDTOBeanConverter();
		
		List<ProductStatusServiceDTO> productStatusServiceDTOs = new ArrayList<ProductStatusServiceDTO>();
		if (productStatusList != null) {
			for (ProductStatusBean productStatus : productStatusList) {
				productStatusServiceDTOs.add( serviceDTOBeanConverter.convert(productStatus) );
			}
		}
		
		return productStatusServiceDTOs;		
	}
	
	/**
	 * Retrieve product serial dtos for the given list of end users or divisions
	 * @param tenantId
	 * @param endUserList
	 * @param divisionList
	 * @param lastId
	 * @return
	 */
	public ArrayList<ProductSerialServiceDTO> findProductSerialForEndUserAndDivision(Long versionNumber,
			Long tenantId, long[] endUserList, long[] divisionList,Date beginDate, 
			Long lastId){
		
		ServiceDTOBeanConverter beanConverter = ServiceLocator.getServiceDTOBeanConverter();
		LegacyProductSerial productSerialManager = ServiceLocator.getProductSerialManager();
		
		int MAX_RESULTS = ConfigContext.getCurrentContext().getInteger(ConfigEntry.MOBILE_PAGESIZE_PRODUCTS);
		
		// THIS FUCKING SUCKS.. I HATE  IT
		Long[] endUserList2 = new Long[endUserList.length];
		for (int i = 0; i < endUserList.length; i++) {
			endUserList2[i] = endUserList[i];
		}
		
		Long[] divisionList2 = new Long[divisionList.length];
		for (int i=0; i< divisionList.length; i++) {
			divisionList2[i] = divisionList[i];
		}
			
		List<Product> productSerials = productSerialManager.findProductSerialByEndUserDivision(tenantId, endUserList2, divisionList2, beginDate, MAX_RESULTS, lastId);
		
		ArrayList<ProductSerialServiceDTO> productDTOs = new ArrayList<ProductSerialServiceDTO>();
		
		boolean moreData = (productSerials.size() > MAX_RESULTS);
		
		for (Product productSerial : productSerials) {
			productDTOs.add( beanConverter.convert(productSerial, moreData) );
		}
		
		return productDTOs;
	}
	
	public ArrayList<UnitOfMeasureServiceDTO> getUnitOfMeasureForDate( Date beginDate) {
		return findUnitOfMeasureForDate( DEFAULT_SERVICE_VERSION, beginDate);
	}
	/**
	 * 
	 */
	public ArrayList<UnitOfMeasureServiceDTO> findUnitOfMeasureForDate(Long versionNumber,Date beginDate) {
		
		Collection<UnitOfMeasure> unitOfMeasures = null;
		try {
			unitOfMeasures = ServiceLocator.getPersistenceManager().findAll( UnitOfMeasure.class ) ;
		} catch (Exception e) {
			logger.error("Loading unit of measures", e);
		}
		
		ArrayList<UnitOfMeasureServiceDTO> unitOfMeasureServiceList = new ArrayList<UnitOfMeasureServiceDTO>();
		for (UnitOfMeasure unitOfMeasure : unitOfMeasures) {
			unitOfMeasureServiceList.add(createUnitOfMeasureServiceDTO(unitOfMeasure));
		}
		
		return unitOfMeasureServiceList;
	}
	
	
	public ArrayList<CommentTemplateServiceDTO> getCommentTemplateForDate(Long tenantId, Date beginDate) {
		return findCommentTemplateForDate( DEFAULT_SERVICE_VERSION , tenantId, beginDate);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CommentTemplateServiceDTO> findCommentTemplateForDate(Long versionNumber, Long tenantId, Date beginDate) {
		ArrayList ct = null;
		try {
			ct = ServiceLocator.getCommentTemp().findCommentTemplateByDate(tenantId, beginDate, DateHelper.getTomorrow());
		} catch (Exception e) {
			logger.error("finding comment templates", e);
		}
		
		ArrayList<CommentTemplateServiceDTO> ctsList = new ArrayList<CommentTemplateServiceDTO>();
		if (ct != null) {
			try {
				for (Iterator i = ct.iterator(); i.hasNext();) {
					CommentTemplateServiceDTO commentTemplate = createCommentTemplateServiceDTO((CommentTempDTO)i.next() );
					ctsList.add(commentTemplate);
				}
			} catch (Exception e) {
				logger.error("finding comment templates", e);
				ctsList = new ArrayList<CommentTemplateServiceDTO>();
			}
		}
		
		return ctsList;
	}

	public ArrayList<EndUserServiceDTO> getEndUsersForDate( Long rManufacturer, Date beginDate) {
		return findEndUsersForDate( DEFAULT_SERVICE_VERSION, rManufacturer, beginDate);
	}
	
	@SuppressWarnings("deprecation")
	public ArrayList<EndUserServiceDTO> findEndUsersForDate(Long versionNumber, Long tenantId, Date beginDate) {
	

		List<Customer> customerList = ServiceLocator.getCustomerManager().findCustomers(tenantId, beginDate, new SecurityFilter(tenantId));

		EndUserServiceDTO customerService;
		ArrayList<EndUserServiceDTO> returnList = new ArrayList<EndUserServiceDTO>();
		try {
			
			for(Customer customer: customerList){
				customerService = ServiceLocator.getServiceDTOBeanConverter().convertLegacy(customer);
				returnList.add(customerService);
			}
			
		} catch( Exception e ) {
			returnList = new ArrayList<EndUserServiceDTO>();
			logger.error( "End user could not be downgraded", e);
		}
		
		return returnList;
	}
	
	@SuppressWarnings("unused")
	private Tenant getTenantBean( Long tenantId ) throws InvalidTenantException {
		// For backwards compatability.  If they didn't send over the user, we use the manufacturers default organizational unit
		// need to load the product serial to look at which one that is
		Tenant tenant = null ;
		try {
			
			tenant =  (Tenant)ServiceLocator.getPersistenceManager().find( Tenant.class, tenantId );
		} catch (Exception e) {
			logger.error("Loading tenant organizational unit", e);
			throw new InvalidTenantException(e);
			
		}
		
		if( tenant == null ) {
			throw new InvalidTenantException();
		}
		return tenant;
	}
	
	

	// XXX this is a temporary method used to fix a bug on the handheld, should eventually be removed
	@SuppressWarnings("unused")
	private boolean doesDivisionReallyExist(Long tenantId, Long rDivision) {
		
		Division division = null;
		try {
			division = ServiceLocator.getPersistenceManager().find( Division.class, rDivision );
		} catch (Exception e) {
			logger.warn("Could not find the division sent in by the handheld", e);
		}

		if (division == null) {
			PopulatorLogger.getInstance().logMessage(tenantId,
					"Could not find the division referenced.",PopulatorLog.logType.mobile, PopulatorLog.logStatus.error);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	public Long TagProduct(ProductSerialServiceDTO productSerialServiceDTO) throws Exception {
		
		// TRANSLATE THIS TO THE NEW WAY AND PASS IT TO THE DATASERVICE
		
		RequestInformation requestInformation = new RequestInformation();
		requestInformation.setTenantId(productSerialServiceDTO.getTenantIdLong());
		requestInformation.setMobileGuid(productSerialServiceDTO.getMobileGUID());
		
		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
		
		ProductServiceDTO product = converter.convert(productSerialServiceDTO);
		
		DataService dataService = new DataServiceImpl();
		
		dataService.createProduct(requestInformation, product);
		
		return 0L;

	}
	
	public Long UpdateProduct( ProductSerialServiceDTO productSerialServiceDTO ) throws Exception {
		RequestInformation requestInformation = new RequestInformation();
		requestInformation.setTenantId(productSerialServiceDTO.getTenantIdLong());
		
		
		ServiceDTOBeanConverter converter = ServiceLocator.getServiceDTOBeanConverter();
		
		ProductServiceDTO product = converter.convert(productSerialServiceDTO);
		
		DataService dataService = new DataServiceImpl();
		
		dataService.updateProduct(requestInformation, product);
		
		return 0L;
	}
	
	
	
	
	/**
	 * Change the RFID number on a product serial
	 */
	public void changeRFIDNumber(Long rProductSerial, String newRFIDNumber){
		
	}
	
	
	public ArrayList<TagOptionManufacturerServiceDTO> getTagOptionsForManufacturer( Long rManufacturerId ){
		return new ArrayList<TagOptionManufacturerServiceDTO>();
	}
	
	public ArrayList<TagOptionManufacturerServiceDTO> findTagOptionsForManufacturer(Long versionNumber, Long tenantId) {
		return new ArrayList<TagOptionManufacturerServiceDTO>();
	}
	
	public ArrayList<InspectionBookServiceDTO> getInspectionBookByDate( Long manufacturerId, Date beginDate){
		return findInspectionBookByDate( DEFAULT_SERVICE_VERSION, manufacturerId, beginDate);
	}
	
	public ArrayList<InspectionBookServiceDTO> findInspectionBookByDate( Long versionNumber, Long tenantId, Date beginDate){
		return new ArrayList<InspectionBookServiceDTO>();
	}
	
	public ArrayList<FindProductOptionServiceDTO> getFindProductOptionForManufacturer( Long rManufacturerId ){
		return findFindProductOptionForManufacturer(DEFAULT_SERVICE_VERSION, rManufacturerId );
	}
	
	
	@Deprecated // Since Mobile Version 1.12
	public ArrayList<FindProductOptionServiceDTO> findFindProductOptionForManufacturer( Long versionNumber, Long tenantId){
		Option optionManager = ServiceLocator.getOption();
		
		Collection<FindProductOptionManufactureBean> unsortedOptions = optionManager.getFindProductOptionsForTenant(tenantId);
		
		SortedSet<FindProductOptionManufactureBean> sortedOptions = new TreeSet<FindProductOptionManufactureBean>();
		
		if (unsortedOptions != null) {
			sortedOptions.addAll(unsortedOptions);
		}
				
		ArrayList<FindProductOptionServiceDTO> returnList = new ArrayList<FindProductOptionServiceDTO>();
		
		try {
			for(FindProductOptionManufactureBean currentFpomb: sortedOptions){
				FindProductOptionServiceDTO fpo = createFindProductOptionServiceDTO(currentFpomb);
				returnList.add( fpo );
			}
		} catch( Exception e ) {
			logger.error( "Failed to downgrade a findProductOption", e );
			returnList = new ArrayList<FindProductOptionServiceDTO>();
		}
		
		return returnList;
	}
	
	
	
	/**
	 * Convert an array of long privitive types to an arraylist of Long object types
	 * @param inputArray
	 * @return
	 */
	@SuppressWarnings("unused")
	private ArrayList<Long> getListFromArrayLong(long[] inputArray){
		
		ArrayList<Long> returnList = new ArrayList<Long>(inputArray.length);
		for(int i = 0; i < inputArray.length; i++)
			returnList.add(inputArray[i]);
		
		return returnList;
	}

	
	/**
	 * 
	 * @param fpomBean
	 * @return
	 */
	private FindProductOptionServiceDTO createFindProductOptionServiceDTO(FindProductOptionManufactureBean fpomBean){
		
		FindProductOptionServiceDTO fposDTO = new FindProductOptionServiceDTO();
		fposDTO.setDtoVersion( FindProductOptionServiceDTO.CURRENT_DTO_VERSION.toString() );
		fposDTO.setUniqueID(fpomBean.getUniqueID().toString());
		fposDTO.setMobileWeight(fpomBean.getMobileWeight());
		fposDTO.setKey(fpomBean.getFindProductOption().getKey());
		fposDTO.setValue(fpomBean.getFindProductOption().getValue());
		fposDTO.setTenantId( fpomBean.getTenant().getId() );
		
		return fposDTO;
	}
	
	private CommentTemplateServiceDTO createCommentTemplateServiceDTO(CommentTempDTO ctDTO) {
		CommentTemplateServiceDTO ctsDTO = new CommentTemplateServiceDTO();
		ctsDTO.setDtoVersion( CommentTemplateServiceDTO.CURRENT_DTO_VERSION.toString() );
		ctsDTO.setContents(ctDTO.getContents());
		ctsDTO.setModifiedBy(ctDTO.getModifiedBy());
		ctsDTO.setTemplateID(ctDTO.getTemplateID());
		ctsDTO.setUniqueID(ctDTO.getUniqueID().toString());
		ctsDTO.setTenantId(ctDTO.getTenantId().toString());
		ctsDTO.setDateModified(ctDTO.getDateModified().toString());
		
		return ctsDTO;
		
	}
	
	private UnitOfMeasureServiceDTO createUnitOfMeasureServiceDTO( UnitOfMeasure unitOfMeasure) {
		UnitOfMeasureServiceDTO unitOfMeasureDTO = new UnitOfMeasureServiceDTO();
		unitOfMeasureDTO.setDtoVersion( UnitOfMeasureServiceDTO.CURRENT_DTO_VERSION.toString() );
		unitOfMeasureDTO.setUniqueID(unitOfMeasure.getId().toString());
		unitOfMeasureDTO.setR_unitofmeasure(unitOfMeasure.getChild() != null ? unitOfMeasure.getChild().getId() : null);
		unitOfMeasureDTO.setSelectable(unitOfMeasure.isSelectable());
		unitOfMeasureDTO.setUnitName(unitOfMeasure.getName());
		unitOfMeasureDTO.setUnitShortName(unitOfMeasure.getShortName());
		unitOfMeasureDTO.setUnitType(unitOfMeasure.getType());
		
		return unitOfMeasureDTO;
	}
	
	
	@SuppressWarnings("unused")
	private Date getTomorrow() {
	    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	    java.util.Date date = new Date();
	    cal.setTime(date);
	    cal.add(Calendar.DAY_OF_MONTH, 1);
	    date = cal.getTime();	    
	    return date;
	}
	
}
