package com.n4systems.model.orgs.secondaryorg;

import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.InternalOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.customer.CustomerOrgArchiver;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByOwnerListLoader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.LoaderFactory;

public class SecondaryOrgArchiver {
	
	private static final Logger logger = Logger.getLogger(SecondaryOrgArchiver.class);
	private CustomerOrgArchiver customerOrgArchiver = new CustomerOrgArchiver();

	public void archiveSecondaryOrg(InternalOrg org, OrgSaver orgSaver, UserSaver userSaver,
			LoaderFactory loaderFactory,  SecurityFilter securityFilter, boolean active) {
		Transaction transaction = PersistenceManager.startTransaction();

		try {
			doArchive(org, orgSaver, userSaver, securityFilter, active, transaction);
			
		} catch (RuntimeException e) {
			logger.error("Error archiving customer", e);
			PersistenceManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			PersistenceManager.finishTransaction(transaction);
		}
	}

	private void doArchive(InternalOrg secondaryOrg, OrgSaver orgSaver, UserSaver userSaver,
			SecurityFilter filter, boolean active,Transaction transaction) {

		EntityState newState = active ? EntityState.ACTIVE : EntityState.ARCHIVED;

        if (!active) {
			List<User> usersList = getUserList(filter,	secondaryOrg);
			for (User user : usersList) {
				user.archiveUser();
				userSaver.update(transaction, user);
			}
		}
		
		List<CustomerOrg> customers = getCustomers(filter, secondaryOrg);
		for (CustomerOrg customer : customers) {
			customerOrgArchiver.doArchive(customer, orgSaver, userSaver, filter, active, transaction);
		}
		
		secondaryOrg.setState(newState);
		orgSaver.update(transaction, secondaryOrg);
	}
	
	private List<User> getUserList(SecurityFilter filter, InternalOrg secondaryOrg) {
		return new UserByOwnerListLoader(filter).owner(secondaryOrg).load();
	}
	
	private List<CustomerOrg> getCustomers(SecurityFilter filter,  InternalOrg secondaryOrg) {
		return new CustomerOrgByOwnerListLoader(new TenantOnlySecurityFilter(filter).setShowArchived(true)).setOwner(secondaryOrg).load();
	}
}
