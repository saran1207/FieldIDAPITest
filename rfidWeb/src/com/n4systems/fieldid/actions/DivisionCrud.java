package com.n4systems.fieldid.actions;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.fieldid.actions.helpers.MissingEntityException;
import com.n4systems.fieldid.validators.HasDuplicateValueValidator;
import com.n4systems.model.AddressInfo;
import com.n4systems.model.Contact;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.model.api.Listable;
import com.n4systems.tools.Pager;
import com.n4systems.util.ConfigContext;
import com.n4systems.util.ConfigEntry;
import com.n4systems.util.persistence.QueryBuilder;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;

@Validation
public class DivisionCrud extends AbstractCrud implements HasDuplicateValueValidator {

	private static final long serialVersionUID = 1L;

	
	private Division division;
	private Customer customer;
	
	private List<Listable<Long>> divisions;
	private Pager<Division> page;
	
	
	public DivisionCrud(PersistenceManager persistenceManager) {
		super(persistenceManager);
		
	}
	
	
	@Override
	protected void initMemberFields() {
		division = new Division();
		
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		division = getLoaderFactory().createDivisionFilteredLoader().setId(uniqueId).load();
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
			createDivision();
			addFlashMessageText("message.division_saved");
		} catch (Exception e) {
			addActionErrorText("error.saving_divsion");
			return ERROR;
		}
		return SUCCESS;
	}


	private void createDivision() {
		division.setCustomer(customer);
		division.setTenant(getTenant());
		persistenceManager.save(division, fetchCurrentUser());
	}

	@SkipValidation
	public String doEdit() {
		testRequiredEntities(true);
		return SUCCESS;
	}

	public String doUpdate() {
		testRequiredEntities(true);
		try {
			updateDivision();
			addFlashMessageText("message.division_saved");
		} catch (Exception e) {
			addActionErrorText("error.saving_divsion");
			return ERROR;
		}
		return SUCCESS;
	}


	private void updateDivision() {
		persistenceManager.update(division, fetchCurrentUser());
	}

	@SkipValidation
	public String doDelete() {
		testRequiredEntities(true);
		
		try {
			deleteDivision();
			addFlashMessageText("message.division_deleted");
		} catch (Exception e) {
			addActionErrorText("error.saving_divsion");
			return ERROR;
		}
		return SUCCESS;
	}

	private void deleteDivision() {
		persistenceManager.delete(division);
	}
	
	public Long getCustomerId() {
		return (customer != null) ? customer.getId() : null;
	}

	public void setCustomerId(Long customerId) {
		if (customerId == null) {
			customer = null; 
		} else if(customer == null || !customer.getId().equals(customerId)) {
			customer = getLoaderFactory().createCustomerFilteredLoader().setId(customerId).load();
		}
		
	}

	
	public List<Listable<Long>> getDivisions() {
		if (divisions == null) {
			divisions = getLoaderFactory().createDivisionListableLoader().setCustomerId(customer.getId()).load();
		}
		return divisions;
	}


	public AddressInfo getAddressInfo() {
		return division.getAddressInfo();
	}


	public Contact getContact() {
		return division.getContact();
	}


	public String getDisplayName() {
		return division.getDisplayName();
	}


	public String getDivisionID() {
		return division.getDivisionID();
	}


	public String getName() {
		return division.getName();
	}


	public void setAddressInfo(AddressInfo addressInfo) {
		division.setAddressInfo(addressInfo);
	}


	public void setContact(Contact contact) {
		division.setContact(contact);
	}

	
	@RequiredStringValidator(message="", key="error.division_id_required")
	@CustomValidator(type = "uniqueValue", message = "", key = "error.division_id_used")
	public void setDivisionID(String divisionID) {
		division.setDivisionID(divisionID);
	}

	@RequiredStringValidator(message="", key="error.namerequired")
	public void setName(String name) {
		division.setName(name);
	}


	public boolean duplicateValueExists(String formValue) {
		return getLoaderFactory().createDivisionUniqueNameUsedLoader().setUniqueName(formValue).exceptForId(uniqueID).load();
	}


	public Pager<Division> getPage() {
		if (page == null) {
			QueryBuilder<Division> query = new QueryBuilder<Division>(Division.class, getSecurityFilter().prepareFor(Division.class));
			query.addSimpleWhere("customer", customer).addOrder("name");
			
			page = persistenceManager.findAllPaged(query, getCurrentPage(), ConfigContext.getCurrentContext().getInteger(ConfigEntry.WEB_PAGINATION_PAGE_SIZE));
		}
		return page;
	}
	
	
	public Customer getCustomer() {
		return customer;
	}
}
