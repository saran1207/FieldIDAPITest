ALTER TABLE procedure_definitions ADD COLUMN origin_date DATETIME DEFAULT NULL;

ALTER TABLE procedure_definitions ADD COLUMN retire_date DATETIME DEFAULT NULL;

ALTER TABLE procedure_definitions ADD COLUMN approved_by_id BIGINT DEFAULT NULL;