alter table subevents drop column thing_event_type_id;

create temporary table recurring_asset_type_events_for_cleanup select id as recurring_event_id from recurring_asset_type_events where thing_event_type_id not in (select id from thing_event_types);

update thing_events set recurring_event_id = null where recurring_event_id in (select recurring_event_id from recurring_asset_type_events_for_cleanup );

update masterevents set recurring_event_id = null where recurring_event_id is not null;

drop temporary table recurring_asset_type_events_for_cleanup;

delete from recurring_asset_type_events where thing_event_type_id not in (select id from thing_event_types);

alter table recurring_asset_type_events add constraint fk_recurring_asset_type_events_thing_event_types foreign key (thing_event_type_id) references thing_event_types(id);