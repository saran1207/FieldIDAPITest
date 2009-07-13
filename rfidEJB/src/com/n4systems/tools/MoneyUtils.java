package com.n4systems.tools;

public class MoneyUtils {

	private final int PRECISION = 2;
	private String zero;
	
	public MoneyUtils() {
		char[] z = new char[PRECISION];
		for (int i=0; i<z.length; i++) z[i] = '0';
		zero = new String(z);
	}
	
	public Integer toCents(String amount) {
		String padded;
		int dot = amount.indexOf('.');
		if (dot == -1) {
			padded = amount + zero.substring(0, PRECISION);
		} else {
			int end = PRECISION - (amount.length() - dot - 1);
			if (end < 0) throw new IllegalArgumentException("precision not enough");
			padded = amount.substring(0, dot) + amount.substring(dot+1) + zero.substring(0, end);
		}

		return Integer.parseInt(padded);
	}
	
	public String fromCents(Long cents) {
		if( cents == null ) {
			return "";
		}
		String amount = String.valueOf(cents);
		if (amount.length() <= PRECISION) {
			amount = zero.substring(0, PRECISION - amount.length() + 1) + amount;
		}
		return amount.substring(0, amount.length() - PRECISION) + "." + 
				amount.substring(amount.length() - PRECISION, amount.length());
		
	}
	
}
