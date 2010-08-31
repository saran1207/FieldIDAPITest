package com.n4systems.util;

import java.util.Random;

/**
 * Class for generating random strings of arbitrary length and character sets.
 */
public class RandomString {
	/** a to z */
	public static final int WITH_LOWER_ALPHA		= 1 << 0;
	/** A to Z */
	public static final int WITH_UPPER_ALPHA		= 1 << 1;
	/** 0 to 9 */
	public static final int WITH_NUMERIC			= 1 << 2;
	/** A set of simple punctuation, intended for use in passwords */
	public static final int WITH_SIMPLE_PUNCTUATION	= 1 << 3;
	/** Same as {@link #WITH_LOWER_ALPHA} | {@link #WITH_UPPER_ALPHA} | {@link #WITH_NUMERIC} */
	public static final int ALPHA_NUMERIC			= WITH_LOWER_ALPHA | WITH_UPPER_ALPHA | WITH_NUMERIC;
	/** All available character sets */
	public static final int ALL_CHARACTERS 			= Integer.MAX_VALUE;
	
	protected static final char[] LOWER_ALPHA = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };	
	protected static final char[] UPPER_ALPHA = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	protected static final char[] NUMERIC = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	/** A set of simple punctuation, intended for use in passwords */
	protected static final char[] SIMPLE_PUNCTUATION = { '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '_', '=', '+', '[', ']', '{', '}', ':', '<', '>', '?' };
	
	private Random rng = new Random();
	private final char[] characterSet;
	private final char[] stringBuffer;
	
	/**
	 * Same as <code>(new RandomString(length)).next();</code>
	 * @param length Length of String to generate
	 */
	public static String getString(int length) {
		return (new RandomString(length)).next();
	}
	
	/**
	 * Initializes RandomString with the {@link #ALPHA_NUMERIC} character set and specified length of strings to generate.
	 * @param length Length of String to generate
	 */
	public RandomString(int stringLength) {
		this(ALPHA_NUMERIC, stringLength);
	}
	
	/**
	 * Initializes RandomString with a character set mask.
	 * @param enabledCharacterSets Character set bit mask.
	 * @param length Length of String to generate
	 * @see #WITH_LOWER_ALPHA
	 * @see #WITH_UPPER_ALPHA
	 * @see #WITH_NUMERIC
	 * @see #WITH_SIMPLE_PUNCTUATION
	 * @see #ALPHA_NUMERIC
	 * @see #ALL_CHARACTERS
	 */
	public RandomString(int enabledCharacterSets, int stringLength) {
		this(compileCharacterSet(enabledCharacterSets), stringLength);
	}
	
	/**
	 * Initializes RandomString with a custom character set.
	 * @param customCharacterSet character array to be used for 
	 * @param length Length of String to generate
	 */
	public RandomString(char[] customCharacterSet, int stringLength) {
		if (customCharacterSet == null || customCharacterSet.length == 0) {
			throw new IllegalArgumentException("Character Set must not be null or empty");
		}
		
		this.stringBuffer = new char[stringLength];
		
		// copy the customer character set to avoid external modification
		characterSet = new char[customCharacterSet.length];
		System.arraycopy(customCharacterSet, 0, characterSet, 0, customCharacterSet.length);
	}
	
	/**
	 * Allows tests and sub classes to override the Random number source. 
	 */
	protected void setRandomSource(Random rng) {
		this.rng = rng;
	}
	
	/**
	 * Combines all enabled character sets into a single array
	 * @param enabledCharacterSetBits bit field representing enabled character sets
	 * @return an array combining the character sets
	 */
	private static char[] compileCharacterSet(int enabledCharacterSetBits) {
		if (enabledCharacterSetBits == 0) {
			throw new IllegalArgumentException("Cannot create empty characeter set");
		}
		
		BitField enabledCharacterSets = new BitField(enabledCharacterSetBits);

		boolean usingLowerAlpha = enabledCharacterSets.isSet(WITH_LOWER_ALPHA);
		boolean usingUpperAlpha = enabledCharacterSets.isSet(WITH_UPPER_ALPHA);
		boolean usingNumeric = enabledCharacterSets.isSet(WITH_NUMERIC);
		boolean usingSimplePunc = enabledCharacterSets.isSet(WITH_SIMPLE_PUNCTUATION);
		
		// this is used in place of a disabled char set, when passed into ArrayUtils
		final char[] emptyCharSet = new char[0];
		
		// this combines a the enabled character sets into a single array.
		char[] charSet = ArrayUtils.combine(
				(usingLowerAlpha)	? LOWER_ALPHA			: emptyCharSet,
				(usingUpperAlpha)	? UPPER_ALPHA			: emptyCharSet,
				(usingNumeric) 		? NUMERIC				: emptyCharSet,
				(usingSimplePunc)	? SIMPLE_PUNCTUATION	: emptyCharSet
		);
		
		return charSet;
	}
	
	/**
	 * @return A copy of the current character set.
	 */
	public char[] getCharacterSet() {
		// returns a copy to avoid external modification 
		char[] returnSet = new char[characterSet.length];
		System.arraycopy(characterSet, 0, returnSet, 0, characterSet.length);
		return returnSet;
	}
	
	/**
	 * @return The next random String
	 */
	public String next() {
		synchronized(stringBuffer) {
			for (int i = 0; i < stringBuffer.length; i++) {
				stringBuffer[i] = characterSet[rng.nextInt(characterSet.length)];
			}
		}
		return new String(stringBuffer);
	}

	@Override
	public String toString() {
		return new String(stringBuffer);
	}
	
}
