
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


alter table assets add index idx_asssets_location (location);
alter table assets drop index freeform_location_idx;
