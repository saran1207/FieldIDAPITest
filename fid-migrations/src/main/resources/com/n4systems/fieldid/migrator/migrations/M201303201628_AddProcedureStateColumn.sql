set @procedure_group = (select id from column_mapping_groups where report_type="PROCEDURE" and group_key="procedure_details");

insert into column_mappings values(null, now(), now(), null, null, "label.procedure_state", "procedure_search_workflow_state", "workflowState", null, null, true, "com.n4systems.fieldid.viewhelpers.handlers.EnumHandler", 5, @procedure_group, null, null);

insert into system_column_mappings (select null, id from column_mappings where name = "procedure_search_workflow_state");
