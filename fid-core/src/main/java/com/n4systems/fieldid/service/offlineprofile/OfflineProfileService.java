package com.n4systems.fieldid.service.offlineprofile;

import org.springframework.transaction.annotation.Transactional;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.offlineprofile.OfflineProfile;
import com.n4systems.model.user.User;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;

public class OfflineProfileService extends FieldIdPersistenceService {

	@Transactional(readOnly = true)
	public OfflineProfile find(User user) {
		QueryBuilder<OfflineProfile> builder = createTenantSecurityBuilder(OfflineProfile.class);
		builder.addWhere(WhereClauseFactory.create("user.id", user.getId()));
		
		OfflineProfile profile = persistenceService.find(builder);
		return profile;
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
	
	@Transactional
	public void save(OfflineProfile profile) {
		persistenceService.save(profile);
	}
	
	@Transactional
	public void update(OfflineProfile profile) {
		persistenceService.update(profile);
	}
}
