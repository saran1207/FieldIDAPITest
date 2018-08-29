create table sso_entity (
  sso_entity_id varchar(255) not null,
  primary key (sso_entity_id)
);
create table sso_idp_metadata (
  id bigint AUTO_INCREMENT,
  idp_url varchar(255) not null,
  metadata TEXT not null,
  sso_entity_id varchar(255) not null,
  tenant_id bigint not null,
  primary key (id),
  unique (tenant_id)
);
alter table sso_idp_metadata add constraint idp_metadata_tenant_constraint foreign key (tenant_id) references tenants(id);
alter table sso_idp_metadata add constraint idp_metadata_entity_constraint foreign key (sso_entity_id) references sso_entity(sso_entity_id);

/*-- rollback

--alter table sso_idp_metadata drop foreign key idp_metadata_entity_constraint;
--alter table sso_idp_metadata drop foreign key idp_metadata_tenant_constraint;
--drop table sso_idp_metadata;
--drop table sso_entity;*/
