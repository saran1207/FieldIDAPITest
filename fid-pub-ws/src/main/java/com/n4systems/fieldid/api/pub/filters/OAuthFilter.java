package com.n4systems.fieldid.api.pub.filters;

import com.n4systems.fieldid.api.pub.auth.*;
import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.model.user.User;
import com.n4systems.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Provider
@Scope("request")
public class OAuthFilter implements ContainerRequestFilter, ContainerResponseFilter {

	@Autowired
	public AuthService authService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		boolean bypass = (containerRequestContext.getUriInfo().getQueryParameters().getFirst("bypass") != null);

        OAuthRequestParams requestParams = OAuthRequestParamsFactory.readParams(containerRequestContext);
        OAuthParams params = requestParams.getOAuthParams();

		if (!bypass) {
			if (authService.exceededRequestLimit(params.getConsumerKey(), params.getTokenKey(), 3)) {
				throw new ForbiddenException(); // flood
			}

			if (!authService.validateRequest(params.getConsumerKey(), params.getTokenKey(), params.getNonce(), Long.parseLong(params.getTimestamp()))) {
				throw new ForbiddenException(); // replay
			}
		}

		User user = authService.findUserByOAuthKey(params.getTokenKey(), params.getConsumerKey());

        if(user == null) {
            throw new ForbiddenException();
        }

		if (!user.getTenant().getSettings().isPublicApiEnabled()) {
			throw new ForbiddenException();
		}

		if (!bypass) {
			OAuthSecrets secrets = new OAuthSecrets()
					.consumerSecret(user.getTenant().getSettings().getAuthConsumer().getSecret())
					.tokenSecret(user.getAuthToken().getSecret());

			OAuthSignature sig = null;
			try {
				sig = OAuthSignatureFactory.getSignature(params);
			} catch (NoSuchAlgorithmException ignored) {}

			//noinspection ConstantConditions
			boolean isValid = sig.verify(requestParams, secrets);

			if(!isValid) {
				throw new ForbiddenException();
			}
		}

		try {
			SecurityContextInitializer.initSecurityContext(user);
		} catch (SecurityException e) {
			throw new ForbiddenException();
		}
    }

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		/*
		 * XXX if an exception is thrown during the resource handling, this method won't be called.
		 * The CatchAllExceptionMapper has also been setup to reset the security context in that case. - mf
		 */
		SecurityContextInitializer.resetSecurityContext();
	}
}
