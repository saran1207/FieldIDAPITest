package com.n4systems.util;

import java.util.HashMap;
import java.util.Map;

public class CustomBaseEncoder {
	private final char[] charset;
	private final Map<Character, Integer> charsetReverseMap;

	public CustomBaseEncoder(char[] charset) {
		this.charset = charset;
		this.charsetReverseMap = new HashMap<>();
		for (int i = 0; i < charset.length; i++) {
			this.charsetReverseMap.put(charset[i], i);
		}
	}

	public String encode(long n) {
		if (n < 0) {
			throw new IllegalArgumentException(n + " not > 0");
		}
		StringBuilder s = new StringBuilder();
		int r;
		do {
			r = (int) (n % charset.length);
			n = (long) Math.floor(n / charset.length);
			s.append(charset[r]);
		} while (n > 0);
		return s.reverse().toString();
	}

	public long decode(String s) {
		long n = 0;
		for (char c: s.toCharArray()) {
			n = (n * charset.length) + charsetReverseMap.get(c);
		}
		return n;
	}
}
