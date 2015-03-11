package com.n4systems.fieldid.migrator.ddl;

import com.n4systems.fieldid.migrator.DDLUtils;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.SQLUtils;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class DropTable implements ExecutableStatement {
	protected static Logger logger = Logger.getLogger(Migration.class);
	private final String table;

	public static DropTable named(String table) {
		return new DropTable(table);
	}

	private DropTable(String table) {
		this.table = table;
	}

	public void execute(Connection conn) throws SQLException {
		String ddl = "DROP TABLE " + SQLUtils.escapeNames(table);

		if (DDLUtils.tableExists(conn, table)) {
			logger.info("Executing:\n" + ddl);
			conn.createStatement().execute(ddl);
		} else {
			logger.warn("Ignoring: " + ddl);
		}
	}

}
