DROP TRIGGER IF EXISTS trig_most_recently_completed_event_insert;//

CREATE TRIGGER trig_most_recently_completed_event_insert AFTER INSERT ON thing_events FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      SET @isActionType = ( SELECT t.action_type FROM events e, eventtypes t WHERE e.type_id = t.id AND e.id = NEW.id);
      SET @WORKFLOW_STATE = (SELECT workflow_state FROM masterevents WHERE id = NEW.id);

      IF @isActionType = FALSE AND @WORKFLOW_STATE = 'COMPLETED' THEN

	      DELETE FROM most_recent_completed_thing_events
	        WHERE type_id = (SELECT type_id FROM events WHERE id = NEW.id)
            AND asset_id = NEW.asset_id;

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
  END;//


-- Clean up 'most_recent_completed_thing_events' table

DELETE FROM most_recent_completed_thing_events
WHERE id NOT IN (SELECT id FROM
  (SELECT MAX(id) as id
   FROM most_recent_completed_thing_events
   GROUP BY asset_id, type_id) x);