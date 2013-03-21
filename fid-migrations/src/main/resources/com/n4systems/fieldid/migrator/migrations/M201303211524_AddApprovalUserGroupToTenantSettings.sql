alter table tenant_settings add column approval_user_id bigint default null;
alter table tenant_settings add column approval_user_group_id bigint default null;

alter table tenant_settings add constraint fk_tenant_settings_approval_user foreign key (approval_user_id) references users (id);
alter table tenant_settings add constraint fk_tenant_settings_approval_user_group foreign key (approval_user_group_id) references user_groups (id);