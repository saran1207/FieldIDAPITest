create table users_user_groups (id bigint(21) not null auto_increment, user_id bigint(21) not null, user_group_id bigint(21) not null,
  primary key (id), constraint key_user_id_users foreign key (user_id) references users(id), constraint key_user_group_id_user_groups foreign key (user_group_id) references user_groups(id));

insert into users_user_groups (select null, u.id, ug.id from users u, user_groups ug where u.group_id = ug.id);