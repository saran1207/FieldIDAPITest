ALTER TABLE notificationsettings ADD COLUMN assettype_id BIGINT(20) DEFAULT NULL, ADD COLUMN eventtype_id BIGINT(20) DEFAULT NULL;

ALTER TABLE notificationsettings ADD CONSTRAINT fk_notificationsettings_asset_type FOREIGN KEY(assettype_id) REFERENCES assettypes(id);
ALTER TABLE notificationsettings ADD CONSTRAINT fk_notificationsettings_event_type FOREIGN KEY(eventtype_id) REFERENCES eventtypes(id);

UPDATE notificationsettings n INNER JOIN notificationsettings_assettypes a ON n.id = a.notificationsettings_id SET n.assettype_id = a.assettype_id;

UPDATE notificationsettings n INNER JOIN notificationsettings_eventtypes a ON n.id = a.notificationsettings_id SET n.eventtype_id = a.eventtype_id;

ALTER TABLE notificationsettings CHANGE assetStatus assetstatus_id BIGINT(20);
ALTER TABLE notificationsettings CHANGE assetTypeGroup assettypegroup_id BIGINT(20);
ALTER TABLE notificationsettings CHANGE eventtypegroup eventtypegroup_id BIGINT(20);