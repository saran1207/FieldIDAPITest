ALTER TABLE criteriasections ADD required TINYINT(20) NOT NULL DEFAULT false;

UPDATE criteriasections SET required = FALSE;