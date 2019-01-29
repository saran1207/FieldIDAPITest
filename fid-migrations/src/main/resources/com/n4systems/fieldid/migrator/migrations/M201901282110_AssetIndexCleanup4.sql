
alter table assets add index idx_asssets_shoporder_id (shoporder_id);
alter table assets drop index index_products_on_shoporder_id;
alter table assets drop index productserial_rordermaster_idx;

alter table assets add index idx_asssets_state (state);
alter table assets drop index index_products_on_state;

alter table assets add index idx_asssets_type_id (type_id);
alter table assets drop index index_products_on_type_id;

alter table assets add index idx_asssets_owner_id (owner_id);
alter table assets drop index fk_products_owner;

alter table assets add index idx_asssets_published (published);
alter table assets drop index index_products_on_published;


alter table assets add index idx_asssets_state_tenantid_linkedid (`state`,`tenant_id`,`linked_id`);
alter table assets drop index index_products_state_tenantid_linkedid;
