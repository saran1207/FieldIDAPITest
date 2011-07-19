package com.n4systems.fieldid.service;

import org.springframework.beans.factory.annotation.Autowired;

public class FieldIdPersistenceService extends FieldIdService {

//    @Autowired
    protected PersistenceService persistenceService;

    public PersistenceService getPersistenceService() {
        return persistenceService;
    }

    public void setPersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

}
