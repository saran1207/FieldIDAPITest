package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.BatchStatementExecutor;
import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.ddl.AlterTable;

import java.sql.Connection;

/**
 * We really should just do migrations like this.  It's great to understand SQL and all, but this method can really
 * save our bacon if a migration goes pear-shaped.
 *
 * This has to be broken up into a bunch of smaller pieces, because we actually depend on certain things being or not
 * being present to proceed to the next step.
 *
 * Created by Jordan Heath on 2016-04-13.
 */
public class M201604131712_Denormalize_Lock_and_Device_Definitions extends Migration {
    @Override
    protected void up(Connection conn) throws Exception {
        //Add the new columns...
        BatchStatementExecutor.create()
                .add(AlterTable.named("isolation_points")
                        .addColumn("device_definition", "VARCHAR(512)", false, false, false)
                        .addColumn("lock_definition", "VARCHAR(512)", false, false, false))
                .execute(conn);

        //Fill the new columns...
        BatchStatementExecutor.create()
                .add("UPDATE isolation_points ip" +
                        " LEFT JOIN isolation_device_descriptions idd1" +
                        " ON ip.device_definition_id = idd1.id" +
                        " LEFT JOIN isolation_device_descriptions idd2" +
                        " ON ip.lock_definition_id = idd2.id" +
                        " SET ip.device_definition = idd1.freeform_description," +
                        " ip.lock_definition = idd2.freeform_description;")
                .execute(conn);

        //Drop the no longer needed foreign keys...
        BatchStatementExecutor.create()
                .add(AlterTable.named("isolation_points")
                        .dropForeignKey("fk_lock", "lock_definition_id", "isolation_device_descriptions", "id")
                        .dropForeignKey("fk_device", "device_definition_id", "isolation_device_descriptions", "id"))
                .execute(conn);

        //Drop the no longer needed columns... done.
        //OMG that's careless.  I changed my mind, or got cold feet.  If we need to revert this, we want to keep those
        //columns... the keys can go, but the columns should stay.  Just like the table, we can't undelete these.
//        BatchStatementExecutor.create()
//                .add(AlterTable.named("isolation_points")
//                        .dropColumn("device_definition_id")
//                        .dropColumn("lock_definition_id"))
//                .execute(conn);

        //We'll hold off on deleting the isolation_device_descriptions table until we know everything is fine.  That
        //cleanup can happen in a subsequent release, as it would be difficult to undelete this table.
    }
}
