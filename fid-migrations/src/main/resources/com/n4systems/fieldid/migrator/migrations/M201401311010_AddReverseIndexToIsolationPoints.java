package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class M201401311010_AddReverseIndexToIsolationPoints extends Migration {

    @Override
    public void up(Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("SELECT procedure_definition_id, count(*) AS count FROM procedure_definitions_isolation_points GROUP BY procedure_definition_id");
            ResultSet resultSet = pstmt.executeQuery();

            while(resultSet.next()) {
                Long procedureDefId = resultSet.getLong("procedure_definition_id");
                Integer count = resultSet.getInt("count");
                for(int i = 0 , j = count-1; i < count; i++, j--) {
                    Long isolationPointId = getIsolationPointId(conn, procedureDefId, i);
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE isolation_points SET rev_idx=" + j + " WHERE id=" + isolationPointId);
                    updateStmt.executeUpdate();
                }
            }
        } finally {
            DbUtils.close(pstmt);
        }
    }

    private Long getIsolationPointId(Connection conn, Long procedureDefId, int index) throws SQLException {
        PreparedStatement pstmt = null;
        Long id;
        try {
            pstmt = conn.prepareStatement("SELECT isolation_point_id FROM procedure_definitions_isolation_points WHERE procedure_definition_id = " + procedureDefId + " AND orderIdx=" + index);
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.first();
            id = resultSet.getLong("isolation_point_id");
        } finally {
            DbUtils.close(pstmt);
        }
        return id;
    }
}
