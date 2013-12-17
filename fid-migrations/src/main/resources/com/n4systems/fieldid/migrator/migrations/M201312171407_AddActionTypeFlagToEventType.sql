alter table eventtypes add column action_type tinyint not null default false;

update eventtypes et, action_event_types aet set et.action_type=true where et.id=aet.id;