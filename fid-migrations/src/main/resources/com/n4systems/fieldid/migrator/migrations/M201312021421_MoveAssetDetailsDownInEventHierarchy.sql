alter table subevents add column asset_id bigint not null;
alter table subevents add column asset_status_id bigint default null;

alter table thing_events add column asset_status_id bigint default null;

update subevents s, events e set s.asset_id=e.asset_id where s.event_id = e.id;
update subevents s, events e set s.asset_status_id=e.assetstatus_id where s.event_id = e.id;

update thing_events t, events e set t.asset_status_id=e.assetstatus_id where t.id = e.id;