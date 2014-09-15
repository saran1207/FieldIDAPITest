ALTER TABLE criteriasections ADD COLUMN optional TINYINT (1);

UPDATE criteriasections SET optional = FALSE;