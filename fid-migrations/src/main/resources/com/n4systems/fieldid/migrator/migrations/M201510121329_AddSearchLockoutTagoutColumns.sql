INSERT INTO column_mapping_groups (created, modified, label, group_key, report_type, ordervalue) values (now(), now(), 'label.lockout_tagout', 'lockouttagout', 'ASSET', 5);

set @procedure_group = (select id from column_mapping_groups where report_type="ASSET" and group_key="lockouttagout");

INSERT INTO column_mappings (created, modified, label, name, path_expression, sortable, output_handler, default_order, group_id) values (now(), now(), 'label.loto_procedures', 'asset_search_lockouttagout', "",	0, 'com.n4systems.fieldid.viewhelpers.handlers.AssetProcedureCountHandler', 2000, @procedure_group);

INSERT into system_column_mappings(column_id) values((SELECT id FROM column_mappings WHERE name ='asset_search_lockouttagout'));