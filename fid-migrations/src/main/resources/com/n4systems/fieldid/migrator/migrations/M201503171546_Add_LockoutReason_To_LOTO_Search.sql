set @procedure_group = (select id from column_mapping_groups where report_type="PROCEDURE" and group_key="procedure_details");
insert into column_mappings values(null, now(), now(), null, null, "label.lockout_reason", "procedure_search_lockout_reason", "lockoutReason.name", "lockoutReason.name", null, true, null, 7, @procedure_group, "lockoutReason,lockoutReason", null);
insert into system_column_mappings (select null, id from column_mappings where name = "procedure_search_lockout_reason");

set @default_procedure_layout = (select id from column_layouts where report_type="PROCEDURE" and tenant_id is null);
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),15,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_lockout_reason"));

alter table saved_procedures add column lockout_reason varchar(255);