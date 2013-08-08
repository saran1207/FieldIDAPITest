drop trigger trig_after_insert_events;
drop trigger trig_after_update_events;

CREATE TRIGGER trig_after_insert_events AFTER INSERT ON masterevents FOR EACH ROW INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_INSERT');
CREATE TRIGGER trig_after_update_events AFTER UPDATE ON masterevents FOR EACH ROW INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_UPDATE');