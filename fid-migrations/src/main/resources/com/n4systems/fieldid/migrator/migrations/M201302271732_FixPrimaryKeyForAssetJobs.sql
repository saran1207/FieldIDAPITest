alter table projects_assets drop primary key;
alter table projects_assets add primary key (projects_id,asset_id,orderidx);