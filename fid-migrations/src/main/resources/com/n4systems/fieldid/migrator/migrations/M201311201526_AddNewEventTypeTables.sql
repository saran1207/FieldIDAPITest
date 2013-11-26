CREATE TABLE thing_event_types (
  id bigint NOT NULL,
  master boolean not null,
  PRIMARY KEY (id),
  CONSTRAINT fk_thing_event_types_on_event_types FOREIGN KEY (id) REFERENCES eventtypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE thing_event_types_supported_proof_tests (
  thing_event_id bigint(20) NOT NULL,
  element varchar(255) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (thing_event_id, element),
  KEY index_thing_event_types_supported_proof_tests_on_thing_event_ud (thing_event_id),
  CONSTRAINT fk_thing_event_types_supported_proof_tests_event_type FOREIGN KEY (thing_event_id) REFERENCES thing_event_types (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

insert into thing_event_types (select id,master from eventtypes);