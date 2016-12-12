package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.BatchStatementExecutor;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.ddl.AlterTable;

import java.sql.Connection;

/**
 * Adding platform tracking columns to the procedures table.
 *
 * Created by jheath on 2016-10-12.
 */
public class M201610121547_Add_Platform_Tracking_To_Procedures_Table extends Migration {
    @Override
    protected void up(Connection conn) throws Exception {
        BatchStatementExecutor.create()
                .add(AlterTable.named("procedures")
                        .addColumn("created_platform", "VARCHAR(200)", false, false, false)
                        .addColumn("modified_platform", "VARCHAR(200)", false, false, false)
                        .addColumn("created_platform_type", "VARCHAR(30)", false, false, false)
                        .addColumn("modified_platform_type", "VARCHAR(30)", false, false, false))
                .execute(conn);
    }
}
