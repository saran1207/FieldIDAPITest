package com.n4systems.util;

import java.lang.reflect.Array;

public class HashCode {
	private static final int fODD_PRIME_NUMBER = 37;
	private static final int DEFAULT_INITIAL_SEED = 23;
	private int currentHash;
	
	private HashCode() {
		this(DEFAULT_INITIAL_SEED);
	}
	
	private HashCode(int initalSeed) {
		this.currentHash = initalSeed;
	}
	
	public static HashCode newHash() {
		return new HashCode();
	}
	
	public static HashCode newHash(int initalSeed) {
		return new HashCode(initalSeed);
	}
	
	private HashCode addToHash(int value) {
		currentHash = (fODD_PRIME_NUMBER * currentHash) + value;
		return this;
	}
	
	private HashCode addNull() {
		return add(0);
	}
	
	public HashCode add(Boolean b) {
		return (b == null) ? addNull() : add(b.booleanValue());
	}
	
	public HashCode add(boolean b) {
		return addToHash(b ? 1 : 0);
	}

	public HashCode add(Character c) {
		return (c == null) ? addNull() : addToHash(c.charValue());
	}
	
	public HashCode add(char c) {
		return addToHash((int)c);
	}

	public HashCode add(Integer i) {
		return (i == null) ? addNull() : addToHash(i.intValue());
	}
	
	public HashCode add(int i) {
		return addToHash(i);
	}

	public HashCode add(Long l) {
		return (l == null) ? addNull() : add(l.longValue());
	}
	
	public HashCode add(long l) {
		return addToHash((int)(l ^ (l >>> 32)));
	}

	public HashCode add(Float f) {
		return (f == null) ? addNull() : add(f.floatValue());
	}
	
	public HashCode add(float f) {
		return add(Float.floatToIntBits(f));
	}

	public HashCode hash(Double d) {
		return (d == null) ? addNull() : add(d.doubleValue());
	}
	
	public HashCode hash(double d) {
		return add(Double.doubleToLongBits(d));
	}

	private boolean isArray(Object obj) {
		return obj.getClass().isArray();
	}
	
	public HashCode add(Object obj) {
		HashCode result = this;
		if (obj == null) {
			result = addNull();
		} else if (!isArray(obj)) {
			result = add(obj.hashCode());
		} else {
			int length = Array.getLength(obj);
			for (int idx = 0; idx < length; ++idx) {
				Object item = Array.get(obj, idx);
				// recursive call!
				result = add(item);
			}
		}
		return result;
	}

	public int toHash() {
		return currentHash;
	}
}
