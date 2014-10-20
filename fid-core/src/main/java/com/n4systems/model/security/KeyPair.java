package com.n4systems.model.security;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class KeyPair implements Serializable {
	@Column(name="key", nullable = false, length = 32)
	private String key;

	@Column(name="secret", nullable = false, length = 48)
	private String secret;

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
