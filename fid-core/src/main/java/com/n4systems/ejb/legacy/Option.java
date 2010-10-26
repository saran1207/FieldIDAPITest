package com.n4systems.ejb.legacy;

import java.util.Collection;
import java.util.List;

import rfid.ejb.entity.FindAssetOptionManufacture;

import com.n4systems.model.TagOption;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.model.security.SecurityFilter;

public interface Option {
	
	//FindAssetOptionManufacture
	public Collection<FindAssetOptionManufacture> getFindAssetOptionsForTenant(Long tenantId);
	
	public Collection<FindAssetOptionManufacture> getAllFindAssetOptionManufacture();
	public FindAssetOptionManufacture getFindAssetOptionManufacture(Long uniqueID);
	public void updateFindAssetOptionManufacture(FindAssetOptionManufacture findAssetOptionManufacturer);
	
	
	//FindAssetOption
	
	
	public List<TagOption> findTagOptions(SecurityFilter filter);
	public TagOption findTagOption(Long id, SecurityFilter filter);
	public TagOption findTagOption(OptionKey key, SecurityFilter filter);
}
