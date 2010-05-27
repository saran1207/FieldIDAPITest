package com.n4systems.ejb.legacy;

import java.util.List;

import rfid.ejb.entity.ProductCodeMappingBean;

import com.n4systems.model.ProductType;

public interface ProductCodeMapping {
	
	
	public List<ProductCodeMappingBean> getAllProductCodesByTenant( Long manufacturer );

	
	public ProductCodeMappingBean getProductCodeByUniqueIdAndTenant(Long id, Long manufacturer );
	
	public ProductCodeMappingBean getProductCodeByProductCodeAndTenant(String productCode, Long manufacturer );

	public void update(ProductCodeMappingBean productCodeMapping);
	
	public void deleteByIdAndTenant(Long id, Long manufacturer );
	public void clearRetiredInfoFields( ProductType productType );

}
