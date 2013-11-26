alter table saved_reports add column thingEventTypeId bigint default null;

alter table saved_reports add constraint fk_saved_reports_on_thing_event_type_id foreign key (thingEventTypeId) references thing_events(id);

update saved_reports set thingEventTypeId = eventTypeId;