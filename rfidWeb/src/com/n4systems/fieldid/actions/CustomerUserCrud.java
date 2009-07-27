package com.n4systems.fieldid.actions;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;
import rfid.web.helper.Constants;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.Customer;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class CustomerUserCrud extends AnyCustomerUserCrud {
	private static final long serialVersionUID = 1L;
	
	private Customer customer;
	
	public CustomerUserCrud( User userManager, CustomerManager customerManager, PersistenceManager persistenceManager ) {
		super(userManager, customerManager, persistenceManager);
	}
	
	
	private void testRequiredEntities(boolean existing) {
		if (customer == null) {
			addActionErrorText("error.no_customer");
			throw new MissingEntityException("customer is required.");
		} else if (user == null || (existing && user.getId() == null)) {
			addActionErrorText("error.no_user");
			throw new MissingEntityException("user is required.");
		}
	}

	@SkipValidation
	public String doList() {
		testRequiredEntities(false);
		return SUCCESS;
	}

	@SkipValidation
	public String doAdd() {
		testRequiredEntities(false);
		super.doEdit();
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		String result = doSave();
		if (result == "saved") {
			return SUCCESS;
		}
		return result;
	}

	
	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		super.doEdit();
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		String result = doSave();
		if (result == "saved") {
			return SUCCESS;
		}
		return result;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		doRemove();
		return SUCCESS;
	}
	
	
	public Pager<UserBean> getPage() {
		if( page == null ) {
			QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class, getSecurityFilter().setTargets("tenant.id"));
			
			builder.addOrder("firstName", "lastName");
			builder.addSimpleWhere("active", true);
			builder.addSimpleWhere("deleted", false);
			builder.addSimpleWhere("r_EndUser", customer.getId());
			
			
			page = persistenceManager.findAllPaged(builder, getCurrentPage(), Constants.PAGE_SIZE);
		}
		return page;
	}
	
	
	public Long getCustomerId() {
		return (customer != null) ? customer.getId() : null;
	}

	public void setCustomerId(Long customerId) {
		if (customerId == null) {
			customer = null; 
		} else if(customer == null || !customer.getId().equals(customerId)) {
			customer = getLoaderFactory().createFilteredIdLoader(Customer.class).setId(customerId).load();
		}
		super.setCustomerId(customerId);
	}
	
	public Customer getCustomer() {
		return customer;
	}
}
