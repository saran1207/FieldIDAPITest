package com.n4systems.fieldid.migrator.migrations;

        import com.n4systems.fieldid.migrator.DbUtils;
        import com.n4systems.fieldid.migrator.Migration;

        import java.sql.Connection;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;

/**
 * Created by rrana on 2015-10-13.
 */
public class M201510131528_EnableLotoSearchForTenants extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement tenantsStmt = null;
        try {
            PreparedStatement addLoto = conn.prepareStatement("update column_mappings set required_extended_feature='LotoAssetSearch' where name ='asset_search_lockouttagout'");
            addLoto.execute();

            tenantsStmt = conn.prepareStatement("select id from org_base where tenant_id in (select tenant_id from tenant_settings where loto_enabled=1)");
            ResultSet resultSet = tenantsStmt.executeQuery();

            while(resultSet.next()) {
                Long orgId = resultSet.getLong("id");

                String insertStr = "INSERT INTO org_extendedfeatures (org_id, feature) VALUES " +
                        "(" + orgId + ", 'LotoAssetSearch');";

                PreparedStatement pstmt = conn.prepareStatement(insertStr);
                pstmt.executeUpdate();

                DbUtils.close(pstmt);
            }

        } finally {
            DbUtils.close(tenantsStmt);
        }
    }
}
