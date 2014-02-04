drop table if exists orgs_place_event_types;

create table orgs_place_event_types(
  org_id bigint not null,
  place_event_type_id bigint not null,
  CONSTRAINT fk_orgs_place_event_types_on_orgs FOREIGN KEY (org_id) REFERENCES org_base (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_orgs_place_event_types_on_place_event_types FOREIGN KEY (place_event_type_id) REFERENCES place_event_types (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);