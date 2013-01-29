ALTER TABLE users ADD COLUMN group_id bigint(20);


alter table users add constraint fk_group_id_on_user_groups foreign key (group_id) references user_groups(id);