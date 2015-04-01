package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.BatchStatementExecutor;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.ddl.AlterTable;

import java.sql.Connection;

public class M201503201540_AddMissingCriteriaForeignKeys extends Migration {

	private static final String[] CRITERIA_TABLES = {
			"combobox_criteria",
			"datefield_criteria",
			"numberfield_criteria",
			"select_criteria",
			"signature_criteria",
			"textfield_criteria",
			"unitofmeasure_criteria"
	};

	@Override
	protected void up(Connection conn) throws Exception {
		BatchStatementExecutor executor = BatchStatementExecutor.create();
		for (String table: CRITERIA_TABLES) {
			executor = executor
					.add(AlterTable.named(table).addForeignKey("id", "criteria", "id"));
		}
		executor.execute(conn);
	}

}
