CREATE TABLE most_recent_completed_thing_events (
  id BIGINT(20) NOT NULL AUTO_INCREMENT,
  event_id BIGINT(20) NOT NULL,
  asset_id BIGINT(20) NOT NULL,
  type_id BIGINT(20) NOT NULL,
  completedDate DATETIME NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_most_recent_completed_thing_events FOREIGN KEY (event_id) REFERENCES thing_events(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_most_recent_completed_thing_events_asset FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_most_recent_completed_thing_events_type FOREIGN KEY (type_id) REFERENCES eventtypes(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO most_recent_completed_thing_events (event_id, asset_id, type_id, completedDate)
SELECT id AS event_id, asset_id, type_id, max(completedDate) AS completedDate
FROM (SELECT e.id, t.asset_id, e.type_id, m.completedDate
      FROM events e, masterevents m, thing_events t, eventtypes et
      WHERE e.id = m.event_id
	AND e.id = t.id
        AND e.type_id = et.id
        AND m.workflow_state = 'COMPLETED'
        AND m.state = 'ACTIVE'
        AND et.action_type = FALSE
      ORDER BY e.type_id, t.asset_id, m.completedDate DESC) AS s
GROUP BY type_id, asset_id;