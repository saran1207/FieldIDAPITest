package com.n4systems.fieldid.actions;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.InvalidQueryException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Customer;
import com.n4systems.tools.Pager;
import com.n4systems.util.UserType;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
public class CustomerCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(CustomerCrud.class);

	private CustomerManager customerManager;
	private User userManager;
	private Customer customer;

	private Pager<Customer> customerPage;
	private String listFilter;

	private Pager<UserBean> userList;

	private static final int CRUD_RESULTS_PER_PAGE = 20;
	private static final int USER_RESULTS_MAX = 100000;

	public CustomerCrud(CustomerManager customerManager, User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.customerManager = customerManager;
		this.userManager = userManager;
	}

	@SkipValidation
	public String doList() {
		return SUCCESS;
	}

	@SkipValidation
	public String doLoadEdit() {
		if (customer == null) {
			addActionError("Customer not found");
			return ERROR;
		}

		return INPUT;
	}

	@SkipValidation
	public String doShow() {
		if (customer == null || customer.getId() == null) {
			addActionError("Customer not found");
			return ERROR;
		}

		return SUCCESS;
	}

	@SkipValidation
	public String doRemove() {
		if (customer == null) {
			addFlashError("Customer not found");
			return ERROR;
		}

		try {
			customerManager.deleteCustomer(customer.getId(), getSecurityFilter());
		} catch (EntityStillReferencedException e) {
			addFlashError(getText("error.customerinuse"));
			return ERROR;
		}

		addFlashMessage("Customer deleted successfully");
		return SUCCESS;
	}

	public String doSave() {

		try {
			if (customer.getTenant() == null) {
				customer.setTenant(getTenant());
			}
			customerManager.saveCustomer(customer);
		} catch (Exception e) {
			addActionError(getText("error.savingcustomer"));
			return ERROR;
		}
		uniqueID = customer.getId();
		addFlashMessage(getText("message.saved"));
		return SUCCESS;
	}

	@Override
	protected void initMemberFields() {
		customer = new Customer();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		customer = customerManager.findCustomer(uniqueId, getSecurityFilter());
		if (customer.getAddressInfo() == null) {
			customer.setAddressInfo(new AddressInfo());
		}
	}

	public Pager<Customer> getPage() {
		if (customerPage == null) {
			try {
				customerPage = customerManager.findCustomers(getTenantId(), getListFilter(), getCurrentPage(),
						CRUD_RESULTS_PER_PAGE, getSecurityFilter());
			} catch (InvalidQueryException e) {
				logger.error("Unable to load Customer Pager", e);
			}
		}
		return customerPage;
	}

	public String getCustomerName() {
		return customer.getName();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.customernamerequired")
	public void setCustomerName(String customerName) {
		customer.setName(customerName);
	}

	public String getCustomerId() {
		return customer.getCustomerId();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.customeridrequired")
	public void setCustomerId(String customerId) {
		customer.setCustomerId(customerId);
	}

	public String getAccountManagerEmail() {
		return customer.getContact().getEmail();
	}

	@EmailValidator(type = ValidatorType.FIELD, message = "", key = "error.validemail")
	public void setAccountManagerEmail(String accountManagerEmail) {
		customer.getContact().setEmail(accountManagerEmail);
	}

	public String getContactName() {
		return customer.getContact().getName();
	}

	public void setContactName(String contactName) {
		customer.getContact().setName(contactName);
	}

	public AddressInfo getAddressInfo() {
		return customer.getAddressInfo();
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		customer.setAddressInfo(addressInfo);
	}

	public String getListFilter() {
		return listFilter;
	}

	public void setListFilter(String listFilter) {
		this.listFilter = listFilter;
	}
	
	public List<UserBean> getUserList() {
		if (userList == null) {
			userList = userManager.getUsers(getSecurityFilter(), true, 1, USER_RESULTS_MAX, null, UserType.CUSTOMERS,
					customer);
		}

		return userList.getList();
	}

	public Customer getCustomer() {
		return customer;
		
	}

}
