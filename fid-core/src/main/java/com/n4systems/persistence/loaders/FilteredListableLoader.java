package com.n4systems.persistence.loaders;

import com.n4systems.model.api.Listable;
import com.n4systems.model.api.NamedEntity;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class FilteredListableLoader extends ListableLoader {
	private final Class<? extends NamedEntity> clazz;
	
	public FilteredListableLoader(SecurityFilter filter, Class<? extends NamedEntity> clazz) {
		super(filter);
		this.clazz = clazz;
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(clazz, filter);
		builder.setSelectArgument(new ListableSelect());
		return builder;
	}

}
