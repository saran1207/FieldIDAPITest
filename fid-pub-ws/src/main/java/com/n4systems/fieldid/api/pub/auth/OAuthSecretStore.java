package com.n4systems.fieldid.api.pub.auth;

/**
 * Created by kirillternovsky on 2014-09-24.
 */
public interface OAuthSecretStore {
    public OAuthSecrets getSecrets(OAuthParams params);
}
