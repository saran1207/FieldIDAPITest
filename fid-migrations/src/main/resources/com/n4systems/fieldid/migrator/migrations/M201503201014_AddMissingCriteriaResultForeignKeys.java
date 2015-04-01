package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.BatchStatementExecutor;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.ddl.AlterTable;

import java.sql.Connection;

public class M201503201014_AddMissingCriteriaResultForeignKeys extends Migration {

	private static final String[] CRITERIA_RESULT_TABLES = {
			"combobox_criteriaresults",
			"datefield_criteriaresults",
			"numberfield_criteriaresults",
			"observationcount_criteriaresults",
			"score_criteriaresults",
			"select_criteriaresults",
			"signature_criteriaresults",
			"textfield_criteriaresults",
			"unitofmeasure_criteriaresults"
//			"oneclick_criteriaresults"
	};

	@Override
	protected void up(Connection conn) throws Exception {
		BatchStatementExecutor executor = BatchStatementExecutor.create();
		for (String table: CRITERIA_RESULT_TABLES) {
			executor = executor
					.add("DELETE t FROM " + table + " t LEFT JOIN criteriaresults c ON t.id = c.id WHERE c.id IS NULL;")
					.add(AlterTable.named(table).addForeignKey("id", "criteriaresults", "id"));
		}
		executor.execute(conn);
	}

}
