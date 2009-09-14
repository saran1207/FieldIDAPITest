package com.n4systems.fieldid.actions;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;
import rfid.web.helper.Constants;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.ExtendedFeatureFilter;
import com.n4systems.model.ExtendedFeature;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.security.OwnerFilter;
import com.n4systems.tools.Pager;
import com.n4systems.util.persistence.QueryBuilder;

@ExtendedFeatureFilter(requiredFeature=ExtendedFeature.PartnerCenter)
public class CustomerUserCrud extends AnyCustomerUserCrud {
	private static final long serialVersionUID = 1L;
	
	public CustomerUserCrud( User userManager, PersistenceManager persistenceManager ) {
		super(userManager, persistenceManager);
	}
	
	private void testRequiredEntities(boolean existing) {
		if (user == null || (existing && user.getId() == null)) {
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
			QueryBuilder<UserBean> builder = new QueryBuilder<UserBean>(UserBean.class, getSecurityFilter());
			builder.applyFilter(new OwnerFilter(getOwner()));
			
			builder.addOrder("firstName", "lastName");
			builder.addSimpleWhere("active", true);
			builder.addSimpleWhere("deleted", false);
			
			page = persistenceManager.findAllPaged(builder, getCurrentPage(), Constants.PAGE_SIZE);
		}
		return page;
	}
	
	public Long getCustomerId() {
		return getOwnerId();
	}

	public void setCustomerId(Long customerId) {
		setOwnerId(customerId);
	}
	
	public CustomerOrg getCustomer() {
		return (CustomerOrg)getOwner();
	}
}
