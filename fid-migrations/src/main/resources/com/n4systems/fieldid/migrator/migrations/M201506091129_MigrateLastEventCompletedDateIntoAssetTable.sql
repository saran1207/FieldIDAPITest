UPDATE assets
INNER JOIN (
SELECT DISTINCT a.id as asset_id, MAX(me.completedDate) as completed_date
FROM assets a
INNER JOIN thing_events te ON a.id = te.asset_id
INNER JOIN masterevents me ON te.id = me.event_id
GROUP BY a.id) s
ON s.asset_id = assets.id
SET assets.last_event_completed_date = s.completed_date;