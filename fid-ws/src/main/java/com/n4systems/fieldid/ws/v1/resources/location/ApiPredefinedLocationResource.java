package com.n4systems.fieldid.ws.v1.resources.location;

import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.n4systems.fieldid.ws.v1.resources.SetupDataResource;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationLevels;

@Component
@Path("locations")
public class ApiPredefinedLocationResource extends SetupDataResource<ApiPredefinedLocation, PredefinedLocation> {

	public ApiPredefinedLocationResource() {
		super(PredefinedLocation.class);
	}

	@Override
	public ApiPredefinedLocation convertEntityToApiModel(PredefinedLocation location) {
		ApiPredefinedLocation apiLocation = new ApiPredefinedLocation();
		apiLocation.setSid(location.getId());
		apiLocation.setActive(location.isActive());
		apiLocation.setModified(location.getModified());
		apiLocation.setName(location.getName());
		apiLocation.setSearchIds(location.getSearchIds());
		
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
