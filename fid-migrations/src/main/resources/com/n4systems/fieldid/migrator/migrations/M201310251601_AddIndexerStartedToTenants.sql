alter table tenants add column asset_indexer_started boolean not null;
update tenants set asset_indexer_started = false;