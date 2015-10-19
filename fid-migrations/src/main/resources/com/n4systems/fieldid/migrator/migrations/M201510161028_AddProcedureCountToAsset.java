package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by rrana on 2015-10-16.
 */
public class M201510161028_AddProcedureCountToAsset extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement assetStmt = null;
        try {
            //Add the new column
            assetStmt = conn.prepareStatement("ALTER TABLE assets ADD active_procedure_definition_count bigint(20) DEFAULT -1");
            assetStmt.executeUpdate();

            //Find all assets that have LOTO enabled by asset type
            assetStmt = conn.prepareStatement("SELECT a.id as id from assets a, assettypes ap where a.type_id=ap.id and ap.hasProcedures=1 and a.state='ACTIVE' and ap.state='ACTIVE'");
            ResultSet resultSet = assetStmt.executeQuery();

            //Set the procedure count on those assets to be 0
            while(resultSet.next()) {
                Long assetID = resultSet.getLong("id");

                String insertStr = "UPDATE assets SET active_procedure_definition_count=0 WHERE id=" + assetID + "";

                assetStmt = conn.prepareStatement(insertStr);
                assetStmt.executeUpdate();
            }

            //Find all assets that have a procedure definition in ACTIVE STATES
            assetStmt = conn.prepareStatement("select asset_id as id from procedure_definitions where state='ACTIVE' and published_state in ('DRAFT', 'WAITING_FOR_APPROVAL', 'PUBLISHED', 'REJECTED')");
            resultSet = assetStmt.executeQuery();

            //Just in case, set these asset's to be 0 as well.  Just so that we don't have some that were still -1.
            while(resultSet.next()) {
                Long assetID = resultSet.getLong("id");

                String insertStr = "UPDATE assets SET active_procedure_definition_count=0 WHERE id=" + assetID + "";

                assetStmt = conn.prepareStatement(insertStr);
                assetStmt.executeUpdate();
            }

            //Find all assets that have a procedure definition in ACTIVE STATES
            assetStmt = conn.prepareStatement("select asset_id as id from procedure_definitions where state='ACTIVE' and published_state in ('DRAFT', 'WAITING_FOR_APPROVAL', 'PUBLISHED', 'REJECTED')");
            resultSet = assetStmt.executeQuery();
            //Increase the count for that asset.
            while(resultSet.next()) {
                Long assetID = resultSet.getLong("id");

                String insertStr = "UPDATE assets SET active_procedure_definition_count=active_procedure_definition_count+1 WHERE id=" + assetID + "";

                assetStmt = conn.prepareStatement(insertStr);
                assetStmt.executeUpdate();
            }

        } finally {
            DbUtils.close(assetStmt);
        }
    }
}
