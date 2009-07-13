package com.n4systems.model.parents;

import com.n4systems.model.Customer;
import com.n4systems.model.Division;

public interface EntityWithCustomerDivision {
	public void setCustomer(Customer customer);
	public Customer getCustomer();
	public void setDivision(Division division);
	public Division getDivision();
	
}
