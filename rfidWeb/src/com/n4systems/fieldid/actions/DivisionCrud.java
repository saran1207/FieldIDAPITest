package com.n4systems.fieldid.actions;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.jboss.logging.Logger;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Validation
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
public class DivisionCrud extends AbstractCrud implements HasDuplicateValueValidator {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DivisionCrud.class);
	
	private DivisionOrg division;
	private CustomerOrg customer;
	private OrgSaver saver;
	private Pager<DivisionOrg> page;
	
	public DivisionCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
		saver = new OrgSaver();
	}
	
	@Override
	protected void initMemberFields() {
		division = new DivisionOrg();	
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		division = getLoaderFactory().createFilteredIdLoader(DivisionOrg.class).setId(uniqueId).load();
	}
	
	private void testRequiredEntities(boolean existing) {
		if (customer == null) {
			addActionErrorText("error.no_customer");
			throw new MissingEntityException("customer is required.");
		} else if (division == null || (existing && division.isNew())) {
			addActionErrorText("error.no_division");
			throw new MissingEntityException("division is required.");
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
		return SUCCESS;
	}

	public String doCreate() {
		testRequiredEntities(false);
		try {
			division.setParent(customer);
			division.setTenant(getTenant());
			saver.save(division);
			addFlashMessageText("message.division_saved");
		} catch (Exception e) {
			logger.error("Failed creating division", e);
			addActionErrorText("error.saving_divsion");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		try {
			saver.update(division);
			addFlashMessageText("message.division_saved");
		} catch (Exception e) {
			logger.error("Failed updating division", e);
			addActionErrorText("error.saving_divsion");
			return ERROR;
		}
		return SUCCESS;
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		
		try {
			// if the address info is new, 
			// we need to nullify it or it'll screw with the delete process
			if (division.getAddressInfo().isNew()) {
				division.setAddressInfo(null);
			}
			
			saver.remove(division);
			addFlashMessageText("message.division_deleted");
		} catch (EntityStillReferencedException e) {
			addFlashError(getText("error.divisioninuse"));
			return ERROR;
		} catch (Exception e) {
			logger.error("Failed deleteing division", e);
			addFlashError(getText("error.saving_divsion"));
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public Long getCustomerId() {
		return (customer != null) ? customer.getId() : null;
	}

	public void setCustomerId(Long customerId) {
		if (customerId == null) {
			customer = null; 
		} else if(customer == null || !customer.getId().equals(customerId)) {
			customer = getLoaderFactory().createFilteredIdLoader(CustomerOrg.class).setId(customerId).load();
		}
	}

	public AddressInfo getAddressInfo() {
		return division.getAddressInfo();
	}

	public void setAddressInfo(AddressInfo addressInfo) {
		division.setAddressInfo(addressInfo);
	}
	
	public Contact getContact() {
		return division.getContact();
	}

	public void setContact(Contact contact) {
		division.setContact(contact);
	}
	
	public String getDivisionID() {
		return division.getCode();
	}

	@RequiredStringValidator(message="", key="error.division_id_required")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.division_id_used")
	public void setDivisionID(String divisionID) {
		division.setCode(divisionID);
	}
	
	public String getName() {
		return division.getName();
	}

	@RequiredStringValidator(message="", key="error.namerequired")
	public void setName(String name) {
		division.setName(name);
	}
	
	public boolean duplicateValueExists(String formValue) {
		return getLoaderFactory().createExternalOrgCodeExistsLoader(DivisionOrg.class).setParentOrg(customer).setCode(formValue).setFilterOutId(uniqueID).load();
	}

	public Pager<DivisionOrg> getPage() {
		if (page == null) {
			DivisionOrgPaginatedLoader loader = getLoaderFactory().createDivisionOrgPaginatedLoader();
			loader.setPage(getCurrentPage()).setPageSize(ConfigContext.getCurrentContext().getInteger(ConfigEntry.WEB_PAGINATION_PAGE_SIZE));
			loader.setCustomerFilter(customer);
			page = loader.load();
		}
		return page;
	}
	
	public String getDisplayName() {
		return division.getDisplayName();
	}
	
	public CustomerOrg getCustomer() {
		return customer;
	}
}
