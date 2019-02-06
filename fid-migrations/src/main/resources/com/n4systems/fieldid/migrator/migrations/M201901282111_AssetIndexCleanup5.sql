
alter table assets add index idx_asssets_linked_id (linked_id);
alter table assets drop index fk_linked_product_id;

alter table assets add index idx_asssets_network_id (network_id);
alter table assets drop index index_products_on_network_id;

alter table assets add index idx_asssets_predefinedlocation_id (predefinedlocation_id);
alter table assets drop index fk_products_predefinedlocations;

alter table assets add index idx_asssets_assetstatus_id (assetstatus_id);
alter table assets drop index index_assets_on_assetstatus_id;

alter table assets add index idx_asssets_createdby (createdby);
alter table assets drop index fk_created_by_user;


alter table assets add index idx_asssets_ownerid_tenantid_mobileguid_state (`owner_id`,`tenant_id`,`mobileguid`,`state`);
alter table assets drop index index_owner_id_tenant_id_mobileguid_state;
