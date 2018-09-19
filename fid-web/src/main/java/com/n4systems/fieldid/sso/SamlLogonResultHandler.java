package com.n4systems.fieldid.sso;

import com.n4systems.fieldid.actions.utils.WebSessionMap;
import com.n4systems.fieldid.service.user.UserService;
import com.n4systems.model.user.User;
import com.n4systems.services.config.ConfigService;
import com.n4systems.sso.dao.SsoMetadataDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.saml.context.SAMLContextProvider;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * Created by agrabovskis on 2018-08-08.
 */
public class SamlLogonResultHandler implements AuthenticationFailureHandler, AuthenticationSuccessHandler {

    private static final Logger logger = Logger.getLogger(SamlLogonResultHandler.class);

    @Autowired
    private SAMLContextProvider contextProvider;

    @Autowired
    private SsoMetadataDao ssoMetadataDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SamlUserDetails userDetails = (SamlUserDetails) authentication.getDetails();
        HttpSession session = request.getSession();
        if (!userDetails.isAuthenticated()) {
            logger.info("SSO user " + userDetails.getUserId() + " reached the success handler but was not authenticated");
            onAuthenticationFailure(request, response, new SessionAuthenticationException("User not authenticated in FieldId"));
        }
        else {
            try {
               User user = userService.findUserByUserID(userDetails.getTenantName(), userDetails.getUserId());
               String newUrl = "/fieldid/logIntoSystem.action";
               synchronized (session) {
                   session.setAttribute(WebSessionMap.USER_ID, user.getId());
                   session.setAttribute(WebSessionMap.SSO_AUTHENTICATE, (new Date()).getTime());
               }
               logger.info("SSO user " + userDetails.getUserId() + " successfully authenticated - being passed to application");
               response.sendRedirect(newUrl);
           }
           catch(Throwable ex) {
               logger.error("SSO authentication success handler failed", ex);
               redirectToHomepage(request, response);
           }
        }
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.error("SSO authentication failure ", exception);
        redirectToHomepage(request, response);
    }

    private void redirectToHomepage(HttpServletRequest request, HttpServletResponse response) {
        try {
            SAMLMessageContext context = contextProvider.getLocalEntity(request, response);
            response.sendRedirect(ssoMetadataDao.getSpByEntityId(context.getLocalEntityId()).getEntityBaseURL() + "/w/dashboard");
        } catch (Exception ex) {
            System.out.println("SAMLLogonFailureHandler failed getting context");
            throw new RuntimeException(ex);
        }
    }

}
