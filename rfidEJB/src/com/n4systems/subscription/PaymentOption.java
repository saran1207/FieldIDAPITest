package com.n4systems.subscription;

public enum PaymentOption {
	ONE_YEAR_UP_FRONT(PaymentFrequency.Upfront, Term.ONE_YEAR), 
	TWO_YEARS_UP_FRONT(PaymentFrequency.Upfront, Term.TWO_YEARS), 
	THREE_YEARS_UP_FRONT(PaymentFrequency.Upfront, Term.THREE_YEARS), 
	MONTH_TO_MONTH(PaymentFrequency.Monthly, Term.ONE_YEAR);
	
	private final PaymentFrequency frequency;
	private final Term term;
	
	private PaymentOption(PaymentFrequency frequency, Term term) {
		this.frequency = frequency;
		this.term = term;
	}

	public PaymentFrequency getFrequency() {
		return frequency;
	}

	
	public int getTerm() {
		return term.getMonths();
	}
}
