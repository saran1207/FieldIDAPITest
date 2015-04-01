package com.n4systems.fieldid.migrator.ddl;

import java.sql.Connection;
import java.sql.SQLException;

public interface ExecutableStatement {
	public void execute(Connection conn) throws SQLException;
}
