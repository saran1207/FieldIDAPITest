package com.n4systems.fieldid.api.pub.auth;

/**
 * Created by kirillternovsky on 2014-09-23.
 */
public interface OAuthSignature {
    public String sign(OAuthRequestParams requestParams, OAuthSecrets secrets);
    public boolean verify(OAuthRequestParams params, OAuthSecrets secrets);
}
