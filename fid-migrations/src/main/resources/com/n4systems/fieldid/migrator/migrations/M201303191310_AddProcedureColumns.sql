insert into column_mapping_groups values (null, now(), now(), null, null, "label.procedure_details", "procedure_details", "PROCEDURE", 0);
set @procedure_group = (select max(id) from column_mapping_groups);
insert into column_mapping_groups values (null, now(), now(), null, null, "label.identifiers", "identifiers", "PROCEDURE", 10);
set @identifiers_group = (select max(id) from column_mapping_groups);
insert into column_mapping_groups values (null, now(), now(), null, null, "label.asset_details", "asset_details", "PROCEDURE", 20);
set @asset_group = (select max(id) from column_mapping_groups);

insert into column_mappings values(null, now(), now(), null, null, "label.due_date", "procedure_search_due_date", "dueDate", null, null, true, "com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler", 10, @procedure_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.completed_date", "procedure_search_completed_date", "completedDate", null, null, true, "com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler", 20, @procedure_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.assignee", "procedure_search_assignee", "assignee.fullName", "assignee.firstName,assignee.lastName", null, true, "com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler", 30, @procedure_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.performed_by", "procedure_search_performedby", "performedBy.fullName", "performedBy.firstName,performedBy.lastName", null, true, "com.n4systems.fieldid.viewhelpers.handlers.DateTimeHandler", 40, @procedure_group, null, null);

insert into column_mappings values(null, now(), now(), null, null, "label.identifier", "procedure_search_identifier", "type.asset.identifier", null, null, true, null, 110, @identifiers_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.rfidnumber", "procedure_search_rfidnumber", "type.asset.rfidnumber", null, null, true, null, 120, @identifiers_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.referencenumber", "procedure_search_referencenumber", "type.asset.customerRefNumber", null, null, true, null, 130, @identifiers_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.identifieddate", "procedure_search_identified_date", "type.asset.identified", null, null, true, null, 140, @identifiers_group, null, null);

insert into column_mappings values(null, now(), now(), null, null, "label.asset_type_group", "procedure_search_assettypegroup", "type.asset.type.group.name", null, null, true, null, 210, @asset_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.assettype", "procedure_search_assettype", "type.asset.type.name", null, null, true, null, 220, @asset_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.assetstatus", "procedure_search_assetstatus", "type.asset.assetStatus.name", null, null, true, null, 230, @asset_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.identifiedby", "procedure_search_identifiedby", "type.asset.identifiedBy.fullName", null, null, false, null, 240, @asset_group, null, null);
insert into column_mappings values(null, now(), now(), null, null, "label.description", "procedure_search_asset_description", "type.asset.description", null, null, false, null, 250, @asset_group, null, null);

insert into system_column_mappings (select null, id from column_mappings where name like "procedure_search%");
