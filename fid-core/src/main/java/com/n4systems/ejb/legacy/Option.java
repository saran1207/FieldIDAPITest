package com.n4systems.ejb.legacy;

import com.n4systems.model.TagOption;
import com.n4systems.model.TagOption.OptionKey;
import com.n4systems.model.security.SecurityFilter;

import java.util.List;

public interface Option {
	
	//TagOption

	public List<TagOption> findTagOptions(SecurityFilter filter);
	public TagOption findTagOption(Long id, SecurityFilter filter);
	public TagOption findTagOption(OptionKey key, SecurityFilter filter);
}
