package com.n4systems.model.customer;

import com.n4systems.model.Customer;
import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.ListableLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

public class CustomerListableLoader extends ListableLoader {

	public CustomerListableLoader(SecurityFilter filter) {
		super(filter);
	}

	@Override
	protected QueryBuilder<Listable<Long>> createBuilder(SecurityFilter filter) {
		QueryBuilder<Listable<Long>> builder = new QueryBuilder<Listable<Long>>(Customer.class, filter.prepareFor(Customer.class));
		builder.setSelectArgument(new ListableSelect());
		builder.addOrder("name");
		return builder;
	}

}
