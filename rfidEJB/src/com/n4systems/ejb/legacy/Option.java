package com.n4systems.ejb.legacy;

import java.util.Collection;
import java.util.List;

import rfid.ejb.entity.FindProductOptionManufactureBean;

import com.n4systems.model.TagOption;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.model.security.SecurityFilter;

public interface Option {
	
	//FindProductOptionManufactureBean
	public Collection<FindProductOptionManufactureBean>	getFindProductOptionsForTenant(Long tenantId);
	
	public Collection<FindProductOptionManufactureBean> getAllFindProductOptionManufacture();
	public FindProductOptionManufactureBean getFindProductOptionManufacture(Long uniqueID);
	public void updateFindProductOptionManufacture(FindProductOptionManufactureBean findProductOptionManufacturer);
	
	
	//FindProductOptionBean
	
	
	public List<TagOption> findTagOptions(SecurityFilter filter);
	public TagOption findTagOption(Long id, SecurityFilter filter);
	public TagOption findTagOption(OptionKey key, SecurityFilter filter);
}
