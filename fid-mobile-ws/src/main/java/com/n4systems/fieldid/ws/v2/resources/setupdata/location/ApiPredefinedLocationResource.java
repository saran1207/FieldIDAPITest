package com.n4systems.fieldid.ws.v2.resources.setupdata.location;

import com.n4systems.fieldid.ws.v2.resources.setupdata.SetupDataResourceReadOnly;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.location.PredefinedLocationLevels;
import org.springframework.stereotype.Component;

import javax.ws.rs.Path;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Path("location")
public class ApiPredefinedLocationResource extends SetupDataResourceReadOnly<ApiPredefinedLocation, PredefinedLocation> {

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

    @Override
    protected List<ApiPredefinedLocation> convertAllEntitiesToApiModels(List<PredefinedLocation> entityModels) {
        return super.convertAllEntitiesToApiModels(entityModels)
                    .stream()
                    .sorted((location1, location2) ->
                                Integer.compare(location1.getSearchIds().size(), location2.getSearchIds().size()))
                    .collect(Collectors.toList());
    }
}
