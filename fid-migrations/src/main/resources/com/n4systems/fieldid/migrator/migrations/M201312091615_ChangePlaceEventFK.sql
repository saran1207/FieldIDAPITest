alter table place_events drop foreign key fk_place_events_on_place;
alter table place_events add constraint fk_place_events_on_place foreign key (place_id) references org_base(id);
