RENAME TABLE loto_custom_details TO loto_settings;

ALTER TABLE loto_settings ADD COLUMN annotation_type VARCHAR(255) NOT NULL;

UPDATE loto_settings SET annotation_type='ARROW_STYLE';

UPDATE loto_settings SET annotation_type='CALL_OUT_STYLE' WHERE tenant_id IN (SELECT DISTINCT tenant_id FROM procedure_definitions);
