
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






