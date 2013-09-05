package com.n4systems.fieldid.migrator;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

public class ScriptMigration extends Migration {
	private final String scriptResourcePath;

	public ScriptMigration(String scriptResourcePath) {
		this.scriptResourcePath = scriptResourcePath;
	}

	@Override
	public String getMigrationName() {
		return new File(scriptResourcePath).getName().replace(".sql", "");
	}

	@Override
	protected void up(Connection conn) throws Exception {
		String script = loadScript();

        String[] statements;
        if (script.contains("//")) {
            // Special case: In the case where multiline statements with semicolons are required, use an alternate delimiter: //
            statements = script.split("//");
        } else {
            statements = script.split(";");
        }

		Statement stmt = null;
		for (String sql: statements) {
			sql = sql.trim();
			if (sql.length() > 0 && !sql.startsWith("--")) {
				try {
					stmt = conn.createStatement();
					logger.debug("Executing SQL: \n\t" + sql.replace("\n", "\n\t"));
					stmt.execute(sql);
				} finally {
					DbUtils.close(stmt);
				}
			}
		}
	}

	private String loadScript() throws IOException {
		InputStream in = null;
		try {
			in = getClass().getResourceAsStream(scriptResourcePath);
			return IOUtils.toString(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

}
