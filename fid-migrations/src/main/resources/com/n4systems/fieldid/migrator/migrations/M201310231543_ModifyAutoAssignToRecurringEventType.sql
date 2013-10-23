update recurring_asset_type_events set autoassign = 0 where autoassign IS NULL;
alter table recurring_asset_type_events modify autoassign tinyint(1) DEFAULT 0 NOT NULL;