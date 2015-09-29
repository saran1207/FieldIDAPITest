AlTER TABLE saved_items DROP COLUMN procedure_defs_id;

ALTER TABLE users DROP COLUMN lastRunProcedureDefsId;

DROP TABLE saved_procedure_defs;