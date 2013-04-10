alter table procedures change column completed_date unlock_date datetime;
alter table procedures add column lock_date datetime;

update column_mappings set path_expression="unlockDate", label="label.unlock_date", name="procedure_search_unlock_date" where name="procedure_search_completed_date";

set @procedure_group = (select id from column_mapping_groups where report_type="PROCEDURE" and group_key="procedure_details");

insert into column_mappings values(null, now(), now(), null, null, "label.lock_date", "procedure_search_lock_date", "lockDate", null, null, true, "com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler", 15, @procedure_group, null, null);

insert into system_column_mappings (select null, id from column_mappings where name = "procedure_search_lock_date");
