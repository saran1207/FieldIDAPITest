ALTER TABLE masterevents ADD COLUMN created_platform_type VARCHAR(30), ADD COLUMN modified_platform_type VARCHAR(30), ADD COLUMN created_platform VARCHAR(200), ADD COLUMN modified_platform VARCHAR(200);

ALTER TABLE assets ADD COLUMN created_platform_type VARCHAR(30), ADD COLUMN modified_platform_type VARCHAR(30), ADD COLUMN created_platform VARCHAR(200), ADD COLUMN modified_platform VARCHAR(200);