package com.n4systems.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtility {
	
	/**
	 * Returns a hexidecimal version of a SHA1 hash of the given message
	 * @param message
	 * @return
	 */
	public static String getSHA1HexHash(String message) {
		
		if (message == null) return null;
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
		} catch (NoSuchAlgorithmException e ) {
			e.printStackTrace();
			return null;
		}

		md.update(message.getBytes());

		byte raw[] = md.digest();
		String hash = getAsHexaDecimal(raw);
		return hash;
	}
	
	private static String getAsHexaDecimal(byte[] bytes) {
		StringBuffer s = new StringBuffer(bytes.length*2);
		
		for (int n=0; n < bytes.length; n++) {
			int number = bytes[n];
			number = (number < 0) ? (number + 256) : number; // shift to positive range
			String numberString = Integer.toString(number, 16);
			// pad with 0 on left
			if (numberString.length() == 1) {
				numberString = "0"+numberString;
			}
			s.append(numberString);
		}
		
		return s.toString();
	}

}
