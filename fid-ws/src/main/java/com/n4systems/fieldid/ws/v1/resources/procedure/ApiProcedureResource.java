package com.n4systems.fieldid.ws.v1.resources.procedure;

import com.n4systems.fieldid.service.FieldIdPersistenceService;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

public class ApiProcedureResource extends FieldIdPersistenceService {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void saveEvent(ApiProcedure apiProcedure) {
        if (apiProcedure.getSid() == null) {
            throw new NullPointerException("ApiProcedure has null sid");
        }
    }

}
