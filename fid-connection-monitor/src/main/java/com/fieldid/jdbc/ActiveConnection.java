package com.fieldid.jdbc;

import java.util.ArrayList;
import java.util.List;

public class ActiveConnection implements Comparable<ActiveConnection> {
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

}
