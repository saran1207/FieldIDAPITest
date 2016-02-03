CREATE TEMPORARY TABLE tmp_most_recent_completed_thing_events
    SELECT m1.*
    FROM most_recent_completed_thing_events m1
      LEFT JOIN most_recent_completed_thing_events m2 ON
                                                        m1.asset_id = m2.asset_id AND
                                                        m1.type_id = m2.type_id AND
                                                        (m1.completedDate < m2.completedDate OR m1.completedDate = m2.completedDate AND m1.id < m2.id)
    WHERE m2.asset_id IS NULL;

TRUNCATE most_recent_completed_thing_events;

CREATE UNIQUE INDEX idx_most_recent_completed_thing_events_on_asset_and_type ON most_recent_completed_thing_events (asset_id, type_id);

INSERT INTO most_recent_completed_thing_events SELECT * FROM tmp_most_recent_completed_thing_events;

DROP TEMPORARY TABLE tmp_most_recent_completed_thing_events;