package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by rrana on 2015-01-26.
 */
public class M201501261402_Add_Default_Values_To_ObservationCount_Tables extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement tenantsStmt = null;
        try {
            tenantsStmt = conn.prepareStatement("SELECT id from tenants");
            ResultSet resultSet = tenantsStmt.executeQuery();

            while (resultSet.next()) {
                Long tenantId = resultSet.getLong("id");

                String insertStr = "INSERT INTO observationcount (created, modified, tenant_id, name, value, counted, state) VALUES" +
                        "(now(), now()," + tenantId + ", 'Safe', 0, 1, 'ACTIVE');";

                String insertStr2 = "INSERT INTO observationcount (created, modified, tenant_id, name, value, counted, state) VALUES" +
                        "(now(), now()," + tenantId + ", 'Unsafe', 0, 1, 'ACTIVE');";

                String insertStr3 = "INSERT INTO observationcount (created, modified, tenant_id, name, value, counted, state) VALUES" +
                        "(now(), now()," + tenantId + ", 'Unseen', 0, 1, 'ACTIVE');";

                PreparedStatement pstmt = conn.prepareStatement(insertStr);
                pstmt.executeUpdate();

                PreparedStatement pstmt2 = conn.prepareStatement(insertStr2);
                pstmt2.executeUpdate();

                PreparedStatement pstmt3 = conn.prepareStatement(insertStr3);
                pstmt3.executeUpdate();

                DbUtils.close(pstmt);
                DbUtils.close(pstmt2);
                DbUtils.close(pstmt3);
            }

        } finally {
            DbUtils.close(tenantsStmt);
        }
    }
}
