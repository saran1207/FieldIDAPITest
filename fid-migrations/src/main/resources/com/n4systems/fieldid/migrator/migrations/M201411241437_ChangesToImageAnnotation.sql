ALTER TABLE procedure_definitions ADD COLUMN annotation_type VARCHAR(255) NOT NULL;

ALTER TABLE image_annotation ADD COLUMN x_tail float;

ALTER TABLE image_annotation ADD COLUMN y_tail float;

UPDATE procedure_definitions SET annotation_type='CALL_OUT_STYLE';