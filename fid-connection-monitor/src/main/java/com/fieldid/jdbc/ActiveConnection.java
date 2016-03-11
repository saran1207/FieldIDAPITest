package com.fieldid.jdbc;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActiveConnection implements Comparable<ActiveConnection>, Serializable {
	private final long id;
	private final long started;
	private final String threadName;
	private final List<ExecutedStatement> statements = new ArrayList<ExecutedStatement>();

	public ActiveConnection(long id, String threadName, long started) {
		this.id = id;
		this.threadName = threadName;
		this.started = started;
	}

	public long getId() {
		return id;
	}

	public long getStarted() {
		return started;
	}

	public long getRuntime() {
		return System.currentTimeMillis() - started;
	}

	public String getThreadName() {
		return threadName;
	}

	public List<ExecutedStatement> getStatements() {
		return statements;
	}

	public ExecutedStatement getLastExecutedStatement() {
		return (statements.size() > 0 ? statements.get(statements.size() - 1) : null);
	}

	@Override
	public int compareTo(ActiveConnection o) {
		return (started < o.started ? -1 : (started == o.started ? 0 : 1));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ActiveConnection that = (ActiveConnection) o;
		if (id != that.id) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return (int) (id ^ (id >>> 32));
	}

	@Override
	public String toString() {
		ZoneId zone = ZoneId.of("America/Toronto");
		String dateString = DateTimeFormatter.ISO_LOCAL_DATE_TIME.withZone(zone).format(Instant.ofEpochMilli(started).atZone(ZoneId.systemDefault()));



		String newLine = System.lineSeparator();
		StringBuilder sb = new StringBuilder()
				.append("MySQL ID: ").append(id).append(newLine)
				.append("Started: ").append(dateString).append(newLine)
				.append("Thread: ").append(threadName).append(newLine);

		statements.forEach((stmt) -> sb
				.append(String.format("============================== @ %7d ms ==============================", stmt.getTimestamp())).append(newLine)
				.append("Stack: [").append(newLine)
				.append(stmt.getStackTrace()).append(newLine)
				.append(']').append(newLine)
				.append("SQL: [").append(newLine)
				.append(stmt.getSql()).append(newLine)
				.append(']').append(newLine));

		return sb.toString();
	}
}
