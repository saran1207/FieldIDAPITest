ALTER TABLE masterevents ADD COLUMN created_platform_type VARCHAR(30), ADD COLUMN modified_platform_type VARCHAR(30);

ALTER TABLE assets ADD COLUMN created_platform_type VARCHAR(30), ADD COLUMN modified_platform_type VARCHAR(30);