package com.n4systems.fieldid.ws.v1.resources;

import java.util.ArrayList;
import java.util.List;

import com.n4systems.fieldid.ws.v1.exceptions.NotFoundException;
import com.n4systems.model.parents.EntityWithTenant;
import org.springframework.stereotype.Component;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import com.n4systems.model.parents.AbstractEntity;

@Component
public abstract class ApiResource<A, E extends AbstractEntity> extends FieldIdPersistenceService {
	protected abstract A convertEntityToApiModel(E entityModel);

	protected List<A> convertAllEntitiesToApiModels(List<E> entityModels) {
		List<A> apiModel = new ArrayList<A>();
		for (E entityModel: entityModels) {
			apiModel.add(convertEntityToApiModel(entityModel));
		}
		return apiModel;
	}

    protected <T extends EntityWithTenant> T findEntity(Class<T> entityClass, Long id) {
        T entity = persistenceService.findUsingTenantOnlySecurityWithArchived(entityClass, id);
        if (entity == null) {
            throw new NotFoundException(entityClass.getSimpleName(), id);
        }
        return entity;
    }
}
