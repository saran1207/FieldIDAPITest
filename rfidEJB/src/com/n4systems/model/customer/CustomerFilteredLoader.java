package com.n4systems.model.customer;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.model.Customer;
import com.n4systems.persistence.loaders.legacy.SecuredLoader;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;

/**
 * @deprecated Use {@link FilteredIdLoader} instead.
 */
@Deprecated
public class CustomerFilteredLoader extends SecuredLoader<Customer> {
	private Long id;
	
	public CustomerFilteredLoader(SecurityFilter filter) {
	    super(filter);
    }

	public CustomerFilteredLoader(PersistenceManager pm, SecurityFilter filter) {
	    super(pm, filter);
    }
	
	@Override
    protected Customer load(PersistenceManager pm, SecurityFilter filter) {
		QueryBuilder<Customer> builder = new QueryBuilder<Customer>(Customer.class, filter.prepareFor(Customer.class));
		
		builder.addSimpleWhere("id", id);
		
		Customer customer = pm.find(builder);
		
	    return customer;
    }

	public CustomerFilteredLoader setId(Long id) {
		this.id = id;
		return this;
	}
}
