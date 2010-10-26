package com.n4systems.ejb.legacy;

import java.util.List;

import rfid.ejb.entity.AssetCodeMapping;

import com.n4systems.model.AssetType;

public interface AssetCodeMappingService {
	
	
	public List<AssetCodeMapping> getAllAssetCodesByTenant( Long manufacturer );

	
	public AssetCodeMapping getAssetCodeByUniqueIdAndTenant(Long id, Long manufacturer );
	
	public AssetCodeMapping getAssetCodeByProductCodeAndTenant(String productCode, Long manufacturer );

	public void update(AssetCodeMapping assetCodeMapping);
	
	public void deleteByIdAndTenant(Long id, Long manufacturer );
	public void clearRetiredInfoFields( AssetType assetType);

}
