package com.fieldid.jdbc;

import java.io.Serializable;

public class ExecutedStatement implements Serializable {
	private final long timestamp;
	private final String sql;
	private final String stackTrace;

	public ExecutedStatement(long timestamp, String sql, String stackTrace) {
		this.timestamp = timestamp;
		this.sql = sql;
		this.stackTrace = stackTrace;
	}

	public ExecutedStatement(long timestamp, String sql, StackTraceElement[] stackTrace) {
		this(timestamp, sql, StackTraceUtils.formatFieldId(stackTrace));
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
