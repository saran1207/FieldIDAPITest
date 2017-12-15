package com.n4systems.fieldid.service.asset;


import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.services.SecurityContext;

import javax.persistence.EntityManager;

/**
 * Wrap the existing AssetService with the ability to provide our own
 * EntityManager instead of one injected from the context.
 */
public class AssetImportService extends AssetService {

    public AssetImportService(EntityManager entityManager, SecurityContext securityContext) {
        super();
        System.out.println("AssetImportService received securityContext " + securityContext.hashCode());
        this.persistenceService = new PersistenceService(entityManager);
        setSecurityContext(securityContext);
    }

}
