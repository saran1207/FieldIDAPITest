package com.n4systems.model;

import com.n4systems.util.CustomBaseEncoder;
import com.n4systems.util.math.MultiplicativeInverseEncoder;

public class PublicIdEncoder {
	// Primes 500000000 and 100000000 respectively.  WARNING, DO NOT CHANGE THESE.
	private static final long COPRIMEA = 11037271757L;
	private static final long COPRIMEB = 22801763489L;

	private static final char[] ID_CHARSET = ("0123456789" + "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

	protected static final MultiplicativeInverseEncoder longEncoder = new MultiplicativeInverseEncoder(COPRIMEA, COPRIMEB);
	protected static final CustomBaseEncoder stringEncoder = new CustomBaseEncoder(ID_CHARSET);

	public static String encode(long n) {
		return stringEncoder.encode(longEncoder.encode(n));
	}

	public static long decode(String s) {
		return longEncoder.decode(stringEncoder.decode(s));
	}
}
