package com.n4systems.model.orgs.division;

import java.util.List;

import org.apache.log4j.Logger;

import com.n4systems.ejb.legacy.UserManager;
import com.n4systems.model.api.Archivable.EntityState;
import com.n4systems.model.orgs.DivisionOrg;
import com.n4systems.model.orgs.OrgSaver;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.user.DivisionUserListLoader;
import com.n4systems.model.user.User;
import com.n4systems.persistence.PersistenceManager;
import com.n4systems.persistence.Transaction;
import com.n4systems.persistence.loaders.LoaderFactory;

public class DivisionOrgArchiver {
	
	private static final Logger logger = Logger.getLogger(DivisionOrgArchiver.class);
			
	public void archiveDivision(DivisionOrg division, UserManager userManager,
			OrgSaver orgSaver, LoaderFactory loaderFactory, SecurityFilter securityFilter,
			boolean active) {
		
		Transaction transaction = PersistenceManager.startTransaction();

		try {
			doArchive(division, userManager, orgSaver, securityFilter, active, transaction);
			
		} catch (RuntimeException e) {
			logger.error("Error archiving customer", e);
			PersistenceManager.rollbackTransaction(transaction);
			throw e;
		} finally {
			PersistenceManager.finishTransaction(transaction);
		}
	}

	private void doArchive(DivisionOrg division, UserManager userManager,
			OrgSaver orgSaver, SecurityFilter filter, boolean active,
			Transaction transaction) {

		EntityState newState = active ? EntityState.ACTIVE : EntityState.ARCHIVED;

		if (!active) {
			List<User> usersList = getUserList(filter,	division);
			for (User user : usersList) {
				user.archiveUser();
				userManager.updateUser(user);
			}
		}
		
		division.setState(newState);
		orgSaver.update(division);
	}

	private List<User> getUserList(SecurityFilter filter, DivisionOrg division) {
		return new DivisionUserListLoader(new TenantOnlySecurityFilter(filter).setShowArchived(true)).setDivision(division).load();
	}

}
