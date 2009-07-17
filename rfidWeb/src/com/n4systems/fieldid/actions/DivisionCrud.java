package com.n4systems.fieldid.actions;

import java.util.Collection;

import net.sf.json.JSONSerializer;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.CustomerManager;
import com.n4systems.ejb.PersistenceManager;
import com.n4systems.fieldid.actions.api.AbstractAction;
import com.n4systems.model.Customer;
import com.n4systems.model.Division;
import com.n4systems.util.ListingPair;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validation;
import com.opensymphony.xwork2.validator.annotations.ValidatorType;

/** 
 * Special CRUD only called through AJAX requests
 * @author Jesse Miller
 *
 */
@Validation
public class DivisionCrud extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private CustomerManager customerManager;
	
	private Long uniqueId = 0L;
	private Long customerId;
	private String name="";
	private String resultMessage="";
	private String function="";
	
	private Collection<ListingPair> divisions;
	
	
	public DivisionCrud(CustomerManager customerManager, PersistenceManager persistenceManager) {
		super(persistenceManager);
		this.customerManager = customerManager;
	}
	
	public String doUpdate() {
		function = "update";
		
		Division division = customerManager.findDivision(uniqueId, customerId, getSecurityFilter());
		
		// only do a duplicate check if the name is changed
		if (!division.getName().equalsIgnoreCase(name)) {
			if (hasDuplicate()) {
				return INPUT;
			}
		}
		
		division.setName(name);
		customerManager.saveDivision(division);
		
		resultMessage = "successfully updated";
		
		return SUCCESS;
	}
	
	private boolean hasDuplicate() {		
		Division division = customerManager.findDivision(name, customerId, getSecurityFilter());
		if (division != null) {
			resultMessage = "cannot use a duplicate name";
			return true;
		}
		
		return false;
	}
	
	public String doAdd() {
		function = "add";
		
		if (hasDuplicate()) {
			return INPUT;
		}
		
		Customer customer = customerManager.findCustomer(customerId, getSecurityFilter());
		
		if(customer == null) {
			return INPUT;
		}
		
		Division division = new Division();
		division.setTenant(customer.getTenant());
		division.setCustomer(customer);
		division.setName(name);
		
		resultMessage = "successfully added";
		
		uniqueId = customerManager.saveDivision(division).getId();
		
		return SUCCESS;		
	}
	
	@SkipValidation
	public String doDelete() {
		function = "delete";
		
		try {
			customerManager.deleteDivision(uniqueId, getSecurityFilter());
		} catch (Exception e) {
			uniqueId = 0L; // this effectively says "not successful" to the javascript
			resultMessage = getText("error.deleting_division");
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	@SkipValidation
	public String doList() {
		if( customerId != null ) {
			try {
				divisions = customerManager.findDivisionsLP(customerId, getSecurityFilter());
				
				return SUCCESS;
			} catch (Exception e) {
				return ERROR;	
			}
		}	
		
		
		return ERROR;
	}
	
	public Long getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(Long uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}
	
	@RequiredStringValidator(type=ValidatorType.FIELD, message = "", key="error.required")
	public void setName(String name) {
		this.name = name;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public String getFunction() {
		return function;
	}

	public JSONSerializer getJson() {
		return new JSONSerializer();
	}

	public Collection<ListingPair> getDivisions() {
		return divisions;
	}

	public void setDivisions(Collection<ListingPair> divisions) {
		this.divisions = divisions;
	}

	
}
