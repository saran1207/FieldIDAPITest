DROP TRIGGER IF EXISTS trig_after_insert_events//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_insert_events AFTER INSERT ON masterevents FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_INSERT');
      INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_INSERT');
    END IF;
  END//