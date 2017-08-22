ALTER TABLE masterevents_subevents DROP FOREIGN KEY masterevents_subevents_ibfk_2;
ALTER TABLE masterevents_subevents DROP INDEX index_masterevents_subevents_on_subevents_event;
ALTER TABLE masterevents_subevents ADD CONSTRAINT masterevents_subevents_ibfk_2 FOREIGN KEY (subevents_event_id) REFERENCES events(id) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE masterevents_subevents ADD INDEX index_masterevents_subevents_on_subevents_event (subevents_event_id);