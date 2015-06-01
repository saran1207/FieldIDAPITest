ALTER TABLE identifier_counters ADD COLUMN resetAnnually TINYINT(1) NOT NULL;

UPDATE identifier_counters SET resetAnnually = TRUE WHERE daystoreset <> 3650;

ALTER TABLE identifier_counters DROP COLUMN daystoreset;