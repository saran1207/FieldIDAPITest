alter table procedures add column tenant_id bigint;

alter table procedures add constraint fk_procedures_tenants foreign key (tenant_id) references tenants(id);