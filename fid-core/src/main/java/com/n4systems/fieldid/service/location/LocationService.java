package com.n4systems.fieldid.service.location;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.location.PredefinedLocation;
import com.n4systems.model.orgs.BaseOrg;
import com.n4systems.util.persistence.QueryBuilder;

import java.util.List;

public class LocationService extends FieldIdPersistenceService {

    public PredefinedLocation saveOrUpdate(PredefinedLocation predefinedLocation) {
        return persistenceService.saveOrUpdate(predefinedLocation);
    }

    public void archive(PredefinedLocation predefinedLocation) {
        predefinedLocation.archiveEntity();
        persistenceService.update(predefinedLocation);
    }

    public void updateOwnerForAllChildren(long parent, BaseOrg newOwner) {
        QueryBuilder<PredefinedLocation> query = createTenantSecurityBuilder(PredefinedLocation.class);
        query.addSimpleWhere("parent.id", parent);
        List<PredefinedLocation> list = persistenceService.findAll(query);
        if(list.size() != 0) {
            for(PredefinedLocation location:list) {
                location.setOwner(newOwner);
                persistenceService.update(location);
                updateOwnerForAllChildren(location.getID(), newOwner);
            }
        }
    }

}
