package com.n4systems.fieldid.migrator.migrations;

import com.n4systems.fieldid.migrator.Migration;
import com.n4systems.fieldid.migrator.ddl.AlterTable;

import java.sql.Connection;

public class M201502061131_AddMissingTenantIdForeignKeys extends Migration {

    private static final String[] MISSING_FOREIGN_KEY_TABLES = {
            "active_column_mappings",
            "add_asset_history",
            "assetcodemapping",
            "assets",
            "assetstatus",
            "assettypes",
            "assettypeschedules",
            "asset_type_schedules",
            "autoattributecriteria",
            "autoattributedefinition",
            "buttons",
            "button_groups",
            "catalogs",
            "column_layouts",
            "criteria",
            "criteriaresults",
            "criteriasections",
            "dashboard_layouts",
            "eulaacceptances",
            "eventbooks",
            "events",
            "eventtypegroups",
            "eventtypes",
            "fileattachments",
            "findassetoption_manufacture",
            "loto_settings",
            "notificationsettings",
            "observationcount",
            "observationcount_groups",
            "observationcount_result",
            "observations",
            "orders",
            "populatorlog",
            "preconfigured_devices",
            "predefinedlocations",
            "projects",
            "requesttransactions",
            "saved_items",
            "scores",
            "score_groups",
            "send_saved_item_schedules",
            "tagoptions",
            "userrequest",
            "users"
    };

    @Override
    protected void up(Connection conn) throws Exception {
        for (String table: MISSING_FOREIGN_KEY_TABLES) {
            AlterTable.named(table).addForeignKey("tenant_id", "tenants", "id").execute(conn);
        }
    }
}
