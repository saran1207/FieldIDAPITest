alter table subevents add column thing_event_type_id bigint not null;

update subevents se, events e set se.thing_event_type_id = e.id where se.event_id = e.id;