package com.n4systems.fieldid.actions;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.EntityStillReferencedException;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.permissions.UserPermissionFilter;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.DivisionOrgPaginatedLoader;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.division.DivisionOrgArchiver;
import com.n4systems.model.user.UserSaver;
import com.n4systems.security.Permissions;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigEntry;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

@Validation
@UserPermissionFilter(userRequiresOneOf={Permissions.ManageEndUsers})
public class DivisionCrud extends AbstractCrud {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(DivisionCrud.class);
	
	private DivisionOrg division;
	private CustomerOrg customer;
	private OrgSaver saver;
	private Pager<DivisionOrg> page;
	private Pager<DivisionOrg> archivedPage;
	
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
		division = getLoaderFactory().createEntityByIdLoader(DivisionOrg.class).setId(uniqueId).load();
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
	public String doArchive() {
		String result = setDivisionActive(false);
		if (SUCCESS.equals(result)) addFlashMessage("Archive successful");
		return result;
	}
	
	@SkipValidation
	public String doUnarchive() {
		String result = setDivisionActive(true);
		if (SUCCESS.equals(result)) addFlashMessage("Unarchive successful");
		return result;
	}

	private String setDivisionActive(boolean active) {
		
		if (division == null) {
			addFlashError("Division not found");
			return ERROR;
		}
		
		// if the address info was created by our loadMemberFields, 
		// we need to nullify it or it'll screw with the delete process
		if (division.getAddressInfo() !=null && division.getAddressInfo().isNew()) {
			division.setAddressInfo(null);
		}
		
		try {
			DivisionOrgArchiver archiver = new DivisionOrgArchiver();
			archiver.archiveDivision(division, saver, new UserSaver(), getLoaderFactory(), getSecurityFilter(), active);
			
		} catch (Exception e) {
			logger.error("Failed updating division", e);
			addFlashErrorText("error.updatingdivision");
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
			if (division.getAddressInfo() != null && division.getAddressInfo().isNew()) {
				division.setAddressInfo(null);
			}
			
			saver.remove(division);
			addFlashMessageText("message.division_deleted");
		} catch (EntityStillReferencedException e) {
			addFlashErrorText("error.divisioninuse");
			return ERROR;
		} catch (Exception e) {
			logger.error("Failed deleteing division", e);
			addFlashErrorText("error.deleting_divsion");
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

	public Pager<DivisionOrg> getPage() {
		if (page == null) {
			DivisionOrgPaginatedLoader loader = getLoaderFactory().createDivisionOrgPaginatedLoader();
			loader.setPage(getCurrentPage()).setPageSize(getConfigContext().getInteger(ConfigEntry.WEB_PAGINATION_PAGE_SIZE));
			loader.setCustomerFilter(customer);
			page = loader.load();
		}
		return page;
	}
	
	public Pager<DivisionOrg> getArchivedPage() {
		if (archivedPage == null) {
			DivisionOrgPaginatedLoader loader = getLoaderFactory().createDivisionOrgPaginatedLoader();
			loader.setPage(getCurrentPage()).setPageSize(getConfigContext().getInteger(ConfigEntry.WEB_PAGINATION_PAGE_SIZE));
			loader.setCustomerFilter(customer);
			loader.setArchivedOnly(true);
			archivedPage = loader.load();
		}
		return archivedPage;
	}
	
	public String getDisplayName() {
		return division.getDisplayName();
	}
	
	public CustomerOrg getCustomer() {
		return customer;
	}
	
	public String getDivisionNotes() {
		return division.getNotes();	
	}
	
	@StringLengthFieldValidator(type = ValidatorType.FIELD, message = "", key = "error.notes_length", maxLength = "1000")
	public void setDivisionNotes(String notes) {
		division.setNotes(notes);
	}
}
