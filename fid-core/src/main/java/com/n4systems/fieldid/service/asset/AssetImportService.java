package com.n4systems.fieldid.service.asset;


import com.n4systems.fieldid.service.PersistenceService;
import com.n4systems.fieldid.service.mixpanel.MixpanelService;
import com.n4systems.services.SecurityContext;
import com.n4systems.services.config.ConfigService;

import javax.persistence.EntityManager;

/**
 * Wrap the existing AssetService with the ability to provide our own
 * EntityManager instead of one injected from the context.
 */
public class AssetImportService extends AssetService {

    public AssetImportService(EntityManager entityManager, SecurityContext securityContext) {
        super();

        /* Given the passed in parameters create the services that will be needed by AssetService */
        setSecurityContext(securityContext);

        persistenceService = new PersistenceService(entityManager);
        persistenceService.setSecurityContext(securityContext);

        ConfigService configService = ConfigService.getInstance();
        configService.setSecurityContext(securityContext);

        MixpanelService mixpanelService = new MixpanelService();
        mixpanelService.setPersistenceService(persistenceService);
        mixpanelService.setSecurityContext(securityContext);
        mixpanelService.setConfigService(configService);
        setMixpanelService(mixpanelService);
    }

}
