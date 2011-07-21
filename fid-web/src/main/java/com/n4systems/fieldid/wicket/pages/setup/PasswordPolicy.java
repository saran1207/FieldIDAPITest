package com.n4systems.fieldid.wicket.pages.setup;

import java.io.Serializable;

import com.google.common.base.Objects;

public class PasswordPolicy implements Serializable {
	private static final long serialVersionUID = 1948224580850349962L;
	
	private Integer minLength;
	private Integer minNumbers;
	private Integer minSymbols;
	private Integer minCapitals;
	private Integer expiryDays;
	private Integer uniqueness;	
	
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	public Integer getMinNumbers() {
		return minNumbers;
	}
	public void setMinNumbers(Integer minNumbers) {
		this.minNumbers = minNumbers;
	}
	public Integer getMinSymbols() {
		return minSymbols;
	}
	public void setMinSymbols(Integer minSymbols) {
		this.minSymbols = minSymbols;
	}
	public Integer getMinCapitals() {
		return minCapitals;
	}
	public void setMinCapitals(Integer minCapitals) {
		this.minCapitals = minCapitals;
	}
	public Integer getExpiryDays() {
		return expiryDays;
	}
	public void setExpiryDays(Integer expiryDays) {
		this.expiryDays = expiryDays;
	}
	public void setUniqueness(Integer uniqueness) {
		this.uniqueness = uniqueness;
	}
	public Integer getUniqueness() {
		return uniqueness;
	}
	
	@Override
	public String toString() { 
		return Objects.toStringHelper(this).toString();
	}
	
	@Deprecated //only for development use until persistence is implemented
	public static PasswordPolicy makeDummyPasswordPolicy() {
		PasswordPolicy passwordPolicy = new PasswordPolicy();
		passwordPolicy.setMinCapitals(1);
		passwordPolicy.setMinLength(6);
		passwordPolicy.setMinNumbers(1);
		passwordPolicy.setMinSymbols(0);
		passwordPolicy.setUniqueness(3);
		return passwordPolicy;
	}
}
