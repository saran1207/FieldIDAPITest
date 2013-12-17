ALTER TABLE thing_events ADD COLUMN recurring_event_id BIGINT(21);
UPDATE thing_events t, masterevents m SET t.recurring_event_id=m.recurring_event_id where t.id = m.event_id;
ALTER TABLE thing_events ADD CONSTRAINT fk_events_recurring_thing_events FOREIGN KEY (recurring_event_id) REFERENCES recurring_asset_type_events(id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE masterevents DROP FOREIGN KEY fk_events_recurring_events;
ALTER TABLE masterevents DROP COLUMN recurring_event_id;

ALTER TABLE place_events ADD COLUMN recurring_event_id BIGINT(21);
ALTER TABLE place_events ADD CONSTRAINT fk_events_recurring_place_events FOREIGN KEY (recurring_event_id) REFERENCES recurring_place_events(id) ON UPDATE NO ACTION ON DELETE NO ACTION;
