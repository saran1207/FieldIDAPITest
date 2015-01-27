package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by rrana on 2015-01-26.
 */
public class M201501261403_Add_Default_Values_To_ObservationCountGroup_Tables extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement tenantsStmt = null;
        try {
            tenantsStmt = conn.prepareStatement("SELECT id from tenants");
            ResultSet resultSet = tenantsStmt.executeQuery();

            while(resultSet.next()) {
                Long tenantId = resultSet.getLong("id");

                String insertStr = "INSERT INTO observationcount_groups (created, modified, tenant_id, name, state) VALUES" +
                        "(now(), now()," + tenantId + ", 'Default Observations', 'ACTIVE');";

                PreparedStatement pstmt = conn.prepareStatement(insertStr);
                pstmt.executeUpdate();
                DbUtils.close(pstmt);
            }

        } finally {
            DbUtils.close(tenantsStmt);
        }
    }
}
