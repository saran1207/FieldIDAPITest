package com.n4systems.model.security;

import java.io.Serializable;

import javax.persistence.Embeddable;

import com.google.common.base.Objects;

@Embeddable
@SuppressWarnings("serial")
public class PasswordPolicy implements Serializable {
	private int minLength;
	private int minNumbers;
	private int minSymbols;
	private int minCapitals;
	private int expiryDays;
	private int uniqueness;

	public PasswordPolicy() {
		this(6, 0, 0, 0, 0, 0);
	}
	
	public PasswordPolicy(int minLength, int minNumbers, int minSymbols, int minCapitals, int expiryDays, int uniqueness) {
		this.minLength = minLength;
		this.minNumbers = minNumbers;
		this.minSymbols = minSymbols;
		this.minCapitals = minCapitals;
		this.expiryDays = expiryDays;
		this.uniqueness = uniqueness;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMinNumbers() {
		return minNumbers;
	}

	public void setMinNumbers(int minNumbers) {
		this.minNumbers = minNumbers;
	}

	public int getMinSymbols() {
		return minSymbols;
	}

	public void setMinSymbols(int minSymbols) {
		this.minSymbols = minSymbols;
	}

	public int getMinCapitals() {
		return minCapitals;
	}

	public void setMinCapitals(int minCapitals) {
		this.minCapitals = minCapitals;
	}

	public int getExpiryDays() {
		return expiryDays;
	}

	public void setExpiryDays(int expiryDays) {
		this.expiryDays = expiryDays;
	}

	public void setUniqueness(int uniqueness) {
		this.uniqueness = uniqueness;
	}

	public int getUniqueness() {
		return uniqueness;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).toString();
	}

}
