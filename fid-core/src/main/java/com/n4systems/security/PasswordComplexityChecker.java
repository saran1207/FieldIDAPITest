package com.n4systems.security;

import org.apache.commons.lang.RandomStringUtils;

public class PasswordComplexityChecker implements PasswordValidator {
	private final int minLength;
	private final int minLowerAlpha;
	private final int minUpperAlpha;
	private final int minNumeric;
	private final int minPunctuation;

	private int length;
	private int lowerAlpha;
	private int upperAlpha;
	private int numeric;
	private int punctuation;
	
	public PasswordComplexityChecker(int minLength, int minLowerAlpha, int minUpperAlpha, int minNumeric, int minPunctuation) {
		this.minLength = minLength;
		this.minLowerAlpha = minLowerAlpha;
		this.minUpperAlpha = minUpperAlpha;
		this.minNumeric = minNumeric;
		this.minPunctuation = minPunctuation;
	}
	
	@Override
	public boolean isValid(String pass) {
		boolean valid = false;
		
		if (pass != null) {
			computeCounts(pass);
			valid = checkLength() && checkLowerAlpha() && checkUpperAlpha() && checkNumeric() && checkPunctuation();
		}
		
		return valid;
	}
	
	private void computeCounts(String pass) {
		length = pass.length();
		lowerAlpha = 0;
		upperAlpha = 0;
		numeric = 0;
		punctuation = 0;
		
		char chr;
		for (int i = 0; i < pass.length(); i++) {
			chr = pass.charAt(i);
			
			switch(Character.getType(chr)) {
				case Character.LOWERCASE_LETTER:
					lowerAlpha++;
					break;
				case Character.UPPERCASE_LETTER:
					upperAlpha++;
					break;
				case Character.DECIMAL_DIGIT_NUMBER:
					numeric++;
					break;
				default:
					punctuation++;
					break;
			}
		}
	}
	
	private boolean checkLength() {
		return (length >= minLength);
	}
	
	private boolean checkLowerAlpha() {
		return (lowerAlpha >= minLowerAlpha);
	}

	private boolean checkUpperAlpha() {
		return (upperAlpha >= minUpperAlpha);
	}

	private boolean checkNumeric() {
		return (numeric >= minNumeric);
	}

	private boolean checkPunctuation() {
		return (punctuation >= minPunctuation);
	}

	public static PasswordComplexityChecker createDefault() {
		return new PasswordComplexityChecker(8, 1, 1, 1, 1);
	}

	public String generatePassword() {
		char[] punctuation = {'.','_','!','@','#','$','%','&','*','(',')','"',';',':'};  // not complete list but good enough to generate pw.
		StringBuffer pw = new StringBuffer();
		pw.append(RandomStringUtils.randomAlphabetic(minLowerAlpha).toLowerCase());
		pw.append(RandomStringUtils.randomAlphabetic(minUpperAlpha).toUpperCase());
		pw.append(RandomStringUtils.randomNumeric(minNumeric));
		pw.append(RandomStringUtils.random(minPunctuation, punctuation));
		if (pw.length()<minLength) { 
			pw.append(RandomStringUtils.randomAlphabetic(minLength-pw.length()).toLowerCase());
		}		
		return pw.toString();
	}
	
}
