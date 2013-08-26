drop table if exists translations;

create table translations (
  tenant_id bigint not null,
  ognl varchar(50) not null,
  entity_id bigint not null,
  language varchar(10),
  value varchar(1000),
  primary key (tenant_id, entity_id, ognl, language),
  constraint fk_translation_tenant foreign key (tenant_id) references tenants(id)
);

