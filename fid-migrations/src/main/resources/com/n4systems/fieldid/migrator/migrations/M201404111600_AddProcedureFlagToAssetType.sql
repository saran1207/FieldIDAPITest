ALTER TABLE assettypes ADD hasProcedures TINYINT (20) NOT NULL DEFAULT false;

UPDATE assettypes
SET hasProcedures = TRUE
WHERE id IN (SELECT * from (SELECT distinct at.id
                            FROM procedure_definitions pd, assets a, assettypes at
                            WHERE pd.asset_id = a.id and a.type_id = at.id) as T);