ALTER TABLE masterevents ADD COLUMN assigned_group_id bigint(20);


alter table masterevents add constraint fk_assigned_group_id_on_user_groups foreign key (assigned_group_id) references user_groups(id);