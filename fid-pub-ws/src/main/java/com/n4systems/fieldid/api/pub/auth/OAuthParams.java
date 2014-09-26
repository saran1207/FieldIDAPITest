package com.n4systems.fieldid.api.pub.auth;

public class OAuthParams {
    private String consumerKey;
	private String tokenKey;
	private String signatureMethod;
	private String nonce;
	private String timestamp;
	private String signature;

    public String getVersion() {
        return version;
    }

    public OAuthParams setVersion(String version) {
        this.version = version;
        return this;
    }

    private String version;

	public String getConsumerKey() {
		return consumerKey;
	}

	public OAuthParams setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
		return this;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public OAuthParams setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
		return this;
	}

	public String getSignatureMethod() {
		return signatureMethod;
	}

	public OAuthParams setSignatureMethod(String signatureMethod) {
		this.signatureMethod = signatureMethod;
		return this;
	}

	public String getNonce() {
		return nonce;
	}

	public OAuthParams setNonce(String nonce) {
		this.nonce = nonce;
		return this;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public OAuthParams setTimestamp(String timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	public String getSignature() {
		return signature;
	}

	public OAuthParams setSignature(String signature) {
		this.signature = signature;
		return this;
	}
}
