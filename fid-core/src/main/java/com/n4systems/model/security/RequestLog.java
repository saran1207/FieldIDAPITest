package com.n4systems.model.security;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name = "request_log")
public class RequestLog {

	@Column(name = "consumer_key", length = 32)
	private String consumerKey;

	@Column(name = "consumer_secret", length = 32)
	private String consumerSecret;

	@Column(name = "nonce", length = 36)
	private String nonce;

	@Column(name = "timestamp")
	private Long timestamp;

	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
