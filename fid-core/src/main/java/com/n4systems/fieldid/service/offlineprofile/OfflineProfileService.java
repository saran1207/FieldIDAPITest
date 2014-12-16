package com.n4systems.fieldid.service.offlineprofile;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class OfflineProfileService extends FieldIdPersistenceService {

	@Transactional(readOnly = true)
	public OfflineProfile find(User user) {
		return persistenceService.find(getOfflineProfileQuery(user));
	}

    public boolean hasOfflineProfile(User user) {
        return persistenceService.exists(getOfflineProfileQuery(user));
    }

    private QueryBuilder<OfflineProfile> getOfflineProfileQuery(User user) {
        QueryBuilder<OfflineProfile> builder = createTenantSecurityBuilder(OfflineProfile.class);
        builder.addWhere(WhereClauseFactory.create("user.id", user.getId()));
        return builder;
    }

	public OfflineProfile findOrCreate(User user) {
		OfflineProfile offlineProfile = find(user);
		
		if (offlineProfile == null) {
			offlineProfile = new OfflineProfile();
			offlineProfile.setTenant(user.getTenant());
			offlineProfile.setUser(user);
			save(offlineProfile);
		}
		return offlineProfile;
	}

	public List<OfflineProfile> findAllProfilesForTenant(Long tenantId) {
		QueryBuilder<OfflineProfile> builder = createTenantSecurityBuilder(OfflineProfile.class);
		builder.addWhere(WhereClauseFactory.create("tenant.id", tenantId));

		return persistenceService.findAll(builder);
	}
	
	@Transactional
	public void save(OfflineProfile profile) {
		persistenceService.save(profile);
	}
	
	@Transactional
	public void update(OfflineProfile profile) {
		persistenceService.update(profile);
	}
}
