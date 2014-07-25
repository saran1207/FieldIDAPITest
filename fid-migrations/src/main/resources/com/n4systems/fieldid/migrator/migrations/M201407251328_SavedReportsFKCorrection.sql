ALTER TABLE saved_reports
DROP FOREIGN KEY fk_saved_reports_on_thing_event_type_id;
ALTER TABLE saved_reports
DROP eventTypeId;
ALTER TABLE saved_reports
CHANGE thingEventTypeId eventTypeId BIGINT(20);
ALTER TABLE saved_reports
ADD CONSTRAINT fk_saved_reports_on_thing_event_type_id
FOREIGN KEY (eventTypeId)
REFERENCES eventtypes(id);
