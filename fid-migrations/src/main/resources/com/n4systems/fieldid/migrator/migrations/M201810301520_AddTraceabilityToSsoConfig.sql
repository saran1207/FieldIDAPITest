ALTER TABLE sso_sp_metadata ADD COLUMN created datetime;
ALTER TABLE sso_sp_metadata ADD COLUMN createdby bigint(20);
ALTER TABLE sso_sp_metadata add constraint sso_sp_created_user foreign key (createdby) references users (id);
ALTER TABLE sso_idp_metadata ADD COLUMN created datetime;
ALTER TABLE sso_idp_metadata ADD COLUMN createdby bigint(20);
ALTER TABLE sso_idp_metadata add constraint sso_idp_created_user foreign key (createdby) references users (id);