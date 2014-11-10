package com.n4systems.fieldid.api.pub.auth;

import com.google.common.base.Preconditions;

/**
 * Created by kirillternovsky on 2014-09-23.
 */
public class OAuthSecrets {
    String consumerSecret;
    String tokenSecret;

    public OAuthSecrets() {}

    public OAuthSecrets consumerSecret(String secret) {
        consumerSecret = secret;
        return this;
    }

    public OAuthSecrets tokenSecret(String secret) {
        tokenSecret = secret;
        return this;
    }

    public String getSigningString() {
        Preconditions.checkNotNull(consumerSecret);
        Preconditions.checkNotNull(tokenSecret);

        return OAuthEncoder.encode(consumerSecret) + '&' + OAuthEncoder.encode(tokenSecret);
    }
}
