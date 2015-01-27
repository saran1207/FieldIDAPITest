package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by rrana on 2015-01-26.
 */
public class M201501261404_Add_Default_Values_To_ObservationCountGroupObservationCounts_Tables extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement tenantsStmt = null;
        PreparedStatement observationCountStmt = null;
        PreparedStatement observationCountGroupStmt = null;
        try {
            tenantsStmt = conn.prepareStatement("SELECT id from tenants");
            ResultSet resultSet = tenantsStmt.executeQuery();

            while(resultSet.next()) {
                Long tenantId = resultSet.getLong("id");

                observationCountStmt = conn.prepareStatement("SELECT id from observationcount where tenant_id=" +tenantId + ";");
                ResultSet observationCountSet = observationCountStmt.executeQuery();

                observationCountGroupStmt = conn.prepareStatement("SELECT id from observationcount_groups where tenant_id=" +tenantId + ";");
                ResultSet groupSet = observationCountGroupStmt.executeQuery();

                if(groupSet.next()) {
                    Long groupId = groupSet.getLong("id");

                    int orderIdx = 0;
                    while (observationCountSet.next()) {
                        Long observationCountId = observationCountSet.getLong("id");

                        String insertStr = "INSERT INTO observationcount_groups_observationcounts (observationcount_id, observationcount_group_id, orderIdx) VALUES" +
                                "(" + observationCountId + ", " + groupId + ", " + orderIdx + ");";

                        PreparedStatement pstmt = conn.prepareStatement(insertStr);
                        pstmt.executeUpdate();
                        DbUtils.close(pstmt);
                        orderIdx++;
                    }
                }
            }

        } finally {
            DbUtils.close(tenantsStmt);
        }
    }
}
