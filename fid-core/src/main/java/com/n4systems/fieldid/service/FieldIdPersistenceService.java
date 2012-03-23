package com.n4systems.fieldid.service;

import com.n4systems.model.parents.AbstractEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.n4systems.model.Tenant;
import com.n4systems.model.user.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class FieldIdPersistenceService extends FieldIdService {

	@Autowired
    protected PersistenceService persistenceService;
    
    @PersistenceContext EntityManager _entityManager;

	public void setPersistenceService(PersistenceService persistenceService) {
		this.persistenceService = persistenceService;
	}
	
	protected Tenant getCurrentTenant() {
		return persistenceService.findNonSecure(Tenant.class, securityContext.getTenantSecurityFilter().getTenantId());
	}
	
	protected User getCurrentUser() {
		return persistenceService.find(User.class, securityContext.getUserSecurityFilter().getUserId());
	}
    
    protected <T extends AbstractEntity> Long getId(T entity) {
        if (entity == null) {
            return null;
        }
        return entity.getId();
    }

    @Deprecated
    // Needed to get legacy savers into service layer
    // We need to remove the savers completely by refactoring them into services themselves
    protected EntityManager getEntityManager() {
        return _entityManager;
    }
	
}
