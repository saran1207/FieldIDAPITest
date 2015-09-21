ALTER TABLE criteria ADD required TINYINT(20) NOT NULL DEFAULT false;

UPDATE criteria SET required = FALSE;