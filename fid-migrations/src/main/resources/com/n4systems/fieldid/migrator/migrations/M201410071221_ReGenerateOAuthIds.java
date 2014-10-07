package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class M201410071221_ReGenerateOAuthIds extends Migration {

	@Override
	public void up(Connection conn) throws Exception {
		regenKeyAndSecret(conn, "token", "users");
		regenKeyAndSecret(conn, "consumer", "tenant_settings");
	}

	private void regenKeyAndSecret(Connection conn, String prefix, String table) throws SQLException {
		String keyName = prefix + "_key";
		String secretName = prefix + "_secret";
		String sql = "SELECT `id`, `" + keyName + "`, `" + secretName + "` FROM `" + table + "`";

		logger.info(sql);

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			rs = stmt.executeQuery(sql);

			while(rs.next()) {
				rs.updateString(keyName, genUUID());
				rs.updateString(secretName, genUUID());
				rs.updateRow();
			}
		} finally {
			DbUtils.close(rs);
			DbUtils.close(stmt);
		}
	}

	private String genUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
