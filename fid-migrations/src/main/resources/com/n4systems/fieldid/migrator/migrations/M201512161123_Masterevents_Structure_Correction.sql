SET FOREIGN_KEY_CHECKS=0//

ALTER TABLE masterevents DROP FOREIGN KEY masterevents_ibfk_1,
                         DROP FOREIGN KEY masterevents_ibfk_2//
ALTER TABLE assignee_notifications DROP FOREIGN KEY assignee_notifications_ibfk_1//
ALTER TABLE criteriaresults_actions DROP FOREIGN KEY criteriaresults_actions_ibfk_1,
DROP FOREIGN KEY criteriaresults_actions_ibfk_3//

DROP TRIGGER IF EXISTS trig_after_insert_events//
DROP TRIGGER IF EXISTS trig_after_update_events//
DROP TRIGGER IF EXISTS trig_most_recently_completed_event_update//
DROP TRIGGER IF EXISTS trig_most_recently_completed_event_insert//

ALTER TABLE masterevents ALGORITHM=INPLACE,
                         CHANGE event_id id BIGINT(20) NOT NULL//

ALTER TABLE masterevents ADD CONSTRAINT masterevents_ibfk_1 FOREIGN KEY (id) REFERENCES events(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
ADD CONSTRAINT masterevents_ibfk_2 FOREIGN KEY (trigger_event_id) REFERENCES masterevents(id)//
ALTER TABLE assignee_notifications ADD CONSTRAINT assignee_notifications_ibfk_1 FOREIGN KEY (event_id) REFERENCES masterevents(id)//
ALTER TABLE criteriaresults_actions ADD CONSTRAINT criteriaresults_actions_ibfk_1 FOREIGN KEY (event_id) REFERENCES masterevents(id)//

SET FOREIGN_KEY_CHECKS=1//

CREATE DEFINER=CURRENT_USER TRIGGER trig_after_insert_events AFTER INSERT ON masterevents FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.id, 'EVENT_INSERT');
      INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.id, 'EVENT_INSERT');
    END IF;
  END//


CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_events AFTER UPDATE ON masterevents FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.id, 'EVENT_UPDATE');
      INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.id, 'EVENT_UPDATE');

      SET @ASSET_ID = (SELECT asset_id FROM masterevents me INNER JOIN thing_events te ON me.id = te.id WHERE me.id = NEW.id);

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
              INNER JOIN thing_events te ON me.id = te.id
              INNER JOIN events e ON me.id = e.id
              INNER JOIN eventtypes et ON e.type_id = et.id
            WHERE te.asset_id = @ASSET_ID
                  AND me.id <> NEW.id
                  AND me.state = 'ACTIVE'
                  AND me.workflow_state = 'COMPLETED'
                  AND et.state = 'ACTIVE'
          )
          WHERE a.id = @ASSET_ID;
        END IF;
      END IF;
    END IF;
  END//


CREATE DEFINER=CURRENT_USER TRIGGER trig_most_recently_completed_event_insert AFTER INSERT ON thing_events FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      SET @isActionType = ( SELECT t.action_type FROM events e, eventtypes t WHERE e.type_id = t.id AND e.id = NEW.id);
      SET @WORKFLOW_STATE = (SELECT workflow_state FROM masterevents WHERE id = NEW.id);

      IF @isActionType = FALSE AND @WORKFLOW_STATE = 'COMPLETED' THEN
        INSERT INTO most_recent_completed_thing_events (event_id, asset_id, type_id, completedDate)
          SELECT id AS event_id, asset_id, type_id, max(completedDate) AS completedDate
          FROM (
                 SELECT e.id, t.asset_id, e.type_id, m.completedDate
                 FROM events e, masterevents m, thing_events t, eventtypes et
                 WHERE e.id = m.id
                       AND e.id = NEW.id
                       AND e.id = t.id
                       AND e.type_id = et.id
                       AND m.workflow_state = 'COMPLETED'
                       AND m.state = 'ACTIVE'
                       AND et.action_type = FALSE
                       AND m.completedDate IS NOT NULL
                 ORDER BY e.type_id, t.asset_id, m.completedDate DESC) AS s;
      END IF;
    END IF;
  END//


CREATE DEFINER=CURRENT_USER TRIGGER trig_most_recently_completed_event_update AFTER UPDATE ON thing_events FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      DELETE FROM most_recent_completed_thing_events WHERE event_id = NEW.id;

      SET @isActionType = ( SELECT t.action_type FROM events e, eventtypes t WHERE e.type_id = t.id AND e.id = NEW.id);
      SET @WORKFLOW_STATE = (SELECT workflow_state FROM masterevents WHERE event_id = NEW.id);

      IF @isActionType = FALSE AND @WORKFLOW_STATE = 'COMPLETED' THEN
        INSERT INTO most_recent_completed_thing_events (event_id, asset_id, type_id, completedDate)
          SELECT id AS event_id, asset_id, type_id, max(completedDate) AS completedDate
          FROM (
                 SELECT e.id, t.asset_id, e.type_id, m.completedDate
                 FROM events e, masterevents m, thing_events t, eventtypes et
                 WHERE e.id = m.id
                       AND e.id = NEW.id
                       AND e.id = t.id
                       AND e.type_id = et.id
                       AND m.workflow_state = 'COMPLETED'
                       AND m.state = 'ACTIVE'
                       AND et.action_type = FALSE
                       AND m.completedDate IS NOT NULL
                 ORDER BY e.type_id, t.asset_id, m.completedDate DESC) AS s;
      END IF;
    END IF;
  END//