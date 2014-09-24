package com.n4systems.fieldid.api.pub.auth;

/**
 * Created by kirillternovsky on 2014-09-23.
 */
public class OAuthParams {
    String consumerKey;
    String tokenKey;
    String signatureMethod;
    String nonce;
    String timestamp;
    String signature;

    public OAuthParams(){}

    public OAuthParams consumerKey(String key) {
        consumerKey = key;
        return this;
    }

    public OAuthParams token(String key) {
        tokenKey = key;
        return this;
    }

    public OAuthParams signatureMethod(String method) {
        this.signatureMethod = method;
        return this;
    }

    public OAuthParams nonce(String nonce) {
        this.nonce = nonce;
        return this;
    }

    public OAuthParams timestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public OAuthParams signature(String signature) {
        this.signature = signature;
        return this;
    }
}
