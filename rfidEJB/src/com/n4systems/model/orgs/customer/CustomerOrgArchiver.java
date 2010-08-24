package com.n4systems.model.orgs.customer;

import java.util.List;

import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.LoaderFactory;
import com.n4systems.util.UserType;

public class CustomerOrgArchiver {

	private static final int USER_RESULTS_MAX = 100000;

	public void archiveCustomer(CustomerOrg customer, UserManager userManager, OrgSaver orgSaver, 
			UserSaver userSaver, LoaderFactory loaderFactory,  SecurityFilter securityFilter, boolean active) {

		Transaction transaction = PersistenceManager.startTransaction();

		try {
			doArchive(customer, userManager, orgSaver, userSaver,
					loaderFactory, securityFilter, active, transaction);
			
		} catch (RuntimeException e) {
			PersistenceManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			PersistenceManager.finishTransaction(transaction);
		}
	}

	protected void doArchive(CustomerOrg customer, UserManager userManager,
			OrgSaver orgSaver, UserSaver userSaver,
			LoaderFactory loaderFactory, SecurityFilter securityFilter,
			boolean active, Transaction transaction) {
		
		EntityState newState = active ? EntityState.ACTIVE : EntityState.ARCHIVED;

		if (!active) {
			List<User> usersList = getUserList(userManager, securityFilter,	customer);
			for (User user : usersList) {
				user.archiveUser();
				userSaver.save(transaction, user);
			}
		}

		customer.setState(newState);
		
		DivisionOrgByCustomerListLoader divisionsLoader = loaderFactory.createDivisionOrgByCustomerListLoader();
		divisionsLoader.setCustomer(customer);
		List<DivisionOrg> divisions = divisionsLoader.load();
		for (DivisionOrg division : divisions) {
			division.setState(newState);
			orgSaver.update(transaction, division);
		}

		orgSaver.update(transaction, customer);
	}

	private List<User> getUserList(UserManager userManager, SecurityFilter securityFilter, CustomerOrg customer) {
		return userManager.getUsers(securityFilter, true, 1, USER_RESULTS_MAX, null, UserType.CUSTOMERS, customer).getList();
	}

}
