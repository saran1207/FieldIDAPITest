package com.n4systems.fieldid.api.mobile.resources.location;

import com.n4systems.fieldid.api.mobile.resources.SetupDataResource;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationLevels;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;

@Component
@Path("location")
public class ApiPredefinedLocationResource extends SetupDataResource<ApiPredefinedLocation, PredefinedLocation> {

	public ApiPredefinedLocationResource() {
		super(PredefinedLocation.class, true);
	}

	@Override
	protected ApiPredefinedLocation convertEntityToApiModel(PredefinedLocation location) {
		ApiPredefinedLocation apiLocation = new ApiPredefinedLocation();
		apiLocation.setSid(location.getId());
		apiLocation.setActive(location.isActive());
		apiLocation.setModified(location.getModified());
		apiLocation.setName(location.getName());
		apiLocation.setSearchIds(location.getSearchIds());

        if (location.getOwner()!=null) {
            apiLocation.setOwnerId(location.getOwner().getId());
        }

		if (location.getParent() != null) {
			apiLocation.setParentId(location.getParent().getId());
		}
		
		PredefinedLocationLevels locationLevels = persistenceService.find(createTenantSecurityBuilder(PredefinedLocationLevels.class));
		if (locationLevels != null) {
			apiLocation.setLevelName(locationLevels.getNameForLevel(location).getName());
		}
		
		return apiLocation;
	}

}
