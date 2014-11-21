package com.n4systems.util;

public class BitField extends Number implements Comparable<BitField> {
	private static final long serialVersionUID = -3392489141166744028L;
	private int mask;
	
	public static BitField create(int mask) {
		return new BitField(mask);
	}
	
	public BitField() {
		this(0);
	}
	
	public BitField(int mask) {
		this.mask = mask;
	}

	public int getMask() {
		return mask;
	}

	public BitField setMask(int mask) {
		this.mask = mask;
		return this;
	}
	
	/**
	 * @return true iff one or more bits are set in mask
	 */
	public boolean isSet(int bits) {
		return (mask & bits) != 0;
	}
	
	/**
	 * @return true iff ALL bits are set in mask
	 */
	public boolean isAllSet(int bits) {
		return (mask & bits) == bits;
	}
	
	public BitField set(int bit, boolean on) {
		if (on) {
			set(bit);
		} else {
			clear(bit);
		}
		return this;
	}

	public BitField set(int...bits) {
		for(int bit: bits) {
			mask |= bit;
		}
		return this;
	}
	
	public BitField clear(int...bits) {
		for(int bit: bits) {
			mask &= ~bit;
		}
		return this;
	}

	public BitField clearAll() {
		mask = 0;
		return this;
	}

	public BitField retain(int...bits) {
		int oldMask = mask;
		mask = 0;
		for(int bit: bits) {
			mask |= oldMask & bit;
		}
		return this;
	}

	@Override
	public double doubleValue() {
		return (double)mask;
	}

	@Override
	public float floatValue() {
		return (float)mask;
	}

	@Override
	public int intValue() {
		return mask;
	}

	@Override
	public long longValue() {
		return (long)mask;
	}

	public int compareTo(BitField other) {
		int otherMask = other.getMask();
		return (mask < otherMask ? -1 : (mask == otherMask ? 0 : 1));
	}
	
}
