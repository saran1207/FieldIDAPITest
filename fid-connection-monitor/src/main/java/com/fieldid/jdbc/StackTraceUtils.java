package com.fieldid.jdbc;

public class StackTraceUtils {

	public static String formatFieldId(StackTraceElement[] stackTrace) {
		return format(stackTrace, "com.n4systems", "fieldid", "rfid");
	}

	public static String format(StackTraceElement[] stackTrace, String...filterPackages) {
		boolean printElement;
		int hiddenElementCount = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < stackTrace.length; i++) {
			printElement = false;
			if (i < 5) {
				printElement = true;
			} else {
				if (filterPackages != null && filterPackages.length > 0) {
					for (String filterPackage: filterPackages) {
						if (stackTrace[i].getClassName().startsWith(filterPackage)) {
							printElement = true;
							break;
						}
					}
				} else {
					printElement = true;
				}
			}

			if (printElement) {
				if (hiddenElementCount > 0) {
					sb.append("... ").append(hiddenElementCount).append(" ...").append('\n');
				}
				sb.append(stackTrace[i]).append('\n');
				hiddenElementCount = 0;
			} else {
				hiddenElementCount++;
			}
		}
		if (hiddenElementCount > 0) {
			sb.append("... ").append(hiddenElementCount).append(" ...").append('\n');
		}
		return sb.toString();
	}

}
