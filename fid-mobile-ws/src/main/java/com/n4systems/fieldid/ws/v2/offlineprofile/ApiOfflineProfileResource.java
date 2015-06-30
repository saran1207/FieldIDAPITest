package com.n4systems.fieldid.ws.v2.offlineprofile;

import com.n4systems.fieldid.ws.v2.resources.ApiResource;
import com.n4systems.model.offlineprofile.OfflineProfile;
import org.springframework.stereotype.Component;

@Component
public class ApiOfflineProfileResource extends ApiResource<ApiOfflineProfile, OfflineProfile> {

	@Override
	public ApiOfflineProfile convertEntityToApiModel(OfflineProfile profile) {
		ApiOfflineProfile apiProfile = new ApiOfflineProfile();
		apiProfile.setUserId(profile.getUser().getId());
		apiProfile.setSyncDuration(profile.getSyncDuration());
		apiProfile.getAssets().addAll(profile.getAssets());
		apiProfile.getOrganizations().addAll(profile.getOrganizations());
		return apiProfile;
	}
	
}
