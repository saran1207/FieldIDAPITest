package com.n4systems.testutils;

import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.orgs.BaseOrgParentFilterListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.persistence.loaders.FilteredListableLoader;
import com.n4systems.persistence.loaders.LoaderFactory;

public class TestDoubleLoaderFactory extends LoaderFactory {

	private FilteredListableLoader filteredListableLoader;
	private BaseOrgParentFilterListLoader baseOrgParentFilterListLoader;
	
	public TestDoubleLoaderFactory(SecurityFilter filter) {
		super(filter);
	}

	@Override
	public FilteredListableLoader createFilteredListableLoader(Class<? extends NamedEntity> clazz) {
		return filteredListableLoader;
	}

	public TestDoubleLoaderFactory setFilteredListableLoader(FilteredListableLoader filteredListableLoader) {
		this.filteredListableLoader = filteredListableLoader;
		return this;
	}

	@Override
	public BaseOrgParentFilterListLoader createBaseParentFilterLoader() {
		return baseOrgParentFilterListLoader;
	}

	public void setBaseOrgParentFilterListLoader(BaseOrgParentFilterListLoader baseOrgParentFilterListLoader) {
		this.baseOrgParentFilterListLoader = baseOrgParentFilterListLoader;
	}

	
	
	
	
	
	

}
