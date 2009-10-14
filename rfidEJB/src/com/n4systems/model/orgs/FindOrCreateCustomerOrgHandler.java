package com.n4systems.model.orgs;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.persistence.loaders.DuplicateExtenalOrgException;
import com.n4systems.persistence.loaders.ListLoader;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.StringUtils;

public class FindOrCreateCustomerOrgHandler {
	private ListLoader<CustomerOrg> loader;
	private Saver<? super CustomerOrg> saver;
	private PrimaryOrg primaryOrg;
	private boolean findOnly = false;
	private boolean customerCreated;
	
	public FindOrCreateCustomerOrgHandler(ListLoader<CustomerOrg> loader, Saver<? super CustomerOrg> saver) {
		this.loader = loader;
		this.saver = saver;
	}
	
	public CustomerOrg findOnly(PrimaryOrg primaryOrg, String name) throws DuplicateExtenalOrgException {
		boolean oldFindOnly = findOnly;
		findOnly = true;
		
		CustomerOrg customer = findOrCreate(primaryOrg, name);
		
		findOnly = oldFindOnly;
		return customer;
	}
	
	public CustomerOrg findOnly(PrimaryOrg primaryOrg, String name, String code) throws DuplicateExtenalOrgException {
		boolean oldFindOnly = findOnly;
		findOnly = true;
		
		CustomerOrg customer = findOrCreate(primaryOrg, name, code);
		
		findOnly = oldFindOnly;
		return customer;
	}
	
	public CustomerOrg findOrCreate(PrimaryOrg primaryOrg, String name) throws DuplicateExtenalOrgException {
		return findOrCreate(primaryOrg, name, FuzzyResolver.mungString(name));
	}
	
	public CustomerOrg findOrCreate(PrimaryOrg primaryOrg, String name, String code) throws DuplicateExtenalOrgException {
		this.primaryOrg = primaryOrg;
		customerCreated = false;
		
		List<CustomerOrg> customers = loadCustomerList();
		
		/*
		 * we get the customer in 3 steps.  First we attempt an exact name match on the 
		 * customer code.  Failing that, we attempt to resolve against the full customer name.
		 * failing that, we'll create one
		 */
		List<CustomerOrg> matchedCustomers = new ArrayList<CustomerOrg>();
		for(CustomerOrg cust: customers) {
			if(cust.getCode() != null && cust.getCode().equalsIgnoreCase(code)) {
				matchedCustomers.add(cust);
			}
		}
		
		// If we find more than one by code, we act like we didn't find it 
		if (matchedCustomers.size() > 1) {
			matchedCustomers = new ArrayList<CustomerOrg>();
		}
		
		//if we didn't find a customer, then lets try the full name
		if(matchedCustomers.isEmpty()) {
			String fuzzyName = FuzzyResolver.mungString(name);
			for(CustomerOrg cust: customers) {
				if(FuzzyResolver.mungString(cust.getName()).equals(fuzzyName)) {
					matchedCustomers.add(cust);
				}
			}
		}
		
		if (matchedCustomers.size() > 1) {
			// if we found more then one customer here, we need to throw an exception
			throw new DuplicateExtenalOrgException(matchedCustomers);
		} else if (matchedCustomers.isEmpty() && !findOnly) {
			// if we still haven't found a customer, we need to create one but first we need to make sure our customer code is unique
			matchedCustomers.add(createCustomer(primaryOrg, name, findUniqueCustomerCode(code, customers)));
		}
		
		// return the first customer in the list if it's not empty (by this time there should only be 0 or 1 in the list)
		return (!matchedCustomers.isEmpty()) ? matchedCustomers.get(0) : null;
	}

	private CustomerOrg createCustomer(PrimaryOrg primaryOrg, String name, String code) { 
		CustomerOrg customer = new CustomerOrg();
		
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(code)) {
			throw new InvalidExternalOrgException(String.format("Name/Code cannot be empty. Name [%s], Code [%s]", name, code));
		}
		
		customer.setTenant(primaryOrg.getTenant());
		customer.setParent(primaryOrg);
		customer.setCode(code);
		customer.setName(name);
		
		persistCustomer(customer);
		customerCreated = true;
		
		return customer;
	}

	private String findUniqueCustomerCode(String preferedId, List<CustomerOrg> customers) {
		List<String> idList = new ArrayList<String>();
		for(CustomerOrg customer: customers) {
			idList.add(customer.getCode());
		}
		
		String newId = preferedId;
		
		int idx = 1;
		while(idList.contains(newId)) {
			newId = preferedId + idx;
			idx++;
		}
		
		return newId;
	}
	
	// XXX - This is really only required by the LegacyFindOrCreateCustomerOrg and should probably be removed when it is
	protected PrimaryOrg getPrimaryOrg() {
		return primaryOrg;
	}
	
	protected List<CustomerOrg> loadCustomerList() {
		List<CustomerOrg> customers = loader.load();
		return customers;
	}
	
	protected void persistCustomer(CustomerOrg customer) {
		saver.save(customer);
	}
	
	public void setFindOnly(boolean findOnly) {
		this.findOnly = findOnly;
	}
	
	public boolean customerWasCreated() {
		return customerCreated;
	}
}
