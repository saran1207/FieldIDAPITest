package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class M201209251354_ExampleClassMigration extends Migration {
    @Override
    public void up(Connection conn) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement("SELECT * from tenants");
			pstmt.execute();
		} finally {
			DbUtils.close(pstmt);
		}
    }

}
