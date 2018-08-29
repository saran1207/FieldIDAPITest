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
create table sso_sp_bindings_hok_sso (
  id bigint not null,
  bindings_hok_sso varchar(255)
);
create table sso_sp_bindings_slo (
  id           BIGINT NOT NULL,
  bingings_slo VARCHAR(255)
);
create table sso_sp_bindings_sso (
  id bigint not null,
  bindings_sso varchar(255)
);
create table sso_sp_name_id (
  id bigint not null,
  name_id varchar(255)
)
create table sso_sp_metadata (
  id bigint AUTO_INCREMENT,
  alias varchar(255),
  assertion_consumer_index integer,
  email_address_attribute_name varchar(255),
  encryption_key varchar(255),
  entity_alias varchar(255),
  entity_base_url varchar(255),
  idp_discovery_enabled boolean,
  idp_discovery_response_url varchar(255),
  idp_discovery_url varchar(255),
  include_discovery_extension boolean,
  local boolean,
  match_email_address boolean not null,
  match_user_id boolean not null,
  request_signed boolean,
  require_artifact_resolve_signed boolean,
  require_logout_request_signed boolean,
  require_logout_response_signed boolean,
  security_profile varchar(255),
  serialized_metadata TEXT not null,
  sign_metadata boolean,
  signing_algorithm varchar(255),
  signing_key varchar(255),
  ssl_hostname_verification varchar(255),
  ssl_security_profile varchar(255),
  tls_key varchar(255),
  user_id_attribute_name varchar(255),
  want_assertion_signed boolean,
  sso_entity_id varchar(255) not null,
  tenant_id bigint not null,
  primary key (id)
);
alter table sso_sp_bindings_hok_sso add constraint sso_sp_metadata_hok_sso_constraint foreign key (id) references sso_sp_metadata;
alter table sso_sp_bindings_slo add constraint sso_sp_metadata_slo_constraint foreign key (id) references sso_sp_metadata;
alter table sso_sp_bindings_sso add constraint sso_sp_metadata_sso_constraint foreign key (id) references sso_sp_metadata;
alter table sso_sp_metadata add constraint sso_sp_metadata_tenant_constraint foreign key (tenant_id) references tenant;
alter table sso_sp_metadata add constraint sso_sp_metadata_entity_constraint foreign key (sso_entity_id) references sso_entity;
alter table sso_sp_name_id add constraint sso_sp_metadata_name_id_constraint foreign key (id) references sso_sp_metadata;

/*-- rollback

--alter table sso_idp_metadata drop foreign key idp_metadata_entity_constraint;
--alter table sso_idp_metadata drop foreign key idp_metadata_tenant_constraint;
--drop table sso_idp_metadata;
--drop table sso_entity;*/
