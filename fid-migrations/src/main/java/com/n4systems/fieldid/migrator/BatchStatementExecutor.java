package com.n4systems.fieldid.migrator;

import com.n4systems.fieldid.migrator.ddl.ExecutableStatement;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public class BatchStatementExecutor implements ExecutableStatement {
	private final ExecutableStatement[] statements;

	public static BatchStatementExecutor create() {
		return new BatchStatementExecutor(new ExecutableStatement[0]);
	}

	private BatchStatementExecutor(ExecutableStatement[] statements) {
		this.statements = statements;
	}

	public BatchStatementExecutor add(ExecutableStatement statement) {
		ExecutableStatement[] newStatements = Arrays.copyOf(statements, statements.length + 1);
		newStatements[newStatements.length - 1] = statement;
		return new BatchStatementExecutor(newStatements);
	}

	public BatchStatementExecutor add(String sql) {
		return add(c -> c.createStatement().execute(sql));
	}

	@Override
	public void execute(Connection conn) throws SQLException {
		for (ExecutableStatement stmt: statements) {
			stmt.execute(conn);
		}
	}

}
