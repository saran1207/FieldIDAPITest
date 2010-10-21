package com.n4systems.ejb.legacy;

import java.util.List;

import rfid.ejb.entity.AssetCodeMapping;

import com.n4systems.model.AssetType;

public interface AssetCodeMappingService {
	
	
	public List<AssetCodeMapping> getAllProductCodesByTenant( Long manufacturer );

	
	public AssetCodeMapping getProductCodeByUniqueIdAndTenant(Long id, Long manufacturer );
	
	public AssetCodeMapping getProductCodeByProductCodeAndTenant(String productCode, Long manufacturer );

	public void update(AssetCodeMapping assetCodeMapping);
	
	public void deleteByIdAndTenant(Long id, Long manufacturer );
	public void clearRetiredInfoFields( AssetType assetType);

}
