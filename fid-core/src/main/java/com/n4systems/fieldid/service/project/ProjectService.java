package com.n4systems.fieldid.service.project;

import com.n4systems.fieldid.service.CrudService;
import com.n4systems.model.Asset;
import com.n4systems.model.Project;
import com.n4systems.persistence.utils.PostFetcher;
import com.n4systems.util.persistence.QueryBuilder;
import com.n4systems.util.persistence.WhereClauseFactory;
import com.n4systems.util.persistence.WhereParameter;

import javax.management.Query;

public class ProjectService extends CrudService<Project> {

    public ProjectService() {
        super(Project.class);
    }

    public int detachAsset(Asset asset, Project project) {
        project = persistenceService.findById(Project.class, project.getId());
        PostFetcher.postFetchFields(project, "assets");
        project.getAssets().remove(asset);
        project = persistenceService.update(project);
        return project.getAssets().size();
    }

    public boolean exists(String projectID, Long id) {
        QueryBuilder<Project> query = createUserSecurityBuilder(Project.class);

        query.addSimpleWhere("projectID", projectID);

        if(id != null) {
            query.addWhere(WhereClauseFactory.create(WhereParameter.Comparator.NE, "id", id));
        }

        return persistenceService.exists(query);
    }

}
