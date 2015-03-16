package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class M201503131619_AddDefaultLockoutReasons extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement tenantsStmt = null;
        try {
            tenantsStmt = conn.prepareStatement("SELECT id from tenants");
            ResultSet resultSet = tenantsStmt.executeQuery();

            while(resultSet.next()) {
                Long tenantId = resultSet.getLong("id");

                String insertStr = "INSERT INTO lockout_reasons (tenant_id, created, modified, name, state) VALUES" +
                        "(" + tenantId + ", now(), now(), 'Planned Maintenance', 'ACTIVE'), " +
                        "(" + tenantId + ", now(), now(), 'Planned Repair', 'ACTIVE'), " +
                        "(" + tenantId + ", now(), now(), 'Adjustments', 'ACTIVE'), " +
                        "(" + tenantId + ", now(), now(), 'Minor Tool Change', 'ACTIVE'), " +
                        "(" + tenantId + ", now(), now(), 'Troubleshooting', 'ACTIVE'), " +
                        "(" + tenantId + ", now(), now(), 'Clearing a Jam', 'ACTIVE');";

                PreparedStatement pstmt = conn.prepareStatement(insertStr);
                pstmt.executeUpdate();

                DbUtils.close(pstmt);
            }

        } finally {
            DbUtils.close(tenantsStmt);
        }
    }
}
