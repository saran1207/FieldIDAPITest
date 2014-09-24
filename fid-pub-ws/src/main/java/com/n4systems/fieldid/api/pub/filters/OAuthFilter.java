package com.n4systems.fieldid.api.pub.filters;

import com.n4systems.fieldid.api.pub.auth.*;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kirillternovsky on 2014-09-24.
 */
@Provider
public class OAuthFilter implements ContainerRequestFilter {

    private OAuthSecretStore secretStore = new OAuthSecretStoreImpl();

    private static class OAuthSecretStoreImpl implements OAuthSecretStore {
        private static final String consumerSecret = "kd94hf93k423kf44";
        private static final String tokenSecret = "pfkkdhi9sl3r4s00";

        @Override
        public OAuthSecrets getSecrets(OAuthParams params) {
            return new OAuthSecrets().consumerSecret(consumerSecret).tokenSecret(tokenSecret);
        }
    }
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        OAuthRequestParams requestParams = OAuthRequestParamsFactory.readParams(containerRequestContext);
        OAuthParams params = requestParams.getOAuthParams();
        OAuthSecrets secrets = secretStore.getSecrets(params);
        OAuthSignature sig = null;
        try {
            sig = OAuthSignatureFactory.getSignature(params);
        }
        catch(NoSuchAlgorithmException ignored) {}

        //noinspection ConstantConditions
        boolean isValid = sig.verify(requestParams, secrets);

        if(!isValid) {
            containerRequestContext.abortWith(Response.status(403).build());
        }

    }
}
