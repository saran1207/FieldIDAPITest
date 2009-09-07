package com.n4systems.ejb;

import javax.ejb.Local;

@Local
public interface CustomerManager {
	
//	/**
//	 * Finds a Customer by name for a Tenant.  The match is case-insensitive.
//	 * @param name		Name of the Customer
//	 * @param tenantId	Id of the Tenant
//	 * @return			A Customer or null if no Customer by that name could be found
//	 * @throws NullPointerException If name is null
//	 */
//	public Customer findCustomer(String name, Long tenantId, LegacySecurityFilter filter);
//	
//	
//	/**
//	 * Finds a Division by name for a Customer.  The match is case-insensitive.
//	 * @param name			Name of the Division
//	 * @param customerId	Id of the customer
//	 * @param filter		A SecurityFilter
//	 * @return				A Division or null
//	 * @throws NullPointerException If name is null
//	 */
//	public Division findDivision(String name, Long customerId, LegacySecurityFilter filter);
//	
//	/**
//	 * Finds a Division by name for a Customer.  The match is case-insensitive.
//	 * @param name			Name of the Division
//	 * @param customer		The Customer for this Division
//	 * @param filter		A SecurityFilter
//	 * @return				A Division or null
//	 * @throws NullPointerException If name is null
//	 */
//	public Division findDivision(String name, Customer customer, LegacySecurityFilter filter);
//	
//	/**
//	 * Finds a Customer by id
//	 * @param customerId	Id of the customer
//	 * @param filter		A SecurityFilter
//	 * @return				A Customer or null
//	 */
//	public Customer findCustomer(Long customerId, LegacySecurityFilter filter);
//	
//	/**
//	 * Finds a Division by id
//	 * @param divisionId	Id of the Division
//	 * @param filter		A SecurityFilter
//	 * @return				A Division or null
//	 */
//	public Division findDivision(Long divisionId, LegacySecurityFilter filter);
//	
//	/**
//	 * Finds a Division by id
//	 * @param divisionId	Id of the Division
//	 * @param customerId	Id of the Customer for this Division
//	 * @return				A Division or null
//	 */
//	public Division findDivision(Long divisionId, Long customerId, LegacySecurityFilter filter);
//	
//	
//	/**
//	 * Finds a Division by id
//	 * @param divisionId	Id of the Division
//	 * @param customerId	Customer for this Division
//	 * @return				A Division or null
//	 */
//	public Division findDivision(Long divisionId, Customer customer, LegacySecurityFilter filter);
//	
//	/**
//	 * Attempts to find a Customer first by customerId and then by name.  If none could be found, creates and persists a Customer for the given Tenant, name, and customerId.
//	 * @param name			Name of the Customer
//	 * @param customerId	CustomerId of the Customer
//	 * @param tenantId		Id of the Tenant
//	 * @param filter		A SecurityFilter
//	 * @return				The resolved or created Customer.
//	 * @throws NullPointerException If either name or customerId is null
//	 */
//	public Customer findOrCreateCustomer(String name, String customerId, Long tenantId, LegacySecurityFilter filter);
//	
//	
//	public Customer findCustomerFussySearch(String name, String customerId, Long tenantId, LegacySecurityFilter filter);
//	
//	/**
//	 * Attempts to find a Division by name.  If none could be found, creates and persists a Division on the given Customer
//	 * @param name			Name of the Division
//	 * @param customerId	The Customer to look for this Division on
//	 * @param filter		A SecurityFilter
//	 * @return				The resolved or created Division.
//	 * @throws	CustomerNotFoundException	If no Customer could be found for the given customerId
//	 */
//	public Division findOrCreateDivision(String name, Long customerId, LegacySecurityFilter filter) throws CustomerNotFoundException;
//	
//	/**
//	 * Finds all Customers ordered by name for the given Tenant
//	 * @param tenantId		Id of the tenant
//	 * @param filter		A SecurityFilter
//	 * @return				A list of Customers ordered by name
//	 */
//	public List<Customer> findCustomers(Long tenantId, LegacySecurityFilter filter);
//	
//	/**
//	 * Creates a ListingPair of Customer for a Tenant ordered by name
//	 * @param tenantId		Id of the tenant
//	 * @param filter		A SecurityFilter
//	 * @return				A list of Customers ordered by name
//	 */
//	public List<ListingPair> findCustomersLP(Long tenantId, LegacySecurityFilter filter);
//	
//	/**
//	 * Finds all Customers ordered by name for the given Tenant with a modified date greater then or equal to startingModifiedDate
//	 * @param tenantId				Id of the tenant
//	 * @param startingModifiedDate	The starting modified date
//	 * @param filter				A SecurityFilter
//	 * @return						A list of Customers ordered by name
//	 */
//	public List<Customer> findCustomers(Long tenantId, Date startingModifiedDate, LegacySecurityFilter filter);
//	
//	/**
//	 * Creates a list of Divisions for a Customer ordered by {@link Division#getName() Division.name}
//	 * @param customerId	The id of the Customer.
//	 * @param filter		A SecurityFilter
//	 * @return				A list of Divisions.
//	 */
//	public List<Division> findDivisions(Long customerId, LegacySecurityFilter filter);
//	
//	/**
//	 * Creates a ListingPair of Divisions for a Customer ordered by {@link Division#getName() Division.name}
//	 * @param customerId	The id of the Customer.
//	 * @param filter		A SecurityFilter
//	 * @return				A list of Divisions.
//	 */
//	public List<ListingPair> findDivisionsLP(Long customerId, LegacySecurityFilter filter);
//	
//	/**
//	 * Creates a list of Divisions for a Customer ordered by {@link Division#getName() Division.name}
//	 * @see 			#getDivisions(Long)
//	 * @param customer	A Customer.
//	 * @param filter	A SecurityFilter
//	 * @return			A list of Divisions.
//	 */
//	public List<Division> findDivisions(Customer customer, LegacySecurityFilter filter);
//	
//	/**
//	 * Constructs a Customer Pager, filtering Customers by name.
//	 * @param tenantId		Id of tenant
//	 * @param nameFilter	A Customer name filter, case insensitive, and uses a right-hand wildcard
//	 * @param page			The current page
//	 * @param pageSize		The size of each page
//	 * @param filter		A SecurityFilter
//	 * @return				The Customer pager object
//	 * @throws InvalidQueryException	One of the constructed queries was invalid.
//	 */
//	public Pager<Customer> findCustomers(Long tenantId, String nameFilter, int page, int pageSize, LegacySecurityFilter filter) throws InvalidQueryException;
//	
//	/**
//	 * Saves or Updates a Customer
//	 * @param customer		A Customer
//	 * @return				The updated Customer
//	 */
//	public Customer saveCustomer(Customer customer);
//	
//	/**
//	 * Saves or Updates a Customer
//	 * @param customer		A Customer
//	 * @param modifiedBy	User to set as the modified by user
//	 * @return				The updated Customer
//	 */
//	public Customer saveCustomer(Customer customer, UserBean modifiedBy);
//	
//	/**
//	 * Saves or Updates a Division
//	 * @param customer		A Division
//	 * @return				The updated Division
//	 */
//	public Division saveDivision(Division division);
//	
//	/**
//	 * Saves or Updates a Division
//	 * @param customer		A Division
//	 * @param modifiedBy	User to set as the modified by user
//	 * @return				The updated Division
//	 */
//	public Division saveDivision(Division division, UserBean modifiedBy);
//	
//	/**
//	 * Permanently removes a Customer and all its Divisions
//	 * @param customerId	id of the customer to be removed
//	 * @param filter		A SecurityFilter
//	 * @throws EntityStillReferencedException	If the Customer is still referenced by some other entity
//	 */
//	public void deleteCustomer(Long customerId, LegacySecurityFilter filter) throws EntityStillReferencedException;
//	
//	/**
//	 * Permanently removes a Customer and all its Divisions
//	 * @param customer	The customer to be removed
//	 * @throws EntityStillReferencedException	If the Customer is still referenced by some other entity
//	 */
//	public void deleteCustomer(Customer customer) throws EntityStillReferencedException;
//	
//	/**
//	 * Permanently removes a Division
//	 * @param divisionId	If of the division to be removed
//	 * @param filter		A SecurityFilter
//	 * @throws EntityStillReferencedException	If the Division is still referenced by some other entity
//	 */
//	public void deleteDivision(Long divisionId, LegacySecurityFilter filter) throws EntityStillReferencedException;
//	
//	/**
//	 * Permanently removes a Division
//	 * @param division	The division to be removed
//	 * @throws EntityStillReferencedException	If the Division is still referenced by some other entity
//	 */
//	public void deleteDivision(Division division) throws EntityStillReferencedException;
}
