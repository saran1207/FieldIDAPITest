set @default_procedure_layout = (select id from column_layouts where report_type="PROCEDURE" and tenant_id is null);

delete from active_column_mappings where column_layout_id = @default_procedure_layout;

insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),10,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_workflow_state"));
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),20,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_due_date"));
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),30,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_lock_date"));
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),40,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_unlock_date"));
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),50,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_assignee"));
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),60,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_lockedby"));
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),70,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_unlockedby"));
insert into active_column_mappings (created,modified,ordervalue,column_layout_id,mapping_id) values (now(),now(),80,@default_procedure_layout,(select id from column_mappings where name = "procedure_search_identifier"));
