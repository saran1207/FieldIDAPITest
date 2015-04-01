DROP TRIGGER IF EXISTS trig_after_insert_assets//
CREATE DEFINER = CURRENT_USER TRIGGER trig_after_insert_assets AFTER INSERT ON assets FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSET_INSERT', NEW.tenant_id);
    END IF;
  END//

DROP TRIGGER IF EXISTS trg_asset_identifiedby_fix//
CREATE DEFINER=CURRENT_USER TRIGGER trg_asset_identifiedby_fix BEFORE UPDATE ON assets FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      IF(NEW.identifiedby_id IS NULL) THEN
        SET NEW.identifiedby_id = NEW.createdby;
      END IF;
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_assets//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_assets AFTER UPDATE ON assets FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSET_UPDATE', NEW.tenant_id);
      IF NEW.state = 'ARCHIVED' THEN
        DELETE FROM most_recent_completed_thing_events WHERE asset_id = NEW.id;
      END IF;
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_assetstatus//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_assetstatus AFTER UPDATE ON assetstatus FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSETSTATUS', NEW.tenant_id);
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_assettypegroups//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_assettypegroups AFTER UPDATE ON assettypegroups FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSETTYPEGROUP', NEW.tenant_id);
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_assettypes//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_assettypes AFTER UPDATE ON assettypes FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ASSETTYPE', NEW.tenant_id);
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_eventtypes//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_eventtypes AFTER UPDATE on eventtypes FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      IF NEW.state = 'ARCHIVED' THEN
        DELETE FROM most_recent_completed_thing_events WHERE type_id = NEW.id;
      END IF;
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_insert_events//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_insert_events AFTER INSERT ON masterevents FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_INSERT');
      INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_INSERT');
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_events//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_events AFTER UPDATE ON masterevents FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO criteria_trends_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_UPDATE');
      INSERT IGNORE INTO event_index_queue_items (item_id, type) VALUES (NEW.event_id, 'EVENT_UPDATE');
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_orders//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_orders AFTER UPDATE ON orders FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ORDER', NEW.tenant_id);
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_org_base//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_org_base AFTER UPDATE ON org_base FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'ORG', NEW.tenant_id);
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_after_update_predefinedlocations//
CREATE DEFINER=CURRENT_USER TRIGGER trig_after_update_predefinedlocations AFTER UPDATE ON predefinedlocations FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
      INSERT IGNORE INTO index_queue_items (item_id, type, tenant_id) VALUES (NEW.id, 'PREDEFINEDLOCATION', NEW.tenant_id);
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_most_recently_completed_event_insert//
CREATE DEFINER=CURRENT_USER TRIGGER trig_most_recently_completed_event_insert AFTER INSERT ON thing_events FOR EACH ROW
  BEGIN
    IF @disable_triggers IS NULL THEN
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
    END IF;
  END//

DROP TRIGGER IF EXISTS trig_most_recently_completed_event_update//
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
    END IF;
  END//
