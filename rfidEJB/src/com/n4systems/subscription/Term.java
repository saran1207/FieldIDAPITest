package com.n4systems.subscription;

public enum Term {
	ONE_YEAR(12), TWO_YEARS(24), THREE_YEARS(36);

	private int months;
	
	private Term(int months) {
		this.months = months;
	}
	
	public int getMonths() {
		return months;
	}
}
