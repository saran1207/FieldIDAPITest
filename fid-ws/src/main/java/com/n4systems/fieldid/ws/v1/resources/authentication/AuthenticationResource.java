package com.n4systems.fieldid.ws.v1.resources.authentication;

import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.fieldid.ws.v1.exceptions.ForbiddenException;
import com.n4systems.fieldid.ws.v1.resources.FieldIdPersistenceServiceWithEnhancedLogging;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUser;
import com.n4systems.fieldid.ws.v1.resources.user.ApiUserResource;
import com.n4systems.model.security.TenantOnlySecurityFilter;
import com.n4systems.model.security.UserSecurityFilter;
import com.n4systems.model.user.User;
import com.n4systems.services.SecurityContext;
import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/authenticate")
@Component
public class AuthenticationResource extends FieldIdPersistenceServiceWithEnhancedLogging {
    private static Logger logger = Logger.getLogger(AuthenticationResource.class);

    @Autowired protected UserService userService;
    @Autowired protected ApiUserResource apiUserResource;
    @Autowired protected SecurityContext securityContext;
    @Autowired private HttpServletRequest request;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public ApiUser authenticate(
            @FormParam("tenant") String tenantName,
            @FormParam("user") String userId, 
            @FormParam("password") String password) {
        
        logger.info("Standard authentication for " + tenantName + " for " + userId);

        if (tenantName == null || userId == null || password == null) {
            throw new ForbiddenException();
        }

        User user = userService.authenticateUserByPassword(tenantName, userId, password);
        if (user != null) {
            setEnhancedLoggingWithAppInfoParameters(tenantName,user.getUserID());
        }
        else {
            NewRelic.addCustomParameter("Tenant", tenantName);
            setEnhancedLoggingAppInfoParameters();
        }
        return authenticateUser(user);
    }
    
    @POST
    @Path("passcode")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Trace  (dispatcher=true)
    @Transactional(readOnly = true)
    public ApiUser authenticate(
            @FormParam("tenant") String tenantName,
            @FormParam("passcode") String passcode) {
        
        logger.info("Passcode authentication for " + tenantName);
        setEnhancedLoggingWithAppInfoParameters();

        if (tenantName == null || passcode == null) {
            throw new ForbiddenException();
        }
        
        User user = userService.authenticateUserBySecurityCard(tenantName, passcode);
        if (user != null) {
            setEnhancedLoggingWithAppInfoParameters(tenantName,user.getUserID());
        }
        else {
            NewRelic.addCustomParameter("Tenant", tenantName);
            setEnhancedLoggingAppInfoParameters();
        }
        return authenticateUser(user);
    }
    
    private ApiUser authenticateUser(User user) {
        if (user == null) {
            throw new ForbiddenException();
        }
        
        securityContext.setUserSecurityFilter(new UserSecurityFilter(user));
        securityContext.setTenantSecurityFilter(new TenantOnlySecurityFilter(user.getTenant().getId()));
        
        ApiUser apiUser = apiUserResource.convertEntityToApiModel(user);
        return apiUser;
    }

}
