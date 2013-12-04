drop table if exists place_event_types;

CREATE TABLE place_event_types (
  id bigint NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_place_event_types_on_event_types FOREIGN KEY (id) REFERENCES eventtypes (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

drop table if exists place_events;

CREATE TABLE place_events (
  id bigint NOT NULL,
  place_id bigint NOT NULL,
  place_event_type_id bigint not null,
  PRIMARY KEY (id),
  CONSTRAINT fk_place_events_on_events FOREIGN KEY (id) REFERENCES events (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_place_events_on_place FOREIGN KEY (id) REFERENCES org_base (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

