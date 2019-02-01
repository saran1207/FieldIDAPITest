package com.n4systems.fieldid.config;

import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.services.SecurityContext;
import com.newrelic.api.agent.NewRelic;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Scope("singleton")
@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Throwable> {
    private Logger logger = Logger.getLogger(CatchAllExceptionMapper.class);

    @Autowired
    protected SecurityContext securityContext;

    @Override
    public Response toResponse(Throwable exception) {
        /*
         * XXX: due to the Jersey filter design, the response filer cannot reset the security context if an exception is thrown.
         * It is important we do this here, as it's probable our last chance to clear it. - mf
         */

        String tenantId;
        String userId;
        try {
            tenantId = securityContext.getUserSecurityFilter().getUser().getTenant().getName();
            userId = securityContext.getUserSecurityFilter().getUser().getUserID();
        }
        catch(Exception ex) {
            tenantId = "unknown";
            userId = "unknown";
        }

        SecurityContextInitializer.resetSecurityContext();

        NewRelic.noticeError(exception);
        if (exception instanceof JsonParseException) {
            logger.error("JSON parse exception for tenantId '" + tenantId + "', userId '" + userId + "'", exception);
            /* Json parse error is a bad data error as opposed to a server error */
            return Response.status(Status.BAD_REQUEST).type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        else {
            logger.error("Uncaught exception in webservice for tenantId '" + tenantId + "', userId '" + userId + "'", exception);
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.TEXT_PLAIN_TYPE).build();
        }
    }

}
