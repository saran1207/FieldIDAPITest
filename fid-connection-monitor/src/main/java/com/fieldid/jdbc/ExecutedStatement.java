package com.fieldid.jdbc;

public class ExecutedStatement {
	private final long timestamp;
	private final String sql;
	private final String stackTrace;

	public ExecutedStatement(long timestamp, String sql, StackTraceElement[] stackTrace) {
		this.timestamp = timestamp;
		this.sql = sql;
		this.stackTrace = StackTraceUtils.formatFieldId(stackTrace);
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getSql() {
		return sql;
	}

	public String getStackTrace() {
		return stackTrace;
	}
}
