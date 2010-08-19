package com.n4systems.fieldid.actions.customers;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.jboss.logging.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.api.Listable;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.CustomerOrgPaginatedLoader;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.customer.CustomerOrgListLoader;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.user.User;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ListHelper;
import com.n4systems.util.ListingPair;
import com.n4systems.util.UserType;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
public class CustomerCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static final int CRUD_RESULTS_PER_PAGE = 20;
	private static final int USER_RESULTS_MAX = 100000;
	private static Logger logger = Logger.getLogger(CustomerCrud.class);
	
	private final UserManager userManager;
	private final OrgSaver saver;
	
	private boolean archivedOnly;
	
	private CustomerOrg customer;
	private Pager<CustomerOrg> customerPage;
	private String listFilter;
	private Pager<User> userList;
	private List<ListingPair> internalOrgList;
	
	public CustomerCrud(UserManager userManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.userManager = userManager;
		this.saver = new OrgSaver();
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
		customer = getLoaderFactory().createEntityByIdLoader(CustomerOrg.class).setId(uniqueId).load();
		if (customer.getAddressInfo() == null) {
			customer.setAddressInfo(new AddressInfo());
		}
		if (customer.getContact() == null) {
			customer.setContact(new Contact());
		}
	}

	@SkipValidation
	public String doList() {
		archivedOnly = false;
		setPageType("customer", "list");
		return SUCCESS;
	}

	@SkipValidation
	public String doListArchived() {
		archivedOnly = true;
		setPageType("customer", "list_archived");
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
	public String doArchive() {
		String result = setCustomerActive(false);
		addFlashMessage("Customer archived successfully");
		return result;
	}
	
	@SkipValidation
	public String doUnarchive() {
		String result = setCustomerActive(true);
		addFlashMessage("Customer unarchived successfully");
		return result;
	}

	private String setCustomerActive(boolean active) {
		EntityState newState = active ? EntityState.ACTIVE : EntityState.ARCHIVED;
		
		if (!active) {
			List<User> usersList = getUserList();
			for (User user : usersList) {
				user.archiveUser();
				userManager.updateUser(user);
			}
		}

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
			
			customer.setState(newState);
			
			DivisionOrgByCustomerListLoader divisionsLoader = getLoaderFactory().createDivisionOrgByCustomerListLoader();
			divisionsLoader.setCustomer(customer);
			List<DivisionOrg> divisions = divisionsLoader.load();
			for (DivisionOrg division : divisions) {
				division.setState(newState);
				saver.update(division);
			}
			
			saver.update(customer);
			
		} catch (Exception e) {
			logger.error("Failed updating customer", e);
			addFlashErrorText("error.updatingcustomer");
			return ERROR;
		}
		
		return SUCCESS;
	}

	public String doSave() {
		try {
			if (customer.getTenant() == null) {
				customer.setTenant(getTenant());
			}
			saver.saveOrUpdate(customer);
			addFlashMessage(getText("message.saved"));
			uniqueID = customer.getId();
		} catch (Exception e) {
			addActionError(getText("error.savingcustomer"));
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doShowImportExport() {
		return SUCCESS;
	}
	
	@SkipValidation
	public String doExport() {
		try {
			getDownloadCoordinator().generateCustomerExport(getText("label.export_file.customer"), getDownloadLinkUrl(), createCustomerOrgListLoader(), getSecurityFilter());
		} catch (RuntimeException e) {
			logger.error("Unable to execute customer export", e);
			addFlashMessage(getText("error.export_failed.customer"));
			return ERROR;
		}
		return SUCCESS;
	}
	
	protected CustomerOrgListLoader createCustomerOrgListLoader() {
		return getLoaderFactory().createCustomerOrgListLoader().withoutLinkedOrgs();
	}
	
	public Pager<CustomerOrg> getPage() {
		if (customerPage == null) {
			CustomerOrgPaginatedLoader loader = getLoaderFactory().createCustomerOrgPaginatedLoader();
			loader.setPage(getCurrentPage()).setPageSize(CRUD_RESULTS_PER_PAGE);
			loader.setNameFilter(listFilter);
			loader.setArchivedOnly(archivedOnly);
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

	@EmailValidator(type = ValidatorType.FIELD, message = "", key = "error.emailformat")
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
	
	public List<User> getUserList() {
		if (userList == null) {
			userList = userManager.getUsers(getSecurityFilter(), true, 1, USER_RESULTS_MAX, null, UserType.CUSTOMERS, customer);
		}
		return userList.getList();
	}

	public CustomerOrg getCustomer() {
		return customer;
	}
	
	public String getFilterAction() {
		if (archivedOnly) {
			return "archivedCustomerList";
		} else {
			return "customerList";
		}
	}

}
