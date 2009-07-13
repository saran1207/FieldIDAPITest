package com.n4systems.model.customer;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Customer;
import com.n4systems.model.api.Listable;
import com.n4systems.persistence.loaders.legacy.ListableLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.ListableSelect;
import com.n4systems.util.persistence.QueryBuilder;

//TODO: Update this class to extend com.n4systems.persistence.loaders.ListableLoader
public class CustomerListableLoader extends ListableLoader {

	public CustomerListableLoader(PersistenceManager pm, SecurityFilter filter) {
		super(pm, filter);
	}

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
