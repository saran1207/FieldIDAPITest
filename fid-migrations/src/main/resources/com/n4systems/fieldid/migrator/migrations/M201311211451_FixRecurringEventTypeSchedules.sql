alter table recurring_asset_type_events add column thing_event_type_id bigint not null;

update recurring_asset_type_events set thing_event_type_id = event_type_id;

alter table recurring_asset_type_events change column event_type_id event_type_id bigint default null;