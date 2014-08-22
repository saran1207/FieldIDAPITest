ALTER TABLE saved_reports
DROP FOREIGN KEY fk_saved_reports_on_thing_event_type_id;

ALTER TABLE saved_reports
ADD CONSTRAINT fk_saved_reports_on_thing_event_type_id
FOREIGN KEY (thingEventTypeId)
REFERENCES eventtypes(id);

