CREATE TABLE action_event_types (
  id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_action_event_types_on_event_types FOREIGN KEY (id) REFERENCES eventtypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

insert into action_event_types (select et.id from eventtypes et, eventtypegroups etg where et.group_id=etg.id and etg.action=true);

delete from associated_event_types where thing_event_type_id in (select id from action_event_types);
delete from thing_event_types_supported_proof_tests where thing_event_id in (select id from action_event_types);
delete from thing_event_types where id in (select id from action_event_types);