package fieldid.web.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import rfid.ejb.entity.AssetStatus;
import rfid.ejb.entity.CommentTempBean;

import com.n4systems.ejb.legacy.ServiceDTOBeanConverter;
import com.n4systems.model.UnitOfMeasure;
import com.n4systems.persistence.loaders.TenantFilteredListLoader;
import com.n4systems.util.ServiceLocator;

import fieldid.web.services.dto.CommentTemplateServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;
import fieldid.web.services.dto.UnitOfMeasureServiceDTO;



public class UserServiceImpl implements IUserService {
	
	private static Logger logger = Logger.getLogger(UserServiceImpl.class);
	private static final Long DEFAULT_SERVICE_VERSION = 1L;
	
	public List<ProductStatusServiceDTO> findProductStatus( long versionNumber, Long tenantId, Date beginDate )	{
		
		List<AssetStatus> assetStatusList = ServiceLocator.getAssetManager().findAssetStatus(tenantId, beginDate);
		
		ServiceDTOBeanConverter serviceDTOBeanConverter = ServiceLocator.getServiceDTOBeanConverter();
		
		List<ProductStatusServiceDTO> productStatusServiceDTOs = new ArrayList<ProductStatusServiceDTO>();
		if (assetStatusList != null) {
			for (AssetStatus assetStatus : assetStatusList) {
				productStatusServiceDTOs.add( serviceDTOBeanConverter.convert(assetStatus) );
			}
		}
		
		return productStatusServiceDTOs;		
	}
	
	public List<UnitOfMeasureServiceDTO> getUnitOfMeasureForDate( Date beginDate) {
		return findUnitOfMeasureForDate( DEFAULT_SERVICE_VERSION, beginDate);
	}
	public List<UnitOfMeasureServiceDTO> findUnitOfMeasureForDate(Long versionNumber,Date beginDate) {
		
		Collection<UnitOfMeasure> unitOfMeasures = null;
		try {
			unitOfMeasures = ServiceLocator.getPersistenceManager().findAll( UnitOfMeasure.class ) ;
		} catch (Exception e) {
			logger.error("Loading unit of measures", e);
		}
		
		List<UnitOfMeasureServiceDTO> unitOfMeasureServiceList = new ArrayList<UnitOfMeasureServiceDTO>();
		for (UnitOfMeasure unitOfMeasure : unitOfMeasures) {
			unitOfMeasureServiceList.add(createUnitOfMeasureServiceDTO(unitOfMeasure));
		}
		
		return unitOfMeasureServiceList;
	}
	
	
	public List<CommentTemplateServiceDTO> getCommentTemplateForDate(Long tenantId, Date beginDate) {
		return findCommentTemplateForDate( DEFAULT_SERVICE_VERSION , tenantId, beginDate);
	}
	
	public List<CommentTemplateServiceDTO> findCommentTemplateForDate(Long versionNumber, Long tenantId, Date beginDate) {
		List<CommentTempBean> ct = null;
		try {
			ct = new TenantFilteredListLoader<CommentTempBean>(tenantId, CommentTempBean.class).load();
		} catch (Exception e) {
			logger.error("finding comment templates", e);
		}
		
		List<CommentTemplateServiceDTO> ctsList = new ArrayList<CommentTemplateServiceDTO>();
		if (ct != null) {
			try {
				for (CommentTempBean template: ct) {
					CommentTemplateServiceDTO commentTemplate = createCommentTemplateServiceDTO(template);
					ctsList.add(commentTemplate);
				}
			} catch (Exception e) {
				logger.error("finding comment templates", e);
				ctsList = new ArrayList<CommentTemplateServiceDTO>();
			}
		}
		
		return ctsList;
	}
	
	private CommentTemplateServiceDTO createCommentTemplateServiceDTO(CommentTempBean commentTemplate) {
		CommentTemplateServiceDTO ctsDTO = new CommentTemplateServiceDTO();
		ctsDTO.setDtoVersion( CommentTemplateServiceDTO.CURRENT_DTO_VERSION.toString() );
		ctsDTO.setContents(commentTemplate.getContents());
		ctsDTO.setModifiedBy(commentTemplate.getModifiedBy());
		ctsDTO.setTemplateID(commentTemplate.getTemplateID());
		ctsDTO.setUniqueID(commentTemplate.getUniqueID().toString());
		ctsDTO.setTenantId(commentTemplate.getTenant().getId().toString());
		ctsDTO.setDateModified(commentTemplate.getDateModified().toString());
		
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
