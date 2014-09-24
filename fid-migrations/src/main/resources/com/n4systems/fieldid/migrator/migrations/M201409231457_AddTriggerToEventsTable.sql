DELIMITER $$

DROP TRIGGER IF EXISTS trig_most_recently_completed_event_insert $$
DROP TRIGGER IF EXISTS trig_most_recently_completed_event_update $$
DROP TRIGGER IF EXISTS trig_after_update_eventtypes $$
DROP TRIGGER IF EXISTS trig_after_update_assets $$

CREATE TRIGGER trig_most_recently_completed_event_insert AFTER INSERT ON thing_events FOR EACH ROW
BEGIN
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
END $$

CREATE TRIGGER trig_most_recently_completed_event_update AFTER UPDATE ON thing_events FOR EACH ROW
BEGIN
	DELETE FROM most_recent_completed_thing_events
	WHERE event_id = NEW.id;

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
END $$

CREATE TRIGGER trig_after_update_assets
AFTER UPDATE ON assets
FOR EACH ROW
  BEGIN
    INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSET_UPDATE', NEW.tenant_id);
    IF NEW.state = 'ARCHIVED' THEN
      DELETE FROM most_recent_completed_thing_events WHERE asset_id = NEW.id;
    END IF;
  END
$$

CREATE TRIGGER trig_after_update_eventtypes
AFTER UPDATE on eventtypes
FOR EACH ROW
  BEGIN
    IF NEW.state = 'ARCHIVED' THEN
      DELETE FROM most_recent_completed_thing_events WHERE type_id = NEW.id;
    END IF;
  END
$$

DELIMITER ;
