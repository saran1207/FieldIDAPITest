package com.n4systems.ejb.legacy;

import com.n4systems.model.AssetType;
import rfid.ejb.entity.AssetCodeMapping;

import java.util.List;

public interface AssetCodeMappingService {
	
	
	public List<AssetCodeMapping> getAllAssetCodesByTenant( Long manufacturer );

	
	public AssetCodeMapping getAssetCodeByUniqueIdAndTenant(Long id, Long manufacturer );
	
	public AssetCodeMapping getAssetCodeByAssetCodeAndTenant(String assetCode, Long manufacturer );

	public void update(AssetCodeMapping assetCodeMapping);
	
	public void deleteByIdAndTenant(Long id, Long manufacturer );
	public void clearRetiredInfoFields( AssetType assetType);

}
