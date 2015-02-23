package com.n4systems.fieldid.api.pub.filters;

import com.n4systems.fieldid.api.pub.auth.*;
import com.n4systems.fieldid.service.SecurityContextInitializer;
import com.n4systems.model.user.User;
import com.n4systems.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Provider
public class OAuthFilter implements ContainerRequestFilter, ContainerResponseFilter {

	private static final String[] WHITELIST_PATHS = { "/api-docs" };

	@Autowired
	public AuthService authService;

	private boolean pathIsWhitelisted(UriInfo uriInfo) {
		return Arrays.stream(WHITELIST_PATHS).anyMatch(path -> uriInfo.getAbsolutePath().getPath().startsWith(path));
	}

	private void validateOAuthParams(OAuthParams params, boolean bypass) {
		if (params.getConsumerKey() == null) {
			throw new ForbiddenException("OAuth Consumer Key (oauth_consumer_key) not set");
		}
		if (params.getTokenKey() == null) {
			throw new ForbiddenException("OAuth Token (oauth_token) not set");
		}

		if (!bypass) {
			if (params.getNonce() == null) {
				throw new ForbiddenException("OAuth Nonce (oauth_nonce) not set");
			}
			if (params.getTimestamp() == null) {
				throw new ForbiddenException("OAuth Timestamp (oauth_timestamp) not set");
			}
			if (params.getSignatureMethod() == null) {
				throw new ForbiddenException("OAuth Signature Method (oauth_signature_method) not set");
			}
			if (params.getSignature() == null) {
				throw new ForbiddenException("OAuth Signature (oauth_signature) not set");
			}

			if (authService.exceededRequestLimit(params.getConsumerKey(), params.getTokenKey(), 10000)) {
				throw new ForbiddenException("API Request Limit Exceeded");
			}

			if (!authService.validateRequest(params.getConsumerKey(), params.getTokenKey(), params.getNonce(), Long.parseLong(params.getTimestamp()))) {
				throw new ForbiddenException("Replay Detected");
			}
		}
	}

	private User findUserByKey(OAuthParams params, boolean bypass) {
		User user = authService.findUserByOAuthKey(params.getTokenKey(), params.getConsumerKey());

		if(user == null) {
			throw new ForbiddenException("Token or Consumer key not valid");
		}

		if (!bypass && !user.getTenant().getSettings().isPublicApiEnabled()) {
			throw new ForbiddenException("API Access Disabled");
		}

		return user;
	}

	private void testSignature(OAuthRequestParams requestParams, User user, boolean bypass) throws UnsupportedEncodingException {
		if (!bypass) {
			OAuthSecrets secrets = new OAuthSecrets()
					.consumerSecret(user.getTenant().getSettings().getAuthConsumer().getSecret())
					.tokenSecret(user.getAuthToken().getSecret());

			OAuthSignature sig;
			try {
				sig = OAuthSignatureFactory.getSignature(requestParams.getOAuthParams());
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}

			//noinspection ConstantConditions
			boolean isValid = sig.verify(requestParams, secrets);

			if(!isValid) {
				throw new ForbiddenException("Signature did not match request");
			}
		}
	}

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
		UriInfo uriInfo = containerRequestContext.getUriInfo();

		if (pathIsWhitelisted(uriInfo) || containerRequestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
			return;
		}

		boolean bypass = (uriInfo.getQueryParameters().getFirst("bypass") != null);

        OAuthRequestParams requestParams = OAuthRequestParamsFactory.readParams(containerRequestContext);

		validateOAuthParams(requestParams.getOAuthParams(), bypass);
		User user = findUserByKey(requestParams.getOAuthParams(), bypass);
		testSignature(requestParams, user, bypass);

		try {
			SecurityContextInitializer.initSecurityContext(user);
		} catch (SecurityException e) {
			throw new ForbiddenException("API Access Disabled");
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
