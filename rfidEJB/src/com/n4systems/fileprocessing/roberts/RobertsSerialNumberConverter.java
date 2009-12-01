package com.n4systems.fileprocessing.roberts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RobertsSerialNumberConverter {
	private static final char SEP = ',';
	
	public RobertsSerialNumberConverter() {}
	
	public String toCSV(String serialNumberLine) {
		if (serialNumberLine == null) {
			return null;
		}

		Pattern serialPattern = Pattern.compile("^(.*?)\\s*(0*)(\\d+)\\s*to\\s*(\\d+)$", Pattern.CASE_INSENSITIVE);
		Matcher serialMatcher = serialPattern.matcher(serialNumberLine.trim());
		
		if (!serialMatcher.matches()) {
			return serialNumberLine.trim();
		}
		
		String prefix = serialMatcher.group(1);
		String zeroPadds = serialMatcher.group(2);
		long firstNum = Long.valueOf(serialMatcher.group(3));
		long secondNum = Long.valueOf(serialMatcher.group(4));
		
		// we do this to handle numbers ascending and descending
		long low = (firstNum <= secondNum) ? firstNum : secondNum;
		long high = (low == firstNum) ? secondNum : firstNum;
		
		String format = generateFormatString(prefix, zeroPadds, serialMatcher.group(3));
		
		StringBuilder serialCSV = new StringBuilder();
		serialCSV.append(String.format(format, low));
		
		for (long i = low + 1; i <= high; i++) {
			serialCSV.append(SEP);
			serialCSV.append(String.format(format, i));
		}
		
		return serialCSV.toString();
	}
	
	private static String generateFormatString(String prefix, String zeroPadds, String first) {
		String zeroPadFormat = "";
		if (zeroPadds.length() > 0) {
			zeroPadFormat = "0" + (zeroPadds.length() + first.length());
		}
 		
		StringBuilder format = new StringBuilder(prefix).append('%').append(zeroPadFormat).append('d');
		return format.toString();
	}
	
}
