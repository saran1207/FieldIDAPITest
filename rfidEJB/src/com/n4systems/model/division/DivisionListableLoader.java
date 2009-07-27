package com.n4systems.model.division;

import com.n4systems.model.Division;
import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class DivisionListableLoader extends ListableLoader {
	private Long customerId;

	public DivisionListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(Division.class, filter.prepareFor(Division.class));
		builder.setSelectArgument(new ListableSelect());
		builder.addSimpleWhere("customer.id", customerId);
		builder.addOrder("name");
		return builder;
	}

	public DivisionListableLoader setCustomerId(Long customerId) {
		this.customerId = customerId;
		return this;
	}


}
