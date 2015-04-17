package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.BatchStatementExecutor;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.ddl.AlterTable;

import java.sql.Connection;

public class M201504171000_AddMasterEventIndexes extends Migration {

	@Override
	protected void up(Connection conn) throws Exception {
		BatchStatementExecutor.create().add(
			AlterTable.named("masterevents")
				.addIndex("dueDate")
				.addIndex("completedDate")
				.addIndex("workflow_state")
				.addIndex("assignee_id")
		).execute(conn);
	}

}
