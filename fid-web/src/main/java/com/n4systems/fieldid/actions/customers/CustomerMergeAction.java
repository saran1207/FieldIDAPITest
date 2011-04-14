package com.n4systems.fieldid.actions.customers;

import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import com.n4systems.ejb.PersistenceManager;
import com.n4systems.exceptions.MissingEntityException;
import com.n4systems.fieldid.actions.api.AbstractCrud;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.taskscheduling.TaskExecutor;
import com.n4systems.taskscheduling.task.CustomerMergeTask;

public class CustomerMergeAction extends AbstractCrud {
	
	private CustomerOrg winningCustomer;
	private CustomerOrg losingCustomer;
	
	private String search;
	
	public CustomerMergeAction(PersistenceManager persistenceManager) {
		super(persistenceManager);
	}

	@Override
	protected void initMemberFields() {
		losingCustomer = new CustomerOrg();
	}

	@Override
	protected void loadMemberFields(Long uniqueId) {
		losingCustomer = getLoaderFactory().createEntityByIdLoader(CustomerOrg.class).setId(uniqueId).load();
	}

	private void testRequiredEntities() {
		if (losingCustomer == null || losingCustomer.isNew()) {
			addActionErrorText("error.noasset");
			throw new MissingEntityException();
		}
	}	
	
	@SkipValidation
	public String doShow() {
		testRequiredEntities();
		return SUCCESS;
	}
	
	public String doCreate() {
		testRequiredEntities();
		if (winningCustomer == null) {
			addActionErrorText("error.you_must_choose_a_valid_customer_to_merge_into");
			return INPUT;
		}

		CustomerMergeTask task = new CustomerMergeTask(winningCustomer, losingCustomer, getUser());
		TaskExecutor.getInstance().execute(task);
		
		return SUCCESS;
	}

	public String doList(){
		testRequiredEntities();
		return SUCCESS;
	}
	
	public List<CustomerOrg> getCustomers() {
		List<CustomerOrg> customersList = getLoaderFactory().createCustomerOrgListLoader()
		                                                    .withLinkedOrgs()
		                                                    .withNameFilter(search)
		                                                    .setPostFetchFields("createdBy")
		                                                    .load();
		customersList.remove(losingCustomer);
		return customersList;
	}

	public CustomerOrg getWinningCustomer() {
		return winningCustomer;
	}

	public void setWinningCustomerId(Long winningCustomerId) {
		this.winningCustomer = getLoaderFactory().createEntityByIdLoader(CustomerOrg.class).setId(winningCustomerId).load();
	}
	
	public void setWinningCustomer(CustomerOrg winningCustomer) {
		this.winningCustomer = winningCustomer;
	}

	public CustomerOrg getLosingCustomer() {
		return losingCustomer;
	}

	public void setLosingCustomer(CustomerOrg losingCustomer) {
		this.losingCustomer = losingCustomer;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	public CustomerOrg getCustomer() {
		return losingCustomer;
	}
}
