package fieldid.web.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.dto.CommentTempDTO;
import rfid.ejb.entity.ProductStatusBean;
import rfid.ejb.session.ServiceDTOBeanConverter;

import com.n4systems.model.UnitOfMeasure;
import com.n4systems.util.DateHelper;
import com.n4systems.util.ServiceLocator;

import fieldid.web.services.dto.CommentTemplateServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;
import fieldid.web.services.dto.UnitOfMeasureServiceDTO;



public class UserServiceImpl implements IUserService {
	
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	private static final Long DEFAULT_SERVICE_VERSION = 1L;
	
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
	
}
