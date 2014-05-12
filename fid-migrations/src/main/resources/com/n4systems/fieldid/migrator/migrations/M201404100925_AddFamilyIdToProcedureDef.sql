ALTER TABLE procedure_definitions ADD family_id BIGINT(20) NOT NULL;

UPDATE procedure_definitions SET family_id = 1;