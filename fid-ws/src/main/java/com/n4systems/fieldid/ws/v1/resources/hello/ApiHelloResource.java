package com.n4systems.fieldid.ws.v1.resources.hello;

import com.n4systems.fieldid.ws.v1.resources.EnhancedLogging;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
@Path("hello")
public class ApiHelloResource extends EnhancedLogging {

    private Logger logger = Logger.getLogger(ApiHelloResource.class);
    
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Trace  (dispatcher=true)
    public String hello(
            @QueryParam("tenant") String tenant,
            @QueryParam("user") String user,
            @QueryParam("version") String version,
            @QueryParam("device") String device) {

        logger.info(String.format("Hello: [%s:%s] [%s] [%s]", tenant, user, version, device));
        setEnhancedLoggingWithAppInfoParameters(tenant, user);
        return "Hello";
    }

}
