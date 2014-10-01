DROP TRIGGER IF EXISTS trig_most_recently_completed_event_insert;//
DROP TRIGGER IF EXISTS trig_most_recently_completed_event_update;//

CREATE TRIGGER trig_most_recently_completed_event_insert AFTER INSERT ON thing_events FOR EACH ROW
  BEGIN
    SET @isActionType = ( SELECT t.action_type FROM events e, eventtypes t WHERE e.type_id = t.id AND e.id = NEW.id);
    SET @WORKFLOW_STATE = (SELECT workflow_state FROM masterevents WHERE event_id = NEW.id);

    IF @isActionType = FALSE AND @WORKFLOW_STATE = 'COMPLETED' THEN
      INSERT INTO most_recent_completed_thing_events (event_id, asset_id, type_id, completedDate)
      SELECT id AS event_id, asset_id, type_id, max(completedDate) AS completedDate
      FROM (
        SELECT e.id, t.asset_id, e.type_id, m.completedDate
        FROM events e, masterevents m, thing_events t, eventtypes et
        WHERE e.id = m.event_id
        AND e.id = NEW.id
        AND e.id = t.id
            AND e.type_id = et.id
            AND m.workflow_state = 'COMPLETED'
            AND m.state = 'ACTIVE'
            AND et.action_type = FALSE
            AND m.completedDate IS NOT NULL
        ORDER BY e.type_id, t.asset_id, m.completedDate DESC) AS s;
    END IF;
  END;//

CREATE TRIGGER trig_most_recently_completed_event_update AFTER UPDATE ON thing_events FOR EACH ROW
BEGIN
    DELETE FROM most_recent_completed_thing_events WHERE event_id = NEW.id;

  SET @isActionType = ( SELECT t.action_type FROM events e, eventtypes t WHERE e.type_id = t.id AND e.id = NEW.id);
  SET @WORKFLOW_STATE = (SELECT workflow_state FROM masterevents WHERE event_id = NEW.id);

  IF @isActionType = FALSE AND @WORKFLOW_STATE = 'COMPLETED' THEN
    INSERT INTO most_recent_completed_thing_events (event_id, asset_id, type_id, completedDate)
    SELECT id AS event_id, asset_id, type_id, max(completedDate) AS completedDate
    FROM (
      SELECT e.id, t.asset_id, e.type_id, m.completedDate
      FROM events e, masterevents m, thing_events t, eventtypes et
      WHERE e.id = m.event_id
      AND e.id = NEW.id
      AND e.id = t.id
          AND e.type_id = et.id
          AND m.workflow_state = 'COMPLETED'
          AND m.state = 'ACTIVE'
          AND et.action_type = FALSE
          AND m.completedDate IS NOT NULL
      ORDER BY e.type_id, t.asset_id, m.completedDate DESC) AS s;
    END IF ;
END;//
