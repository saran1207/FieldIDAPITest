alter table procedures drop foreign key fk_procedures_performedby;

alter table procedures change column performedby_id unlockedby_id bigint;
alter table procedures add column lockedby_id bigint;

alter table procedures add constraint fk_procedures_locking_user foreign key (unlockedby_id) references users (id);
alter table procedures add constraint fk_procedures_unlocking_user foreign key (lockedby_id) references users (id);

update column_mappings set path_expression="unlockedBy.fullName", sort_expression="lockedBy.firstName,lockedBy.lastName", label="label.unlocked_by", name="procedure_search_unlockedby", output_handler=null where name="procedure_search_performedby";

set @procedure_group = (select id from column_mapping_groups where report_type="PROCEDURE" and group_key="procedure_details");

insert into column_mappings values(null, now(), now(), null, null, "label.locked_by", "procedure_search_lockedby", "lockedBy.fullName", "lockedBy.firstName,lockedBy.lastName", null, true, null, 35, @procedure_group, null, null);

insert into system_column_mappings (select null, id from column_mappings where name = "procedure_search_lockedby");
