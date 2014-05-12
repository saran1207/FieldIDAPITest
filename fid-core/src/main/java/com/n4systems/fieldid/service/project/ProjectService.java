package com.n4systems.fieldid.service.project;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.persistence.utils.PostFetcher;

public class ProjectService extends FieldIdPersistenceService {

    public int detachAsset(Asset asset, Project project) {
        project = persistenceService.findById(Project.class, project.getId());
        PostFetcher.postFetchFields(project, "assets");
        project.getAssets().remove(asset);
        project = persistenceService.update(project);
        return project.getAssets().size();
    }

}
