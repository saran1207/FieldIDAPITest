package com.n4systems.model.security;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class KeyPair implements Serializable {

	public static KeyPair generate() {
		KeyPair kp = new KeyPair();
		kp.setSecret(generateKey());
		kp.setKey(generateKey());
		return kp;
	}

	private static String generateKey() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

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

	public boolean isSet() {
		return (secret != null && key != null);
	}
}
