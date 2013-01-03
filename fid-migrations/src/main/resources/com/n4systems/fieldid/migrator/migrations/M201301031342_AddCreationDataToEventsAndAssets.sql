ALTER TABLE masterevents ADD COLUMN created_platform VARCHAR(200);
ALTER TABLE masterevents ADD COLUMN modified_platform VARCHAR(200);

ALTER TABLE assets ADD COLUMN created_platform VARCHAR(200);
ALTER TABLE assets ADD COLUMN modified_platform VARCHAR(200);