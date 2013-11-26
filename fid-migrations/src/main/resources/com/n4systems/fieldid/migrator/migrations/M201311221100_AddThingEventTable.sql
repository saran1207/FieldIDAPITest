CREATE TABLE thing_events (
  id bigint NOT NULL,
  asset_id bigint NOT NULL,
  thing_event_type_id bigint not null,
  prooftesttype varchar(255) default null,
  peakload varchar(255) default null,
  duration varchar(255) default null,
  peakloadduration varchar(255) default null,
  PRIMARY KEY (id),
  CONSTRAINT fk_thing_events_on_events FOREIGN KEY (id) REFERENCES events (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

insert into thing_events (select e.id,e.asset_id,e.type_id,me.peakload,me.duration,me.peakloadduration from events e, masterevents me where me.event_id=e.id);