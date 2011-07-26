package com.n4systems.security;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

public class PasswordComplexityChecker implements PasswordValidator {
	private final Integer minLength;
	private final Integer minLowerAlpha;
	private final Integer minUpperAlpha;
	private final Integer minNumeric;
	private final Integer minPunctuation;

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
		Set<PasswordComplexityStatus> statusSet = checkPasswordComplexityStatus(pass);
		return statusSet.size()==0;	//ONLY contains errors. size 0 if valid 
	}
	
	public Set<PasswordComplexityStatus> checkPasswordComplexityStatus(String pass) {
		Set<PasswordComplexityStatus> statusSet = new HashSet<PasswordComplexityStatus>();

		String pw = StringUtils.trimToEmpty(pass);
		computeCounts(pw);			
		statusSet.add(checkLength());
		statusSet.add(checkLowerAlpha());
		statusSet.add(checkUpperAlpha());
		statusSet.add(checkNumeric());
		statusSet.add(checkPunctuation());
		statusSet.remove(PasswordComplexityStatus.VALID);		// remove VALID...leave only errors in set.
		
		return statusSet; 
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
	
	private PasswordComplexityStatus checkLength() {
		return (length >= minLength) ? PasswordComplexityStatus.VALID : PasswordComplexityStatus.MIN_LENGTH;
	}
	
	private PasswordComplexityStatus checkLowerAlpha() {
		return (lowerAlpha >= minLowerAlpha)  ? PasswordComplexityStatus.VALID : PasswordComplexityStatus.MIN_LOWER;
	}

	private PasswordComplexityStatus checkUpperAlpha() {
		return (upperAlpha >= minUpperAlpha) ? PasswordComplexityStatus.VALID : PasswordComplexityStatus.MIN_UPPER;
	}

	private PasswordComplexityStatus checkNumeric() {
		return (numeric >= minNumeric) ? PasswordComplexityStatus.VALID : PasswordComplexityStatus.MIN_NUMERIC;
	}

	private PasswordComplexityStatus checkPunctuation() {
		return (punctuation >= minPunctuation) ? PasswordComplexityStatus.VALID : PasswordComplexityStatus.MIN_PUNCTUATION;
	}

	public static PasswordComplexityChecker createDefault() {
		return new PasswordComplexityChecker(8, 1, 1, 1, 1);
	}

	public String generateValidPassword() {
		char[] punctuation = {'.','_','!','@','#','$','%','&','*','(',')','"',';',':'};  // not complete list of chars, but good enough to generate pw.
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
	
	public int getMinLength() {
		return minLength;
	}

	public int getMinLowerAlpha() {
		return minLowerAlpha;
	}

	public int getMinUpperAlpha() {
		return minUpperAlpha;
	}

	public int getMinNumeric() {
		return minNumeric;
	}

	public int getMinPunctuation() {
		return minPunctuation;
	}

	
	
	public enum PasswordComplexityStatus { 
		VALID, 
		MIN_LENGTH, 
		MIN_UPPER,
		MIN_LOWER, 
		MIN_NUMERIC,
		MIN_PUNCTUATION, 
		NULL_PASSWORD
	}
	
}
