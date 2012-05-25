package com.n4systems.model.user;

import javax.persistence.EntityManager;

import com.n4systems.exceptions.DuplicateRfidException;
import com.n4systems.exceptions.DuplicateUserException;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.security.SecurityFilter;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.persistence.savers.Saver;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;
import com.n4systems.util.persistence.WhereParameter.Comparator;

public class UserSaver extends Saver<User> {

	@Override
	public void save(EntityManager em, User entity) {
        SecurityFilter filter = new TenantOnlySecurityFilter(entity.getTenant().getId());
		checkUnique(em, entity, filter);
		
		super.save(em, entity);
	}

	@Override
	public User update(EntityManager em, User entity) {
        SecurityFilter filter = new TenantOnlySecurityFilter(entity.getTenant().getId());
		checkUnique(em, entity, filter);
        cleanOfflineProfile(em, entity, filter);
		return super.update(em, entity);
	}
	
	private void checkUnique(EntityManager em, User user, SecurityFilter filter) {
		if(userIdExists(em, filter, user)) {
			throw new DuplicateUserException("Account with userId " + user.getUserID() + " already exists for Tenant " + user.getTenant().getName(), user.getUserID());
		}
		
		if(rfidExists(em, filter, user)) {
			throw new DuplicateRfidException("Account with hashed RFID " + user.getHashSecurityCardNumber() + " already exists for Tenant " + user.getTenant().getName(), user.getUserID(), user.getHashSecurityCardNumber());
		}
	}
	
	private boolean userIdExists(EntityManager em, SecurityFilter filter, User user) {
		if(user.getUserID() == null) {
			return false;
		}
		
		QueryBuilder<User> userCount = new QueryBuilder<User>(User.class, filter);
		userCount.addWhere(Comparator.EQ, "userID", "userID", user.getUserID(), WhereParameter.IGNORE_CASE);
		
		if (!user.isNew()) {
			userCount.addWhere(Comparator.NE, "id", "id", user.getId());
		}
		
		return userCount.entityExists(em);
	}

	public boolean rfidExists(EntityManager em, SecurityFilter filter, User user) {
		if(user.getHashSecurityCardNumber() == null || user.getHashSecurityCardNumber().length() == 0) {
			return false;
		}
		
		QueryBuilder<User> userCount = new QueryBuilder<User>(User.class, filter);
		userCount.addSimpleWhere("hashSecurityCardNumber", user.getHashSecurityCardNumber());
		
		if (!user.isNew()) {
			userCount.addWhere(Comparator.NE, "id", "id", user.getId());
		}
		
		return userCount.entityExists(em);
	}

    private void cleanOfflineProfile(EntityManager em, User user, SecurityFilter filter) {
        // The following query checks to see if the users owner has changed.
        QueryBuilder<User> ownerChangedQuery = new QueryBuilder<User>(User.class, filter);
        ownerChangedQuery.addWhere(WhereClauseFactory.create("id", user.getId()));
        ownerChangedQuery.addWhere(WhereClauseFactory.create("owner.id", user.getOwner().getId()));

        if (!ownerChangedQuery.entityExists(em)) {
            // Users owner has changed, delete the offline profile.
            QueryBuilder<OfflineProfile> offlineProfileQuery = new QueryBuilder<OfflineProfile>(OfflineProfile.class, filter);
            offlineProfileQuery.addWhere(WhereClauseFactory.create("user.id", user.getId()));

            OfflineProfile offlineProfile = offlineProfileQuery.getSingleResult(em);
            em.remove(offlineProfile);
        }
    }
}
