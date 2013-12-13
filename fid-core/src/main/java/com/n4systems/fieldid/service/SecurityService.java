package com.n4systems.fieldid.service;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.MessageDigest;
import java.util.Random;

public class SecurityService {
	private static final char[] PASSWORD_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()?+-[]{}".toCharArray();

	@Autowired private Random secureRandom;
	@Autowired private MessageDigest sha512Digest;

	public char[] generateSalt(int bytes) {
		byte[] saltBytes = new byte[bytes];
		secureRandom.nextBytes(saltBytes);
		char[] saltChars = Hex.encodeHex(saltBytes);
		return saltChars;
	}

	public char[] hashSaltedPassword(String password, char[] salt) {
		StringBuilder saltedPassword = new StringBuilder(password);
		saltedPassword.append(salt);

		byte[] hashBytes = sha512Digest.digest(saltedPassword.toString().getBytes());
		char[] hashChars = Hex.encodeHex(hashBytes);
		return hashChars;
	}

	public String generatePassword(int length) {
		StringBuilder pass = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			pass.append(PASSWORD_CHARS[secureRandom.nextInt(PASSWORD_CHARS.length)]);
		}
		return pass.toString();
	}
}
