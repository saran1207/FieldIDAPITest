alter table thing_events add column owner_id bigint not null;

update thing_events t, masterevents m set t.owner_id = m.owner_id where t.id=m.event_id;

alter table masterevents change column owner_id owner_id bigint default null;