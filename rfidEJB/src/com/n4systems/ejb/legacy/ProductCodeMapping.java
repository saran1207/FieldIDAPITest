package com.n4systems.ejb.legacy;

import java.util.List;

import rfid.ejb.entity.ProductCodeMappingBean;

import com.n4systems.model.ProductType;

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
