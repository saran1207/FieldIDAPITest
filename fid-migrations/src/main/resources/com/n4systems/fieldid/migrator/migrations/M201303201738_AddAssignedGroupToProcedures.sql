alter table procedures add column assigned_group_id bigint default null;
alter table procedures add constraint fk_procedures_assigned_group foreign key (assigned_group_id) references user_groups (id);