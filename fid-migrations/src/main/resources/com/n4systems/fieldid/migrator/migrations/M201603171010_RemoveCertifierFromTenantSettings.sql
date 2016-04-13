ALTER TABLE tenant_settings DROP FOREIGN KEY fk_tenant_settings_approval_user;
ALTER TABLE tenant_settings DROP FOREIGN KEY fk_tenant_settings_approval_user_group;

ALTER TABLE tenant_settings DROP COLUMN approval_user_id;
ALTER TABLE tenant_settings DROP COLUMN approval_user_group_id;