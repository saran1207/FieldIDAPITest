package com.n4systems.model.orgs.customer;

import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.CustomerOrg;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.orgs.division.DivisionOrgArchiver;
import com.n4systems.model.orgs.division.DivisionOrgByCustomerListLoader;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.model.user.UserByOwnerListLoader;
import com.n4systems.model.user.UserSaver;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.LoaderFactory;

public class CustomerOrgArchiver {

	private static final Logger logger = Logger.getLogger(CustomerOrgArchiver.class);
	private DivisionOrgArchiver divisionOrgArchiver;
	
	public CustomerOrgArchiver() {
		divisionOrgArchiver = new DivisionOrgArchiver();
	}

	public void archiveCustomer(CustomerOrg customer, OrgSaver orgSaver, 
			UserSaver userSaver, LoaderFactory loaderFactory,  SecurityFilter securityFilter, boolean active) {

		Transaction transaction = PersistenceManager.startTransaction();

		try {
			doArchive(customer, orgSaver, userSaver,
					securityFilter, active, transaction);
			
		} catch (RuntimeException e) {
			logger.error("Error archiving customer", e);
			PersistenceManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			PersistenceManager.finishTransaction(transaction);
		}
	}

	public void doArchive(CustomerOrg customer, OrgSaver orgSaver, UserSaver userSaver,
			SecurityFilter filter, boolean active, Transaction transaction) {
		
		EntityState newState = active ? EntityState.ACTIVE : EntityState.ARCHIVED;

		if (!active) {
			List<User> usersList = getUserList(filter,	customer);
			for (User user : usersList) {
				user.archiveUser();
				userSaver.update(transaction, user);
			}
		}
		
		List<DivisionOrg> divisions = getDivisions(filter, customer);
		for (DivisionOrg division : divisions) {
			divisionOrgArchiver.doArchive(division, orgSaver, userSaver, filter, active, transaction);
		}

		customer.setState(newState);
		orgSaver.update(transaction, customer);
	}

	protected List<User> getUserList(SecurityFilter filter, CustomerOrg customer) {
		return new UserByOwnerListLoader(filter).owner(customer).load();
	}

	protected List<DivisionOrg> getDivisions(SecurityFilter filter, CustomerOrg customer) {
		return new DivisionOrgByCustomerListLoader(new TenantOnlySecurityFilter(filter).setShowArchived(true)).setCustomer(customer).load();
	}

	public void setDivisionOrgArchiver(DivisionOrgArchiver divisionOrgArchiver) {
		this.divisionOrgArchiver = divisionOrgArchiver;
	}
}
