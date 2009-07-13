package rfid.ejb.session;

import java.util.List;

import javax.ejb.Local;

import com.n4systems.model.ProductType;

import rfid.ejb.entity.ProductCodeMappingBean;

@Local
public interface ProductCodeMapping {
	
	public void save(ProductCodeMappingBean bean);
	
	public List<ProductCodeMappingBean> getAllProductCodes();
	public List<ProductCodeMappingBean> getAllProductCodesByTenant( Long manufacturer );

	public ProductCodeMappingBean getProductCodeByUniqueId(Long id);
	public ProductCodeMappingBean getProductCodeByUniqueIdAndTenant(Long id, Long manufacturer );
	
	public ProductCodeMappingBean getProductCodeByProductCodeAndTenant(String productCode, Long manufacturer );

	public void update(ProductCodeMappingBean productCodeMapping);

	public void deleteById(Long uniqueID);
	public void deleteByIdAndTenant(Long id, Long manufacturer );
	public void clearRetiredInfoFields( ProductType productType );

}
