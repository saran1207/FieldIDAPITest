package fieldid.web.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import fieldid.web.services.dto.AutoAttributeCriteriaServiceDTO;
import fieldid.web.services.dto.CommentTemplateServiceDTO;
import fieldid.web.services.dto.EndUserServiceDTO;
import fieldid.web.services.dto.FindProductOptionServiceDTO;
import fieldid.web.services.dto.ProductSerialServiceDTO;
import fieldid.web.services.dto.ProductStatusServiceDTO;
import fieldid.web.services.dto.SerialNumberCounterServiceDTO;
import fieldid.web.services.dto.TagOptionManufacturerServiceDTO;
import fieldid.web.services.dto.UnitOfMeasureServiceDTO;


public interface IUserService  {
	
	
	public ArrayList<ProductSerialServiceDTO> findProductSerialForEndUserAndDivision(Long versionNumber, Long rManufacturerId, long[] endUserList, long[] divisionList, Date beginDate,Long lastId);
	public SerialNumberCounterServiceDTO findSerialNumberCounter(Long versionNumber, long tenantId) throws Exception;
	public List<ProductStatusServiceDTO> findProductStatus( long versionNumber, Long tenantId, Date beginDate );
	public ArrayList<EndUserServiceDTO> getEndUsersForDate(Long rManufacturer, Date beginDate);
	public ArrayList<EndUserServiceDTO> findEndUsersForDate(Long versionNumber, Long rManufacturer, Date beginDate);
	public ArrayList<UnitOfMeasureServiceDTO> getUnitOfMeasureForDate(Date beginDate);
	public ArrayList<UnitOfMeasureServiceDTO> findUnitOfMeasureForDate(Long versionNumber, Date beginDate);
	public ArrayList<CommentTemplateServiceDTO> getCommentTemplateForDate(Long rManufacturerId, Date beginDate);
	public ArrayList<CommentTemplateServiceDTO> findCommentTemplateForDate(Long versionNumber, Long rManufacturerId, Date beginDate);

	public Long TagProduct(ProductSerialServiceDTO productSerialServiceDTO) throws Exception;
	public Long UpdateProduct( ProductSerialServiceDTO productSerialServiceDTO ) throws Exception;
	public void changeRFIDNumber(Long rProductSerial, String newRFIDNumber);
	
	public ArrayList<TagOptionManufacturerServiceDTO> getTagOptionsForManufacturer(Long rManufacturerId);
	public ArrayList<TagOptionManufacturerServiceDTO> findTagOptionsForManufacturer(Long versionNumber, Long rManufacturerId);
	public ArrayList<FindProductOptionServiceDTO> getFindProductOptionForManufacturer(Long rManufacturerId);
	public ArrayList<FindProductOptionServiceDTO> findFindProductOptionForManufacturer(Long versionNumber, Long rManufacturerId);
	public Collection<AutoAttributeCriteriaServiceDTO> findAllAutoAttributes(Long versionNumber, Long tenantId, Date beginDate) throws Exception;
	
}