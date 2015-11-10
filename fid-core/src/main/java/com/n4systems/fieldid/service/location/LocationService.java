package com.n4systems.fieldid.service.location;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.location.PredefinedLocation;

public class LocationService extends FieldIdPersistenceService {

    public PredefinedLocation saveOrUpdate(PredefinedLocation predefinedLocation) {
        return persistenceService.saveOrUpdate(predefinedLocation);
    }

    public void archive(PredefinedLocation predefinedLocation) {
        predefinedLocation.archiveEntity();
        persistenceService.update(predefinedLocation);
    }


}
