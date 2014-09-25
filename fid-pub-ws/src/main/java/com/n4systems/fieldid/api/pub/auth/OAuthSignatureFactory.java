package com.n4systems.fieldid.api.pub.auth;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by kirillternovsky on 2014-09-23.
 */
public class OAuthSignatureFactory {
    private static final String HMAC_SHA1_JAVANAME = "HmacSHA1";
    public static final String HMAC_SHA1 = "HMAC-SHA1";
    public static OAuthSignature getSignature(OAuthParams params) throws IllegalArgumentException, NoSuchAlgorithmException, UnsupportedEncodingException {
        if(!HMAC_SHA1.equals(params.getSignatureMethod())) {
            throw new IllegalArgumentException(String.format("Signature method %s is not supported", HMAC_SHA1_JAVANAME));
        }

        return new HmacSha1OAuthSignature();
    }
}
