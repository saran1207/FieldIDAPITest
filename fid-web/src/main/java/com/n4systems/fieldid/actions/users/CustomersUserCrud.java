package com.n4systems.fieldid.actions.users;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserPaginatedLoader;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.opensymphony.xwork2.validator.annotations.FieldExpressionValidator;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.ReadOnlyUser)
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
public class CustomersUserCrud extends ReadOnlyUserCrud {
	private static final long serialVersionUID = 1L;
	
	private CustomerOrg customer;
	
	public CustomersUserCrud( UserManager userManager, PersistenceManager persistenceManager ) {
		super( userManager, persistenceManager);
	}
	
	
	@Override
	protected void testRequiredEntities(boolean existing) {
		super.testRequiredEntities(existing);
		
		if (customer == null) { 
			addActionErrorText("error.no_customer");
			throw new MissingEntityException("we must have a customer defined.");
		}
	}


	@Override
	@SkipValidation
	public String doAdd() {
		String result = super.doAdd();
		defaultOwnerToCustomerBeingManaged();
		return result;
	}


	private void defaultOwnerToCustomerBeingManaged() {
		setOwnerId(getCustomerId());
	}

	
	public Pager<User> getPage() {
		if( page == null ) {
		
			page = new UserPaginatedLoader(getSecurityFilter())
							.withCustomer(customer)
							.setPage(getCurrentPage().intValue())
							.setPageSize(Constants.PAGE_SIZE)
							.load();

		}
		return page;
	}
	
	public Long getCustomerId() {
		return customer != null ? customer.getId() : null;
	}
	
	public CustomerOrg getCustomer() {
		return customer;
	}
	
	public void setCustomerId(Long customerId) {
		if (customerId == null) {
			customer = null;
		} else if (customer == null || !customer.getId().equals(customerId)) {
			customer = getLoaderFactory().createFilteredIdLoader(CustomerOrg.class).setId(customerId).load();
		}
	}
	
	@Override
	@FieldExpressionValidator(message="", key="error.owner_must_be_under_this_customer", expression="owner.customerOrg.id == customerId")
	public BaseOrg getOwner() {
		return super.getOwner();
	}

}
