package com.n4systems.fieldid.migrator;

public class SQLUtils {

	public static String buildInListParams(Object...params) {
		StringBuilder paramCsv = new StringBuilder();
		for (int i = 0; i < params.length; i++) {
			if (i > 0) {
				paramCsv.append(", ");
			}
			paramCsv.append('?');
		}
		return paramCsv.toString();
	}

	public static String escapeNames(String... names) {
		StringBuilder nameCsv = new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			if (i > 0) {
				nameCsv.append(", ");
			}
			nameCsv.append('`').append(names[i]).append('`');
		}
		return nameCsv.toString();
	}
}
