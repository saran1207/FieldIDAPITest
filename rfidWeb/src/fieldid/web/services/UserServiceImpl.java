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
import rfid.ejb.session.Option;
import rfid.ejb.session.ServiceDTOBeanConverter;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.NotImplementedException;
import com.n4systems.fieldid.web.services.exceptions.InvalidTenantException;
import com.n4systems.fieldid.web.services.exceptions.WebServiceLoadingFailure;
import com.n4systems.model.AutoAttributeCriteria;
import com.n4systems.model.Tenant;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ServiceLocator;

import fieldid.web.services.dto.AutoAttributeCriteriaServiceDTO;
import fieldid.web.services.dto.CommentTemplateServiceDTO;
import fieldid.web.services.dto.EndUserServiceDTO;
import fieldid.web.services.dto.FindProductOptionServiceDTO;
import fieldid.web.services.dto.InspectionBookServiceDTO;
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

	public ArrayList<EndUserServiceDTO> findEndUsersForDate(Long versionNumber, Long tenantId, Date beginDate) {
		
		// TODO: CUSTOMER_REFACTOR: UserService need to return a list of CustomerOrg's by date
		return new ArrayList<EndUserServiceDTO>();
//		List<Customer> customerList = ServiceLocator.getCustomerManager().findCustomers(tenantId, beginDate, new LegacySecurityFilter(tenantId));
//
//		EndUserServiceDTO customerService;
//		ArrayList<EndUserServiceDTO> returnList = new ArrayList<EndUserServiceDTO>();
//		try {
//			
//			for(Customer customer: customerList){
//				customerService = ServiceLocator.getServiceDTOBeanConverter().convertLegacy(customer);
//				returnList.add(customerService);
//			}
//			
//		} catch( Exception e ) {
//			returnList = new ArrayList<EndUserServiceDTO>();
//			logger.error( "End user could not be downgraded", e);
//		}
//		
//		return returnList;
	}
	
	@SuppressWarnings("unused")
	private Tenant getTenantBean( Long tenantId ) throws InvalidTenantException {
		// For backwards compatability.  If they didn't send over the user, we use the manufacturers default organizational unit
		// need to load the product serial to look at which one that is
		Tenant tenant = null ;
		try {
			
			tenant =  ServiceLocator.getPersistenceManager().find( Tenant.class, tenantId );
		} catch (Exception e) {
			logger.error("Loading tenant organizational unit", e);
			throw new InvalidTenantException(e);
			
		}
		
		if( tenant == null ) {
			throw new InvalidTenantException();
		}
		return tenant;
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

	public void changeRFIDNumber(Long rProductSerial, String newRFIDNumber) {
		throw new NotImplementedException("changeRFIDNumber(Long, String) has been disabled");
	}

	public ArrayList<TagOptionManufacturerServiceDTO> findTagOptionsForManufacturer(Long versionNumber, Long rManufacturerId) {
		throw new NotImplementedException("findTagOptionsForManufacturer(Long, Long) has been disabled");
	}

	public ArrayList<TagOptionManufacturerServiceDTO> getTagOptionsForManufacturer(Long rManufacturerId) {
		throw new NotImplementedException("getTagOptionsForManufacturer(Long) has been disabled");
	}
	
}
