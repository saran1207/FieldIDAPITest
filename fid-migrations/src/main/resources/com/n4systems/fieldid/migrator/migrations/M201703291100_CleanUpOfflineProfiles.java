package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.DbUtils;
import com.n4systems.fieldid.migrator.Migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by rrana on 2017-03-29.
 */
public class M201703291100_CleanUpOfflineProfiles extends Migration {

    @Override
    protected void up(Connection conn) throws Exception {
        PreparedStatement archivedUsersIdsQuery = null;
        try {
            archivedUsersIdsQuery = conn.prepareStatement("select op.id from users u, offline_profiles op where u.id=op.user_id and u.state='ARCHIVED'");
            ResultSet archivedIds = archivedUsersIdsQuery.executeQuery();

            while(archivedIds.next()) {
                Long profileID = archivedIds.getLong("id");

                //Delete offline_profiles_assets
                String deleteOfflineProfileAssetQueryString = "delete from offline_profiles_assets where offline_profiles_id=" + profileID + "";
                PreparedStatement deleteOfflineProfileAssetsQuery = conn.prepareStatement(deleteOfflineProfileAssetQueryString);
                deleteOfflineProfileAssetsQuery.execute();

                //Delete offline_profiles_orgs
                String deleteOfflineProfileOrgQueryString = "delete from offline_profiles_orgs where offline_profiles_id=" + profileID + "";
                PreparedStatement deleteOfflineProfileOrgQuery = conn.prepareStatement(deleteOfflineProfileOrgQueryString);
                deleteOfflineProfileOrgQuery.execute();

                //Delete offline_profiles
                String deleteOfflineProfileQueryString = "delete from offline_profiles where id=" + profileID + "";
                PreparedStatement deleteOfflineProfileQuery = conn.prepareStatement(deleteOfflineProfileQueryString);
                deleteOfflineProfileQuery.execute();

            }
        } finally {
            DbUtils.close(archivedUsersIdsQuery);
        }
    }
}
