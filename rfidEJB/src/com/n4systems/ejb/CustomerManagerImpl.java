package com.n4systems.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.apache.log4j.Logger;

import rfid.ejb.entity.UserBean;

import com.n4systems.ejb.interceptor.TimingInterceptor;
import com.n4systems.exceptions.CustomerNotFoundException;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.Tenant;
import com.n4systems.tools.Page;
import com.n4systems.tools.Pager;
import com.n4systems.util.FuzzyResolver;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.SecurityFilter;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereParameter;

@Interceptors({TimingInterceptor.class})
@Stateless
public class CustomerManagerImpl implements CustomerManager {
	private Logger logger = Logger.getLogger(CustomerManager.class);
	
	@EJB
	private PersistenceManager persistenceManager;

	public Customer findCustomer(String name, Long tenantId, SecurityFilter filter) {
		if(name == null) {
			throw new NullPointerException();
		}
		
		if(filter != null) {
			filter.setTargets("tenant.id", "id");
		}
		
		QueryBuilder<Customer> builder = new QueryBuilder<Customer>(Customer.class, filter);
		builder.addSimpleWhere("tenant.id", tenantId);
		builder.addWhere(WhereParameter.Comparator.EQ, "name", "name", name, WhereParameter.IGNORE_CASE);
		
		Customer customer = null;
		try {
			customer = persistenceManager.find(builder);
		}  catch(InvalidQueryException e) {
			logger.error("Failed loading Customer named [" + name + "] for Tenant [" + filter.getTenantId() + "]", e);
		}
		
		return customer;
	}
	
	
	
	public Division findDivision(String name, Long customerId, SecurityFilter filter) {
		if(name == null) {
			throw new NullPointerException();
		}
		
		if(filter != null) {
			filter.setTargets("tenant.id", "customer.id", "id");
		}
		
		QueryBuilder<Division> builder = new QueryBuilder<Division>(Division.class, filter);
		builder.addSimpleWhere("customer.id", customerId);
		builder.addWhere(WhereParameter.Comparator.EQ, "name", "name", name, WhereParameter.IGNORE_CASE);
		
		Division division = null;
		try {
			division = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed loading Division named [" + name + "] for Customer [" + filter.getCustomerId() + "]", e);
		}
		
		return division;
	}
	
	public Division findDivision(String name, Customer customer, SecurityFilter filter) {
		return findDivision(name, customer.getId(), filter);
	}
	
	public Customer findCustomer(Long customerId, SecurityFilter filter) {
		if (customerId == null) {
			return null;
		}
		if(filter != null) {
			filter.setTargets("tenant.id", "id");
		}
		
		QueryBuilder<Customer> builder = new QueryBuilder<Customer>(Customer.class, filter);
		builder.addSimpleWhere("id", customerId);
		
		Customer customer = null;
		try {
			customer = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed loading Customer with id [" + customerId + "]", e);
		}
		
		return customer;
	}
	
	public Division findDivision(Long divisionId, SecurityFilter filter) {
		return findDivision(divisionId, (Long)null, filter);
	}
	
	public Division findDivision(Long divisionId, Customer customer, SecurityFilter filter) {
		return findDivision(divisionId, customer.getId(), filter);
	}
	
	public Division findDivision(Long divisionId, Long customerId, SecurityFilter filter) {
		if(filter != null) {
			filter.setTargets("tenant.id", "customer.id", "id");
		}
		
		QueryBuilder<Division> builder = new QueryBuilder<Division>(Division.class, filter);
		builder.addSimpleWhere("id", divisionId);
		
		if(customerId != null) {
			builder.addSimpleWhere("customer.id", customerId);
		}
		
		Division division = null;
		try {
			division = persistenceManager.find(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed loading Division with id [" + divisionId + "]", e);
		}
		
		return division;
	}
	
	public Customer findOrCreateCustomer(String name, String customerId, Long tenantId, SecurityFilter filter) {
		if(name == null || customerId == null) {
			throw new NullPointerException();
		}
		
		Customer customer = findCustomerFussySearch(name, customerId, tenantId, filter);
		
		if(customer == null) {
			Tenant tenant = persistenceManager.find(Tenant.class, tenantId);
			
			customer = new Customer();
			customer.setTenant(tenant);
			customer.setName(name);
			customer.setCustomerId(customerId);
			
			logger.info("Creating new Customer [" + name + "] for Tenant [" + tenant.getName() +"]");
			persistenceManager.save(customer);
		}
		
		return customer;
	}
	
	
	public Customer findCustomerFussySearch(String name, String customerId, Long tenantId, SecurityFilter filter) {
		Customer customer = null;
		// if the customer name is null or zero length, return null
		if (name == null || name.trim().length() == 0) {
			return null;
		}
		
		if (customerId == null || customerId.trim().length() == 0) {
			return null;
		}
		
		List<Customer> customerList = findCustomers(tenantId, null);
		
		/*
		 * we get the customer in 3 steps.  First we attempt an exact name match on the 
		 * Customer ID (short name).  Failing that, we attempt to resolve against the full customer.
		 * failing that, we'll create one
		 */
		// attempt resolution against the customer id first
		int customerCount = 0;
		for(Customer cust: customerList) {
			if(cust.getCustomerId().equalsIgnoreCase(customerId)) {
				customer = cust;
				customerCount++;
			}
		}
		// if we find more than one customer id that match we will act as if we didn't find it.
		if (customerCount > 1) {
			customer = null;
		}
		
		//if we didn't find a customer, then lets try the full name
		if(customer == null) {
			String fuzzyName = FuzzyResolver.mungString(name);
			for(Customer cust: customerList) {
				if(FuzzyResolver.mungString(cust.getName()).equals(fuzzyName)) {
					customer = cust;
					break;
				}
			}
		}
		return customer;
	}
	
	public Division findOrCreateDivision(String name, Long customerId, SecurityFilter filter) throws CustomerNotFoundException {
		if(name == null || customerId == null) {
			throw new NullPointerException();
		}
		
		Customer customer = findCustomer(customerId, filter);
		
		if(customer == null) {
			throw new CustomerNotFoundException(customerId);
		}
		
		Division division = findDivision(name, customerId, filter);
		
		// if the division is still null, then we'll create a new one
		if(division == null) {
			division = new Division();
			division.setTenant(customer.getTenant());
			division.setCustomer(customer);
			division.setName(name);
		
			logger.info("Creating new Division [" + name + "] for Customer [" + customer.getName() + "], Tenant [" + customer.getTenant().getName() +"]");
			persistenceManager.save(division);
		}
		
		return division;
	}
	
	public List<Customer> findCustomers(Long tenantId, SecurityFilter filter) {		
		return findCustomers(tenantId, null, filter);
	}
	
	public List<ListingPair> findCustomersLP(Long tenantId, SecurityFilter filter) {
		return ListHelper.longListableToListingPair(findCustomers(tenantId, filter));
	}
	
	public List<Customer> findCustomers(Long tenantId, Date startingModifiedDate, SecurityFilter filter) {
		if(filter != null) {
			filter.setTargets("tenant.id", "id");
		}
		
		QueryBuilder<Customer> builder = new QueryBuilder<Customer>(Customer.class, filter);
		builder.addSimpleWhere("tenant.id", tenantId);
		builder.addOrder("name");
		
		// if the start date is null, return all
		if(startingModifiedDate != null) {
			builder.addWhere(WhereParameter.Comparator.GE, "startingModifiedDate", "modified", startingModifiedDate);
		}
		
		List<Customer> customers = new ArrayList<Customer>();
		try {
			customers = persistenceManager.findAll(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed loading Customers for Tenant [" + tenantId + "]", e);
		}
		
		return customers;
	}
	
	public Pager<Customer> findCustomers(Long tenantId, String nameFilter, int page, int pageSize, SecurityFilter filter) throws InvalidQueryException {
		if(filter != null) {
			filter.setTargets("tenant.id", "id");
		}
		
		QueryBuilder<Customer> builder = new QueryBuilder<Customer>(Customer.class, filter);
		builder.addSimpleWhere("tenant.id", tenantId);
		builder.addOrder("name");
		
		if(nameFilter != null) {
			builder.addWhere(WhereParameter.Comparator.LIKE, "nameFilter", "name", nameFilter, WhereParameter.WILDCARD_RIGHT|WhereParameter.IGNORE_CASE);
		}

		return new Page<Customer>(persistenceManager.prepare(builder.setSimpleSelect()), persistenceManager.prepare(builder.setCountSelect()), page, pageSize);
	}
	
	public List<Division> findDivisions(Long customerId, SecurityFilter filter) {
		if(filter != null) {
			filter.setTargets("tenant.id", "customer.id", "id");
		}
		
		// return empty division list on null customer
		if(customerId == null) {
			return new ArrayList<Division>();
		}
		
		QueryBuilder<Division> builder = new QueryBuilder<Division>(Division.class, filter);
		builder.addSimpleWhere("customer.id", customerId);
		builder.addOrder("name");
		
		List<Division> divisions = new ArrayList<Division>();
		try {
			divisions = persistenceManager.findAll(builder);
		} catch(InvalidQueryException e) {
			logger.error("Failed loading Divisions for Customer [" + customerId + "]", e);
		}
		
		return divisions;
	}

	public List<ListingPair> findDivisionsLP(Long customerId, SecurityFilter filter) {
		return ListHelper.longListableToListingPair(findDivisions(customerId, filter));
	}
	
	public List<Division> findDivisions(Customer customer, SecurityFilter filter) {
		return findDivisions(customer.getId(), filter);
	}
	
	public Customer saveCustomer(Customer customer) {
		return saveCustomer(customer, null);
	}
	
	public Customer saveCustomer(Customer customer, UserBean user) {
		Customer newCustomer;
		if(customer.getId() == null) {
			persistenceManager.save(customer, user);
			newCustomer = customer;
		} else {
			newCustomer = persistenceManager.update(customer, user);
		}
		
		return newCustomer;
	}
	
	public Division saveDivision(Division division) {
		return saveDivision(division, null);
	}
	
	public Division saveDivision(Division division, UserBean user) {
		Division newDivision;
		if(division.getId() == null) {
			persistenceManager.save(division, user);
			newDivision = division;
		} else {
			newDivision = persistenceManager.update(division, user);
		}
		
		return newDivision;
	}
	
	public void deleteCustomer(Long customerId, SecurityFilter filter) throws EntityStillReferencedException {
		Customer customer = findCustomer(customerId, filter);
		
		if(customer != null) {
			deleteCustomer(customer);
		}
	}
	
	public void deleteCustomer(Customer customer) throws EntityStillReferencedException {
		for(Division division: findDivisions(customer, null)) {
			deleteDivision(division);
		}
			
		persistenceManager.deleteSafe(customer);
	}
	
	public void deleteDivision(Long divisionId, SecurityFilter filter) throws EntityStillReferencedException {
		Division division = findDivision(divisionId, filter);
		
		if(division != null) {
			deleteDivision(division);
		}
	}
	
	public void deleteDivision(Division division) throws EntityStillReferencedException {
		persistenceManager.deleteSafe(division);
	}
}
