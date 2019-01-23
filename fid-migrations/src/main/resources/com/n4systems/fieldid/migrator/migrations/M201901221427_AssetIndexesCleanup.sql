
alter table assets add unique key pk_assets_id (id);
alter table assets drop index productserial_uniqueid_idx;

alter table assets add unique key uk_assets_mobileguid (mobileguid);
alter table assets drop index mobile_guid_unique;

alter table assets add index idx_asssets_assigneduser_id (assigneduser_id);
alter table assets drop index index_products_on_assigneduser_id;

alter table assets add index idx_asssets_customerorder_id (customerorder_id);
alter table assets drop index index_products_on_customerorder_id;

alter table assets add index idx_asssets_identifiedby_id (identifiedby_id);
alter table assets drop index index_products_on_identifiedby_uniqueid;

alter table assets add index idx_asssets_mobileguid (mobileguid);
alter table assets drop index index_products_on_mobileguid;

alter table assets add index idx_asssets_modifiedby (modifiedby);
alter table assets drop index index_products_on_modifiedby;

alter table assets add index idx_asssets_purchaseorder (purchaseorder);
alter table assets drop index index_productserial_on_purchaseorder;

alter table assets add index idx_asssets_tenant_id (tenant_id);
alter table assets drop index smart_search_customer_ref_number;
alter table assets drop index smart_search_rfid;
alter table assets drop index smart_search_serial_number;

alter table assets add index idx_asssets_rfidnumber (rfidnumber);
alter table assets drop index index_products_on_rfidnumber;

alter table assets add index idx_asssets_identifier (identifier);
alter table assets drop index index_products_on_serialnumber;

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

alter table assets add index idx_asssets_location (location);
alter table assets drop index freeform_location_idx;

alter table assets add index idx_asssets_state_tenantid_linkedid (`state`,`tenant_id`,`linked_id`);
alter table assets drop index index_products_state_tenantid_linkedid;

alter table assets add index idx_asssets_ownerid_tenantid_mobileguid_state (`owner_id`,`tenant_id`,`mobileguid`,`state`);
alter table assets drop index index_owner_id_tenant_id_mobileguid_state;


























