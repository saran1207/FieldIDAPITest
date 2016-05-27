package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.BatchStatementExecutor;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.ddl.AlterTable;

import java.sql.Connection;

/**
 * WEB-6175 requires we add indexes on procedure_definitions.procedure_code and
 * procedure_definitions.equipment_number in order to alleviate lack of responsiveness during
 * searches against either of these fields on Procedure Definition listing pages.
 *
 * Created by Jordan Heath on 2016-05-03.
 */
public class M201605031612_Add_Indexes_to_procedure_definitions_Table extends Migration {
    @Override
    protected void up(Connection conn) throws Exception {
        BatchStatementExecutor.create()
                .add(AlterTable.named("procedure_definitions")
                        .addIndex("procedure_code")
                        .addIndex("equipment_number"))
                .execute(conn);
    }
}
