DROP TRIGGER IF EXISTS trig_after_update_events//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_events AFTER UPDATE ON masterevents FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_UPDATE');
      INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_UPDATE');

      SET @ASSET_ID = (SELECT asset_id FROM masterevents me INNER JOIN thing_events te ON me.event_id = te.id WHERE me.event_id = NEW.event_id);

      IF NEW.workflow_state = 'COMPLETED' AND @ASSET_ID IS NOT NULL THEN
        IF NEW.state = 'ACTIVE' THEN
          UPDATE assets
          SET last_event_completed_date = NEW.completedDate
          WHERE id = @ASSET_ID
                AND (last_event_completed_date < NEW.completedDate
                     OR last_event_completed_date IS NULL);
        ELSEIF (NEW.state = 'ARCHIVED' OR NEW.state = 'RETIRED') AND OLD.state = 'ACTIVE' THEN
          UPDATE assets a
          SET a.last_event_completed_date = (
            SELECT MAX(me.completedDate)
            FROM masterevents me
              INNER JOIN thing_events te ON me.event_id = te.id
              INNER JOIN events e ON me.event_id = e.id
              INNER JOIN eventtypes et ON e.type_id = et.id
            WHERE te.asset_id = @ASSET_ID
                  AND me.event_id <> NEW.event_id
                  AND me.state = 'ACTIVE'
                  AND me.workflow_state = 'COMPLETED'
                  AND et.state = 'ACTIVE'
          )
          WHERE a.id = @ASSET_ID;
        END IF;
      END IF;
    END IF;
  END//