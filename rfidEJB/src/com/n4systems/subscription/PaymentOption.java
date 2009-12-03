package com.n4systems.subscription;

public enum PaymentOption {
	MONTH_TO_MONTH(PaymentFrequency.Monthly, Term.ONE_YEAR),
	ONE_YEAR_UP_FRONT(PaymentFrequency.Upfront, Term.ONE_YEAR), 
	TWO_YEARS_UP_FRONT(PaymentFrequency.Upfront, Term.TWO_YEARS); 

	
	private static final PaymentOption DEFAULT_OPTION = MONTH_TO_MONTH;
	
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
	
	public boolean isDefault() {
		return this == DEFAULT_OPTION;
	}

	public static PaymentOption preferredOption() {
		return DEFAULT_OPTION;
	}
}
