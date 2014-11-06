package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class M201411051028_PopulatePreconfiguredDevicesForEachTenant extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement tenantsStmt = null;
        try {
            tenantsStmt = conn.prepareStatement("SELECT id from tenants");
            ResultSet resultSet = tenantsStmt.executeQuery();

            while(resultSet.next()) {
                Long tenantId = resultSet.getLong("id");

                String insertStr = "INSERT INTO preconfigured_devices (tenant_id, created, modified, isolationPointSourceType, device) VALUES" +
                        "(" + tenantId + ", now(), now(), 'W', 'Adjustable Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'W', 'Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'W', 'Gate Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'W', 'Wedge style Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'V', 'Adjustable Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'V', 'Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'V', 'Gate Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'V', 'Wedge style Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'S', 'Adjustable Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'S', 'Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'S', 'Gate Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'S', 'Wedge style Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'P', 'Pneumatic Lockout'), " +
                        "(" + tenantId + ", now(), now(), 'G', 'Pressurized Gas Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'G', 'Adjustable Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'G', 'Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'G', 'Gate Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'G', 'Wedge style Ball Valve Cover'), " +
                        "(" + tenantId + ", now(), now(), 'E', 'Circuit Breaker Cover'), " +
                        "(" + tenantId + ", now(), now(), 'E', 'Electrical Lockout Plug'), " +
                        "(" + tenantId + ", now(), now(), 'E', 'Plug & Hoist Control Cover'), " +
                        "(" + tenantId + ", now(), now(), 'E', 'Rotary/Push Button Covers'), " +
                        "(" + tenantId + ", now(), now(), 'E', 'Universal Wall Switch Cover'), " +
                        "(" + tenantId + ", now(), now(), null, 'Cable Lockout'), " +
                        "(" + tenantId + ", now(), now(), null, 'Group Lock Box'), " +
                        "(" + tenantId + ", now(), now(), null, 'Lockout Hasps'), " +
                        "(" + tenantId + ", now(), now(), null, 'Steering Wheel Cover');";

                PreparedStatement pstmt = conn.prepareStatement(insertStr);

                pstmt.executeUpdate();

                DbUtils.close(pstmt);

            }

        } finally {
            DbUtils.close(tenantsStmt);
        }

    }
}
