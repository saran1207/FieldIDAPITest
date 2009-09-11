package com.n4systems.fieldid.actions;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import rfid.ejb.entity.UserBean;
import rfid.ejb.session.User;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.api.Listable;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.UserType;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
public class CustomerCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final int CRUD_RESULTS_PER_PAGE = 20;
	private static final int USER_RESULTS_MAX = 100000;

	private final User userManager;
	private final OrgSaver saver;
	
	private CustomerOrg customer;
	private Pager<CustomerOrg> customerPage;
	private String listFilter;
	private Pager<UserBean> userList;
	private List<ListingPair> internalOrgList;
	
	public CustomerCrud(User userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
		this.saver = new OrgSaver();
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
			// if the address info was created by our loadMemberFields, 
			// we need to nullify it or it'll screw with the delete process
			if (customer.getAddressInfo().isNew()) {
				customer.setAddressInfo(null);
			}
			
			saver.remove(customer);
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
			saver.saveOrUpdate(customer);
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
		customer = new CustomerOrg();
		customer.setAddressInfo(new AddressInfo());
		customer.setTenant(getTenant());
		customer.setParent(getSessionUserOwner().getInternalOrg());
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		customer = getLoaderFactory().createFilteredIdLoader(CustomerOrg.class).setId(uniqueId).load();
		if (customer.getAddressInfo() == null) {
			customer.setAddressInfo(new AddressInfo());
		}
	}

	public Pager<CustomerOrg> getPage() {
		if (customerPage == null) {
			CustomerOrgPaginatedLoader loader = getLoaderFactory().createCustomerOrgPaginatedLoader();
			loader.setPage(getCurrentPage()).setPageSize(CRUD_RESULTS_PER_PAGE);
			loader.setNameFilter(listFilter);
			customerPage = loader.load();
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
		return customer.getCode();
	}

	@RequiredStringValidator(type = ValidatorType.FIELD, message = "", key = "error.customeridrequired")
	public void setCustomerId(String customerId) {
		customer.setCode(customerId);
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

	public void setParentOrgId(Long parent) {
		customer.setParent((InternalOrg)getLoaderFactory().createFilteredIdLoader(BaseOrg.class).setId(parent).load());
	}
	
	public Long getParentOrgId() {
		return customer.getParent().getId();
	}
	
	public String getListFilter() {
		return listFilter;
	}

	public void setListFilter(String listFilter) {
		this.listFilter = listFilter;
	}
	
	public List<ListingPair> getParentOrgs() {
		if( internalOrgList == null ) {
			List<Listable<Long>> orgListables = getLoaderFactory().createInternalOrgListableLoader().load();
			internalOrgList = ListHelper.longListableToListingPair(orgListables);
		}
		return internalOrgList;
	}
	
	public List<UserBean> getUserList() {
		if (userList == null) {
			userList = userManager.getUsers(getSecurityFilter(), true, 1, USER_RESULTS_MAX, null, UserType.CUSTOMERS, customer);
		}
		return userList.getList();
	}

	public CustomerOrg getCustomer() {
		return customer;
	}
}
