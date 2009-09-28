package fieldid.web.services;

import java.util.Date;
import java.util.List;

import fieldid.web.services.dto.CommentTemplateServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;
import fieldid.web.services.dto.UnitOfMeasureServiceDTO;


public interface IUserService  {
	public List<ProductStatusServiceDTO> findProductStatus( long versionNumber, Long tenantId, Date beginDate );
	public List<UnitOfMeasureServiceDTO> getUnitOfMeasureForDate(Date beginDate);
	public List<UnitOfMeasureServiceDTO> findUnitOfMeasureForDate(Long versionNumber, Date beginDate);
	public List<CommentTemplateServiceDTO> getCommentTemplateForDate(Long rManufacturerId, Date beginDate);
	public List<CommentTemplateServiceDTO> findCommentTemplateForDate(Long versionNumber, Long rManufacturerId, Date beginDate);
	
}