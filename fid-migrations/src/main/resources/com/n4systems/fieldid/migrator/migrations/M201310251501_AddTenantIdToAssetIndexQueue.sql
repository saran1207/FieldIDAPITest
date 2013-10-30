alter table index_queue_items add column tenant_id bigint not null;

update index_queue_items i, assets a set i.tenant_id = a.tenant_id where a.id=i.id and (i.type="ASSET_INSERT" or i.type="ASSET_UPDATE");
update index_queue_items i, assettypes at set i.tenant_id = at.tenant_id where at.id=i.id and i.type="ASSET_TYPE";
update index_queue_items i, users u set i.tenant_id = u.tenant_id where u.id=i.id and i.type="USER";
update index_queue_items i, org_base o set i.tenant_id = o.tenant_id where o.id=i.id and i.type="ORG";
update index_queue_items i, orders o set i.tenant_id = o.tenant_id where o.id=i.id and i.type="ORDER";
update index_queue_items i, predefinedlocations p set i.tenant_id = p.tenant_id where p.id=i.id and i.type="PREDEFINEDLOCATION";
update index_queue_items i, assettypegroups atg set i.tenant_id = atg.tenant_id where atg.id=i.id and i.type="ASSETTYPEGROUP";
update index_queue_items i, assetstatus a set i.tenant_id = a.tenant_id where a.id=i.id and i.type="ASSETSTATUS";

delete from index_queue_items where type="TENANT";

alter table index_queue_items add constraint fk_index_queue_items_tenants foreign key (tenant_id) references tenants(id);